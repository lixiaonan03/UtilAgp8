package com.lxn.util.base

import android.graphics.Color
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding

/**
 * @author：李晓楠
 * 时间：2023/12/29 16:00
 */
open class BaseActivity : AppCompatActivity() {

    /**
     * 设置沉浸式状态栏
     */
    fun setTransBar() {
        //设置状态栏的字体颜色为黑色（Light Mode），适应深色背景。
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //禁用系统窗口装饰适应系统窗口区域，这意味着应用将负责绘制在状态栏和导航栏区域。
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //设置状态栏透明
        window.statusBarColor = Color.TRANSPARENT
        //如果 Android 版本是 Android 9（Pie）或更高，则设置导航栏的分隔线颜色为白色。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = Color.WHITE
        }
        //设置应用窗口布局的 WindowInsetsListener，以便在窗口插入变化时更新布局。
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                bottom = insets.bottom
            )
            windowInsets
        }
        //使用 WindowInsetsControllerCompat 设置状态栏为浅色模式，以确保状态栏的图标和文字是深色的。
        WindowInsetsControllerCompat(this.window, this.window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars = true
        }
    }

}