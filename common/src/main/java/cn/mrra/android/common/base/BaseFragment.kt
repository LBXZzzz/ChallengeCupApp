package cn.mrra.android.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import java.lang.reflect.ParameterizedType

/**
 * @see SimpleFragment
 * */
abstract class BaseFragment<VB : ViewDataBinding, VM : BaseViewModel> :
    Fragment() {

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
    protected abstract fun onFragmentCreated(savedInstanceState: Bundle?)

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        onFragmentCreated(savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        var cls = javaClass.superclass
        var type = javaClass.genericSuperclass
        while (cls != BaseFragment::class.java) {
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