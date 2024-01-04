package com.lxn.util.glide

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.lxn.util.R
import com.lxn.util.base.BaseActivity
import com.lxn.util.databinding.ActivityGlideBinding
import com.lxn.util.util.operationutil.Utils

/**
 * Glide 学习的
 * @author：李晓楠
 * 时间：2024/1/2 14:39
 */
class GlideActivity: BaseActivity() {
    //进入当前页面
    companion object{
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
//        Glide.with(this).load(R.drawable.adapter_cart_imgadd).into(vb.ivLocale)
//        Glide.with(this).load(picPath).into(vb.ivWrap)
        Glide.with(this).load(R.drawable.adapter_cart_imgadd).override(Int.Companion.MIN_VALUE,Int.Companion.MIN_VALUE).into(vb.ivLocale)
        Glide.with(this).load(picPath).override(Int.Companion.MIN_VALUE,Int.Companion.MIN_VALUE).into(vb.ivWrap)
        Glide.with(this).load(picPath).into(vb.iv50dp)
        Glide.with(this).load(picPath).into(vb.iv100dp)
        Glide.with(this).load(picPath).into(vb.iv150dp)


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

            Log.i("lxnGlide", "ivLocal=${vb.ivLocale.width}==${vb.ivLocale.height}=${bitmap0.toString()}===${bitmap0.intrinsicWidth}====${bitmap0.intrinsicHeight}")
            Log.i("lxnGlide", "ivWrap=${vb.ivWrap.width}==${vb.ivWrap.height}=${bitmap1.toString()}===${bitmap1.intrinsicWidth}====${bitmap1.intrinsicHeight}")
            Log.i("lxnGlide", "iv50dp==${vb.iv50dp.width}==${vb.iv50dp.height}=${bitmap2.toString()}===${bitmap2.intrinsicWidth}====${bitmap2.intrinsicHeight}")
            Log.i("lxnGlide", "iv100dp===${vb.iv100dp.width}=${vb.iv100dp.height}=${bitmap3.toString()}===${bitmap3.intrinsicWidth}====${bitmap3.intrinsicHeight}")
            Log.i("lxnGlide", "iv150dp==${vb.iv150dp.width}==${vb.iv150dp.height}=${bitmap4.toString()}===${bitmap4.intrinsicWidth}====${bitmap4.intrinsicHeight}")
        }
    }
}