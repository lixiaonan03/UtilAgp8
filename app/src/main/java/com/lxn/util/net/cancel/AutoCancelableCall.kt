package com.lxn.util.net.cancel

import retrofit2.Call

/**
 * @author：李晓楠
 * 时间：2024/3/19 11:09

 *
 * 这个类继承自 {@link Call retrofit2.Call}，用来发起一个可自动取消的请求。
 * 所谓自动取消，指的是如果上一个请求还未返回，又发起了一个 *完全相同* 的请求，则前一个请求会被自动取消掉。
 *
 * 准备措施如下:
 * 1. 创建 Retrofit 实例时，将 {@link AutoCancelableCallAdapterFactory Factory} 传进 Retrofit
 * <code>
 *     public Retrofit buildRetrofit() {
 *         return new Retrofit
 *                      .Builder()
 *                      .baseUrl("https://www.this_url_does_not_exist.com/")
 *                      .client(buildOkHttpClient())
 *                      .addConverterFactory(GsonConverterFactory.create())
 *                      .addCallAdapterFactory(new AutoCancelableCallAdapterFactory()) // [1].
 *                      .build();
 *         // [1]. 向 Retrofit 注册 Call 工厂，以支持我们自己的 {@link AutoCancelableCall AutoCancelableCall}
 *     }
 * </code>
 *
 * 2. 将接口返回值从 {@link Call retrofit2.Call} 替换为 {@link AutoCancelableCall AutoCancelableCall}
 * <code>
 *     public interface AppNetworkService {
 *
 *          // 如果仍然使用 retrofit2.Call 作为返回值，这个请求走的是之前的老逻辑，不会有任何影响
 *
 *          @GET("/this_url_does_not_exist")
 *          Call<UserData> getUserData();
 *
 *          // 下面的返回值使用的是 AutoCancelableCall，支持自动取消
 *
 *          @GET("/this_url_does_not_exist")
 *          AutoCancelableCall<UserData> getUserDataWithAutoCancel();
 *     }
 * </code>
 *
 * 相比之前的 {@link Call#enqueue(retrofit2.Callback)}，我们拓展了回调类型。
 * 当传进来的 Callback 是 {@link AutoCancelableCall.Callback} 类型时，如果该请求被取消（不管是被自动取消的还是开发者手动取消的），
 * 其 {@link Callback#onCancel(AutoCancelableCallImpl)} 函数会被回调，而 {@link Callback#onFailure(Call, Throwable)} 会被忽略。
 * 如果传进来的仍然是普通的 {@link retrofit2.Callback}，则不会收到任何感知。
 * NOTE: 这个特征有点不稳定，我们可能会随时修改它。
 *
 */
interface AutoCancelableCall<T> : Call<T> {
    //    @NonNull
    //    public okhttp3.Call rawCall();
    interface CallbackForCancel<T> : retrofit2.Callback<T> {
        fun onCancel(call: AutoCancelableCallImpl<T>?) {}
    }

}