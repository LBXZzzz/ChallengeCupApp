package cn.mrra.android.common.base

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.view.WindowCompat
import cn.mrra.android.common.isDarkMode
import rikka.material.app.MaterialActivity

abstract class BaseActivity : MaterialActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置状态栏字体颜色
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = isStateBarTextBlack(isDarkMode)
    }

    /**
     * 设置状态栏字体颜色。
     * </p>
     *
     * 传入布尔值代表应用是否处于深色模式，true 代表处于深色模式。
     * </p>
     *
     * 返回要设置的状态栏字体颜色，true 代表黑色，false 代表白色。
     * </p>
     *
     * 默认模式为：非深色模式显示为黑色字体，深色模式显示为白色字体
     * */
    protected open val isStateBarTextBlack: (Boolean) -> Boolean = { !it }

    override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()
        val window = window
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }

}