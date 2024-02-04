package com.lxn.util.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView

/**
 * 自定义的ImageView 控件
 * @author：李晓楠
 * 时间：2024/2/2 10:50
 */
class MyImageView(context: Context, attrs: AttributeSet?): AppCompatImageView(context,attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.i("lxnGlide", "自定义ImageView=的宽度=${MeasureSpec.getMode(widthMeasureSpec)}==${MeasureSpec.getSize(widthMeasureSpec)}=高度==${MeasureSpec.getMode(heightMeasureSpec)}==${MeasureSpec.getSize(heightMeasureSpec)}}")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.i("lxnGlide", "自定义ImageView=onLayout==changed=${changed}=left=${left}=top==${top}=right=${right}}")
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        Log.i("lxnGlide", "自定义ImageView=onDraw==")
        super.onDraw(canvas)
    }
}