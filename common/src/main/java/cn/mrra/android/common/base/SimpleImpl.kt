package cn.mrra.android.common.base

import androidx.databinding.ViewDataBinding

/**
 * 如果不需要 [BaseViewModel] ，那么可以使用 [SimpleActivity]
 * */
abstract class SimpleActivity<VB : ViewDataBinding> : BaseActivity<VB, SimpleViewModel>()

/**
 * 如果不需要 [BaseViewModel] ，那么可以使用 [SimpleFragment]
 * */
abstract class SimpleFragment<VB : ViewDataBinding> : BaseFragment<VB, SimpleViewModel>()

/**
 * 如果不需要 [BaseViewModel] ，那么可以使用 [SimpleViewModel]
 * ，使用 [SimpleViewModel] 将不会初始化 [BaseViewModel]
 * */
class SimpleViewModel : BaseViewModel()