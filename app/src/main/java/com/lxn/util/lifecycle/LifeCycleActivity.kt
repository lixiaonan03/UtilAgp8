package com.lxn.util.lifecycle

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
import com.lxn.util.databinding.ActivityLifecycleBinding
import com.lxn.util.util.operationutil.Utils

/**
 * lifeCycle 生命周期的学习的
 * https://zhuanlan.zhihu.com/p/484383470
 */
class LifeCycleActivity : BaseActivity() {
    //进入当前页面
    companion object {
        fun startActivity(context: Activity) {
            val intent = Intent(context, LifeCycleActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var vb: ActivityLifecycleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityLifecycleBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "lifeCycle 学习"
        //添加观察着
        lifecycle.addObserver(SampleLifecycle())

    }
}