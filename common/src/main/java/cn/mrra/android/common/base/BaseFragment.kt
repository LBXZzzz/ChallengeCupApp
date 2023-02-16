package cn.mrra.android.common.base

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    protected abstract var _binding: B?
    protected val binding: B get() = _binding!!

    protected open val isFitStatusBar: Boolean = true

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}