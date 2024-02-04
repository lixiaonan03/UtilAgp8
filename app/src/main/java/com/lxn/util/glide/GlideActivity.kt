package com.lxn.util.glide

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lxn.util.R
import com.lxn.util.base.BaseActivity
import com.lxn.util.databinding.ActivityGlideBinding
import com.lxn.util.util.operationutil.Utils

/**
 * Glide 学习的   https://jun2sn6jvo.feishu.cn/docx/IrWeducfqoXC1gxS2XEcQGVqndg
 * @author：李晓楠
 * 时间：2024/1/2 14:39
 */
class GlideActivity : BaseActivity() {
    //进入当前页面
    companion object {
        fun startActivity(context: Activity) {
            val intent = Intent(context, GlideActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var vb: ActivityGlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityGlideBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "glide 学习测试"

//        val picPath = "https://pic.wanwustore.cn/Fjt_IddpcNV6aNAyxCfRRBFHU_-c?imageView2/2/w/300"
        val picPath = "https://pic.bbtkids.cn/Fgd5aUM9Lgi1l5vexctd37Tajsrv"  //图片的宽高 是 148*198

        //如果需要固定位图片尺寸则需要用到 override(Int.Companion.MIN_VALUE,Int.Companion.MIN_VALUE) 配置
        Glide.with(this).load(R.drawable.adapter_cart_imgadd).into(vb.ivLocale)
        Glide.with(this).load(picPath).into(vb.ivWrap)
        Glide.with(this).load(R.drawable.adapter_cart_imgadd)
            .override(Int.Companion.MIN_VALUE, Int.Companion.MIN_VALUE).into(vb.ivLocale)
        Glide.with(this).load(picPath).override(Int.Companion.MIN_VALUE, Int.Companion.MIN_VALUE)
            .into(vb.ivWrap)
        Glide.with(this).load(picPath).into(vb.iv50dp)
        Glide.with(this).load(picPath).into(vb.iv100dp)
        Glide.with(this).load(picPath).into(vb.iv150dp)
        Glide.with(this).apply {
            load(picPath)
//            .override(Int.Companion.MIN_VALUE,Int.Companion.MIN_VALUE)
                .error(R.drawable.adapter_cart_imgadd)
                .into(vb.iv100dp)
        }

        //多参数控制
        Glide.with(this).load(picPath)
            .placeholder(R.drawable.adapter_cart_imgadd)//指定图片未成功加载前显示的图片
            .error(R.drawable.adapter_cart_imgadd)//指定图片加载失败显示的图片
            .fitCenter() //指定图片缩放类型
            .centerCrop() //指定图片缩放类型
            .skipMemoryCache(true) //是否跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存任何内容
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //缓存转换过后的图片
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //根据图片资源智能地选择使用哪一种缓存策略（默认选项）
            .diskCacheStrategy(DiskCacheStrategy.ALL) //既缓存原始图片，也缓存转换过后的图片
            .priority(Priority.HIGH)  //指定优先级
            .override(Int.Companion.MIN_VALUE, Int.Companion.MIN_VALUE) //指定图片的尺寸
            .into(vb.iv150dp)

        //通过RequestOptions 来设置
        val requestOptions = RequestOptions().apply {
            placeholder(R.drawable.logo)
            fitCenter()
            centerCrop()
        }
        //通过RequestOptions 来设置  如果设置了那么加载的图片大小就和占位的的大小一样,因为设置占位图的时候 ImageView的大小已经确定了  (设置了error 是没有用的)
        Glide.with(this).load("https://pic.wanwustore.cn/Fv69wXMOvWZas9DawxmTsxpoi9Yx").addListener(object : com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                Log.i("lxnGlide", "onResourceReady==${resource.intrinsicWidth}==${resource.intrinsicHeight}==isFirstResource=${isFirstResource}")
                return false
            }

        }).apply(requestOptions).into(vb.ivNetWorkWarp)
        //如果不设置占位图的话，那么加载的图片大小就是屏幕宽高中最大的一边作为边的正方形去加载的
//        Glide.with(this).load("https://pic.wanwustore.cn/Fv69wXMOvWZas9DawxmTsxpoi9Yx").into(vb.ivNetWorkWarp)

        //如果加载的资源为null 则给载体加载的资源顺序为：fallBack 设置的 -》 error 设置的 -》placeholder 设置的
        Glide.with(this).load(picPath)
            .placeholder(R.drawable.adapter_cart_imgadd)  //指定图片未成功加载前显示的图片
            .fallback(R.drawable.adapter_cart_imgadd)    //指定图片加载失败回调的b
            .error(R.drawable.adapter_cart_imgadd)       //指定图片加载失败显示的图片
            .into(vb.ivWrap)


        vb.tvGet.setOnClickListener {
            val bitmap0 = vb.ivLocale.drawable
            val bitmap1 = vb.ivWrap.drawable
            val bitmap2 = vb.iv50dp.drawable
            val bitmap3 = vb.iv100dp.drawable
            val bitmap4 = vb.iv150dp.drawable


//            ivWrap=1080==2115=android.graphics.drawable.BitmapDrawable@1b3837a===1581====2115
//            iv50dp==138==138=android.graphics.drawable.BitmapDrawable@5da572b===103====138
//            iv100dp===275=275=android.graphics.drawable.BitmapDrawable@ac86788===206====275
//            iv150dp==413==413=android.graphics.drawable.BitmapDrawable@20fb521===309====413
//
            //自己的理解
//             小米8 打印出来的日志如上：因为小米8的屏幕密度 是2.75 获取的屏幕高度是2115  所以获取的资源是2115
            // 具体的获取2115这个值 是在 {@link ViewTarget }  中的这个方法 getMaxDisplayLength

            Log.i(
                "lxnGlide",
                "ivLocal=${vb.ivLocale.width}==${vb.ivLocale.height}=${bitmap0.toString()}===${bitmap0.intrinsicWidth}====${bitmap0.intrinsicHeight}"
            )
            Log.i(
                "lxnGlide",
                "ivWrap=${vb.ivWrap.width}==${vb.ivWrap.height}=${bitmap1.toString()}===${bitmap1.intrinsicWidth}====${bitmap1.intrinsicHeight}"
            )
            Log.i(
                "lxnGlide",
                "iv50dp==${vb.iv50dp.width}==${vb.iv50dp.height}=${bitmap2.toString()}===${bitmap2.intrinsicWidth}====${bitmap2.intrinsicHeight}"
            )
            Log.i(
                "lxnGlide",
                "iv100dp===${vb.iv100dp.width}=${vb.iv100dp.height}=${bitmap3.toString()}===${bitmap3.intrinsicWidth}====${bitmap3.intrinsicHeight}"
            )
            Log.i(
                "lxnGlide",
                "iv150dp==${vb.iv150dp.width}==${vb.iv150dp.height}=${bitmap4.toString()}===${bitmap4.intrinsicWidth}====${bitmap4.intrinsicHeight}"
            )
        }
    }
}