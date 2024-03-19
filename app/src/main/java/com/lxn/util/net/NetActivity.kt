package com.lxn.util.net

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.lxn.util.base.BaseActivity
import com.lxn.util.databinding.ActivityNetBinding
import com.lxn.util.net.cancel.AutoCancelableCall
import com.lxn.util.net.cancel.AutoCancelableCallAdapterFactory
import com.lxn.util.net.cancel.AutoCancelableCallImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * 网络库学习的 包括retrofit 和 okhttp 的使用
 */
class NetActivity : BaseActivity() {
    //进入当前页面
    companion object {
        fun startActivity(context: Activity) {
            val intent = Intent(context, NetActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var vb: ActivityNetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityNetBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "网络库使用学习"
        vb.tvNetRetrofit.setOnClickListener {
            goNetForRetrofitForAutoCancel()
        }
        vb.tvNetRetrofitForKt.setOnClickListener {
            goNetForRetrofitForKt()
        }

    }

    /**
     * 获取okhttp 客户端
     */
    private fun newOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        //通过这个设置okhttp 不能用代理链接来访问 防止抓包
//        builder.proxy(Proxy.NO_PROXY)
        //错误重连
        builder.retryOnConnectionFailure(true)
        // 日志拦截器，用来记录所有的网络请求和响应
        // 这个拦截器应该放到所有拦截器的最后，用来监听真正的请求/响应
        val logging = HttpLoggingInterceptor { message: String? ->
            Log.i(
                "lxnNet",
                message!!
            )
        }
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(logging)
        return builder.build()
    }

    /**
     * Retrofit 网络请求的使用
     */
    private fun goNetForRetrofit() {
        // retrofit 的使用
        val retrofit = Retrofit.Builder()
            .client(newOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiService.BASE_URL)
            .build()
        val mService = retrofit.create(ApiService::class.java)
        val call = mService.getWxArticleForCommon()
        call.enqueue(object : retrofit2.Callback<ApiResponse<List<WxArticleBean>>> {
            override fun onResponse(
                call: retrofit2.Call<ApiResponse<List<WxArticleBean>>>,
                response: retrofit2.Response<ApiResponse<List<WxArticleBean>>>
            ) {
                Log.i("lxnNet", "请求所成功")
                if (response.body()?.isSuccess == true) {
                    vb.tvNetContentRetrofit.text = response.body()?.data.toString()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<ApiResponse<List<WxArticleBean>>>,
                t: Throwable
            ) {
                Log.i("lxnNet", "请求失败")
            }


        })
    }

    private fun goNetForRetrofitForAutoCancel() {
        // retrofit 的使用
        val retrofit = Retrofit.Builder()
            .client(newOkHttpClient())
            .addCallAdapterFactory(AutoCancelableCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiService.BASE_URL)
            .build()
        val mService = retrofit.create(ApiService::class.java)
        for (i in 0..2) {
            val call = mService.getWxArticleForAutoCancel()
            call.enqueue(object :
                AutoCancelableCall.CallbackForCancel<ApiResponse<List<WxArticleBean>>> {
                override fun onResponse(
                    call: retrofit2.Call<ApiResponse<List<WxArticleBean>>>,
                    response: retrofit2.Response<ApiResponse<List<WxArticleBean>>>
                ) {
                    Log.i("lxnNet", "请求成功")
                    if (response.body()?.isSuccess == true) {
                        vb.tvNetContentRetrofit.text = response.body()?.data.toString()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ApiResponse<List<WxArticleBean>>>,
                    t: Throwable
                ) {
                    Log.i("lxnNet", "请求失败")
                }

                override fun onCancel(call: AutoCancelableCallImpl<ApiResponse<List<WxArticleBean>>>?) {
                    super.onCancel(call)
                    Log.i("lxnNet", "请求取消===${call?.request()?.url}")
                }
            })
            //延迟100毫秒
//            Thread.sleep(100)
        }

    }

    /**
     * 通过协程访问的
     */
//一个全局单例的作用域
    private val mainScope = MainScope()
    private fun goNetForRetrofitForKt() {
        // retrofit 的使用
        val retrofit = Retrofit.Builder()
            .client(newOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiService.BASE_URL)
            .build()
        val mService = retrofit.create(ApiService::class.java)

        //这个返回的 ApiResponse<List<WxArticleBean>>
//        lifecycleScope.launch {
//            Log.i("lxnNet", "goNetForRetrofitForKt 中的线程===${Thread.currentThread().name}")
//            val call = mService.getWxArticleForKt()
//            Log.i("lxnNet", "goNetForRetrofitForKt 请求结束的线程===${Thread.currentThread().name}"+"返回的结果===${call.data}")
//            if(call.isSuccess){
//                vb.tvNetContentRetrofit.text = call.data.toString()
//            }else{
//                vb.tvNetContentRetrofit.text = "请求失败"
//            }
//        }
        //这个返回的是  Response<ApiResponse<List<WxArticleBean>>>
        lifecycleScope.launch {
            Log.i("lxnNet", "goNetForRetrofitForKt 中的线程===${Thread.currentThread().name}")
            val call = mService.getWxArticleForKt1()
            Log.i(
                "lxnNet",
                "goNetForRetrofitForKt 请求结束的线程===${Thread.currentThread().name}" + "返回的结果===${call.toString()}"
            )
            if (call.isSuccessful) {
                vb.tvNetContentRetrofit.text = call.toString()
            } else {
                vb.tvNetContentRetrofit.text = "请求失败"
            }
        }


    }
}