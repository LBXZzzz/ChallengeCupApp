package cn.mrra.android.common.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import cn.mrra.android.common.isDarkMode
import cn.mrra.android.common.preference.Preference
import rikka.material.app.MaterialActivity
import java.lang.reflect.ParameterizedType

/**
 * @see SimpleActivity
 * */
abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel> :
    MaterialActivity() {

    /**
     * 用来构造 [VM] 的构造工厂
     * */
    protected open val factory: () -> ViewModelProvider.Factory =
        { defaultViewModelProviderFactory }

    /**
     * 用来构造 [VM] 的构造参数
     * */
    protected open val creationExtras: () -> CreationExtras =
        { defaultViewModelCreationExtras }

    private var _binding: VB? = null
    private var _viewModel: VM? = null

    protected val binding: VB get() = _binding!!

    /**
     * 对应的 [VM] ，如果不需要，那么设为 [SimpleViewModel]
     * 即可，或者直接使用 [SimpleActivity]
     * */
    protected val viewModel: VM get() = _viewModel!!

    /**
     * 界面的 layout ID
     *
     * @LayoutRes
     */
    protected abstract val layoutId: Int

    /**
     * [binding] 和 [viewModel] 初始化完毕后回调此函数
     * */
    protected abstract fun onActivityCreated(savedInstanceState: Bundle?)

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Preference.appLocaleDelegate.onCreate(this)
        initViewDataBinding()
        initViewModel()
        onActivityCreated(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        Preference.appLocaleDelegate.updateConfiguration(resources.configuration)
    }

    override fun onResume() {
        super.onResume()
        if (Preference.appLocaleDelegate.isLocaleChanged) {
            recreate()
        }
    }

    @CallSuper
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.action ?: return
        when (action) {
            ACTION_RECREATE -> {
                recreate()
            }
            ACTION_FINISH -> {
                finish()
            }
        }
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
    protected var isStateBarTextBlack: (Boolean) -> Boolean = { !it }
        set(value) {
            field = value
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = value(isDarkMode)
        }

    final override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()
        val window = window
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewDataBinding() {
        _binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        var cls = javaClass.superclass
        var type = javaClass.genericSuperclass
        while (cls != BaseActivity::class.java) {
            type = cls.genericSuperclass
            cls = cls.superclass
        }
        val clz = (type as ParameterizedType).actualTypeArguments[1] as Class<BaseViewModel>
        if (clz != SimpleViewModel::class.java) {
            _viewModel = ViewModelProvider(
                viewModelStore, factory(), creationExtras()
            )[clz] as VM
            lifecycle.addObserver(viewModel)
        }
    }

}