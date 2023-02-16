package cn.mrra.android.common.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel

/**
 * @see SimpleViewModel
 * @see SimpleActivity
 * @see SimpleFragment
 * */
abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver