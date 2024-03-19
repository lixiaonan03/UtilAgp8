package com.lxn.util.net.cancel

import android.util.Log
import okhttp3.Request
import okio.Buffer
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Arrays
import java.util.Objects
import java.util.concurrent.Executor

/**
 * @author：李晓楠
 * 时间：2024/3/19 11:12
 */
class AutoCancelableCallImpl<T>(call:retrofit2.Call<T> , callbackExecutor:Executor) : AutoCancelableCall<T> {


    private var mDelegate: Call<T>? = null
    private var mCallbackExecutor: Executor? = null

    companion object {
        val TAG = "AutoCancelableCall"
        //FIXME 这个map 集合一定要做成全局的  要不然每次请求都会重新创建一个 这样就起不到自动取消的效果的
        private val sWaitingRequestList: Map<AutoCancelableCallImpl.RequestKey, Call<*>> =
            HashMap<AutoCancelableCallImpl.RequestKey, Call<*>>(16, 1f)
    }
    init {
        mDelegate = call
        mCallbackExecutor = callbackExecutor
    }



    override fun clone(): Call<T> {
       return AutoCancelableCallImpl(mDelegate!!.clone(),mCallbackExecutor!!)
    }

    override fun execute(): Response<T> {
        //暂时先不处理同步的请求
        return mDelegate!!.execute()
    }

    override fun isExecuted(): Boolean {
       return mDelegate!!.isExecuted
    }

    override fun cancel() {
      return mDelegate!!.cancel()
    }

    override fun isCanceled(): Boolean {
       return mDelegate!!.isCanceled
    }

    override fun request(): Request {
       return mDelegate!!.request()
    }

    override fun timeout(): Timeout {
       return  mDelegate!!.timeout()
    }

    override fun enqueue(callback: Callback<T>) {
        //获取当前的请求
        val requestKey = RequestKey(request())
        //这里是一个同步的操作  因为不同网络请求的线程不同
        synchronized(sWaitingRequestList) {
            //先把现在这个请求放进map 中
            // 如果之前存在一个一摸一样的旧请求，取消掉它
            Log.i("lxnNet", "往里面放数据===${requestKey.hashCode()}===现在列表大小==${sWaitingRequestList.size}")
            val oldCall = (sWaitingRequestList as MutableMap).put(requestKey, this)
            Log.i("lxnNet", "查出来的旧的=====${oldCall?.request()?.let { RequestKey(oldCall.request()).hashCode() }}===现在列表大小==${sWaitingRequestList.size}")
            oldCall?.cancel()
        }
        mDelegate?.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                // 我们没有直接使用 sWaitingRequestList.remove(requestKey)，而是先判断集合中的请求是不是自己，
                // 如果是再真正移除。这一步是防止已经 cancel() 的请求把新的请求从集合中移除。
                // 使用 if (! isAutoCanceled()) { sWaitingRequestList.remove(requestKey); } 或许是一种
                // 更为优雅的写法，但这意味着我们要新增一个 boolean mAutoCanceled 标志位，还要和 retrofit 的 isCancel()
                // 区别开，另外还要考虑访问这个标志位时的线程同步问题，增大了许多成本。
                val oldCall: Call<*>? = sWaitingRequestList[requestKey]
                if (oldCall != null && oldCall.request() == requestKey.request) {
                    (sWaitingRequestList as MutableMap).remove(requestKey)
                }
                if(mCallbackExecutor!=null){
                    mCallbackExecutor!!.execute {
                        callback.onResponse(this@AutoCancelableCallImpl, response)
                    }
                }else{
                    callback.onResponse(this@AutoCancelableCallImpl, response)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                synchronized(sWaitingRequestList) {
                    val oldCall: Call<*>? = sWaitingRequestList[requestKey]
                    if (oldCall != null && oldCall.request() == requestKey.request) {
                        (sWaitingRequestList as MutableMap).remove(requestKey)
                    }
                }
                if(mCallbackExecutor!=null){
                    mCallbackExecutor!!.execute {
                        except(call, t)
                    }
                }else{
                   except(call, t)
                }


            }

            private fun except(call: Call<T>, t: Throwable) {
                if (isCanceled) {
                    if (callback is AutoCancelableCall.CallbackForCancel) {
                        callback.onCancel(this@AutoCancelableCallImpl)
                    }
                } else {
                    callback.onFailure(call, t)
                }
            }
        })
    }


    private class RequestKey internal constructor(val request: Request) {
        override fun hashCode(): Int {
            return Objects.hash(request.url, request.method /* , request.body() */)
        }

        /**
         * 比较两个请求是否完全相同
         * 由于请求的 header 中有个参数是随时间戳的变化而变化的，故不比较请求头，直接比较请求体。
         */
        override fun equals(obj: Any?): Boolean {
            if (this === obj) {
                return true
            }
            if (obj !is RequestKey) {
                return false
            }
            val p = request
            val q = obj.request

            // 快速比较: 如果 url 和请求方法不同，直接返回即可
            if (p.url != q.url) {
                return false
            }
            if (p.method != q.method) {
                return false
            }

            // 如果 url 和请求方法都相同，接下来就需要比较请求体了
            val pBody = p.body
            val qBody = q.body
            if (pBody === qBody) return true
            if (pBody == null || qBody == null) return false

            // 如果 mediaType 不同，直接判断为不同
            if (pBody.contentType() != qBody.contentType()) {
                return false
            }

            // 比较 contentLength，如果不相等直接返回。
            // 添加这个判断的原因是，项目中绝大多数请求都是 POST 请求，而且手动使用 "application/json charset=utf-8"
            // 创建请求体。这个过程创建的 RequestBody 是依靠字节数组实现的，因此其 contentLength() 函数相当快，且不会抛出异常。
            try {
                if (pBody.contentLength() != qBody.contentLength()) {
                    return false
                }
            } catch (e: IOException) {
                Log.e(
                    AutoCancelableCallImpl.TAG,
                    "equals: failed to compare #contentLength between [$p] and [$q]", e
                )
            }


            // 尝试比较两个 RequestBody 是否相同。RequestBody 作为一个抽象类，只有一个可用的 API 能够满足这种条件，writeTo().
            // writeTo() 用来将 RequestBody 中的内容写进一块缓冲区，我们可以借此比较二者是否相等。

            // 下面的实现依赖于 Buffer，先将 RequestBody 写进 Buffer，再读出字节数组，然后逐字节比较。在没有缓存时，这个过程涉及到
            // 2 次字节拷贝和最多 1 次内存分配。由于 Arrays.equals(byte[], byte[]) 使用逐字节遍历的算法，肯定是相当低效的。
            // 一个可能的优化点是自定义 BufferedSink，并使用 native 函数 GetByteArrayRegion() + memcmp() 实现高效比较。
            try {
                return Arrays.equals(requestBody(), obj.requestBody())
            } catch (e: IOException) {
                Log.e(
                    AutoCancelableCallImpl.TAG,
                    "equals: failed to compare #content between [$pBody] and [$qBody]", e
                )
            }
            // 执行到这里说明有异常发生，我们无法判断这两个请求体是否相同，返回 false
            return false
        }

        private var cachedRequestBody: ByteArray? = null

        @Throws(IOException::class)
        private fun requestBody(): ByteArray? {
            if (cachedRequestBody != null) {
                return cachedRequestBody
            }
            val body = request.body ?: return null
            synchronized(sBuffer) {
                return try {
                    body.writeTo(sBuffer)
                    cachedRequestBody = sBuffer.readByteArray()
                    cachedRequestBody
                } finally {
                    sBuffer.clear()
                }
            }
        }

        companion object {
            private val sBuffer = Buffer()
        }
    }

}