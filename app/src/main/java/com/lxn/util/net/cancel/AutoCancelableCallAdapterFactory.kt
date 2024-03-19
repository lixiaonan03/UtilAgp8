package com.lxn.util.net.cancel

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.SkipCallbackExecutor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 学习参考艾虎当初写的 自动取消前一个 2个相同请求参数的请求
 * 实现方式是通过自定义CallAdapter.Factory 的方式实现
 * @author：李晓楠
 * 时间：2024/3/19 11:00
 */
class AutoCancelableCallAdapterFactory: CallAdapter.Factory(){
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //先判断方法返回的格式对不对
        if (getRawType(returnType) != AutoCancelableCall::class.java) {
            return null
        }
        require(returnType is ParameterizedType) { "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>" }

//        // retrofit 应当接受一个 OkHttpClient 作为 CallFactory，这样我们才能访问其 Dispatcher
//        final okhttp3.Call.Factory callFactory = retrofit.callFactory();
//        if (! (callFactory instanceof OkHttpClient)) {
//            throw new IllegalArgumentException(
//                    "You must register a OkHttpClient instance as the call-factory of retrofit");
//        }
        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        val executor = if (isAnnotationPresent(annotations, SkipCallbackExecutor::class.java)
        ) null else retrofit.callbackExecutor()

        return object : CallAdapter<Any?, Call<Any?>> {
            override fun responseType(): Type {
                return responseType
            }

            override fun adapt(call: Call<Any?>): Call<Any?> {
                return AutoCancelableCallImpl(call /*, dispatcher*/, executor!!)
            }
        }
    }

    private fun isAnnotationPresent(
        annotations: Array<out Annotation>,
        cls: Class<out Annotation>
    ): Boolean {
        for (annotation in annotations) {
            if (cls.isInstance(annotation)) {
                return true
            }
        }
        return false
    }
}