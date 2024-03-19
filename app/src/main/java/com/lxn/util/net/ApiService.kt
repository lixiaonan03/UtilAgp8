package com.lxn.util.net

import com.lxn.util.net.cancel.AutoCancelableCall
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

/**
  *  @author 李晓楠
  *  功能描述: 接口相关的 api
  *  时 间： 2022/10/27 15:03
  */
interface ApiService {

    @GET("wxarticle/chapters/json")
    suspend fun getWxArticle(): ApiResponse<List<WxArticleBean>>

    /**
     * get 接口模拟普通获取方式不用协程的
     */
    @GET("wxarticle/chapters/json")
    fun getWxArticleForCommon(): Call<ApiResponse<List<WxArticleBean>>>

    @GET("wxarticle/chapters/json")
    fun getWxArticleForAutoCancel(): AutoCancelableCall<ApiResponse<List<WxArticleBean>>>

    @GET("wxarticle/chapters/json")
    suspend fun getWxArticleForKt(): ApiResponse<List<WxArticleBean>>
    @GET("wxarticle/chapters/json")
    suspend fun getWxArticleForKt1(): Response<ApiResponse<List<WxArticleBean>>>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName: String, @Field("password") passWord: String): ApiResponse<User?>


    @FormUrlEncoded
    @POST("user/login")
    suspend fun login1(@Url url: String,@Field("username") userName: String, @Field("password") passWord: String): ApiResponse<User?>

    companion object {
        const val BASE_URL = "https://wanandroid.com/"
    }
}