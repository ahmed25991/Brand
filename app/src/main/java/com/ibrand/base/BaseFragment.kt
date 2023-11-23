package com.ibrand.base


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialFadeThrough
import com.ibrand.sharedPref.UserPreferenceHelper
import com.ibrand.utils.AppUtils
import com.ibrand.utils.Constants
import com.ibrand.utils.KeyboardUtil.hideKeyboard
import com.ibrand.utils.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class BaseFragment<T : ViewDataBinding> constructor(@LayoutRes private val contentLayoutId: Int) : Fragment() {
    @Inject
    lateinit var prefs: UserPreferenceHelper
    protected var bindingComponent: DataBindingComponent? = DataBindingUtil.getDefaultComponent()
    private lateinit var _binding: T

    protected val binding: T get() = _binding

    protected inline fun binding(block: T.() -> Unit): T { return binding.apply(block) }

    var currentLanguage = "en"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false, bindingComponent)
        _binding.executePendingBindings()
        _binding.lifecycleOwner = viewLifecycleOwner
        currentLanguage = prefs.retrieve<String>(Constants.LANGUAGE_KEY)!!
        return _binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTransitions(view)
    }

    @CallSuper
    protected open fun setUpTransitions(view: View) {
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        if (view is ViewGroup) view.isTransitionGroup = true
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus ?: return
        view.hideKeyboard()
    }

    private fun onError(throwable: String) {
        if(AppUtils.hasInternetConnection(requireContext())) {
            showToast(requireContext(), throwable)
        }
    }


    var job: Job? = null

    protected fun <T : Any> LiveData<BaseResponse<T>>.onSuccessObserve(onSuccess: (T) -> Unit) {
        job?.cancel()
        job = lifecycleScope.launch {
            observe(viewLifecycleOwner) { response ->
                when (response) {
                    is BaseResponse.Loading -> Unit
                    is BaseResponse.Success -> onSuccess(response.data)
                    is BaseResponse.Error -> onError(response.throwable)
                    is BaseResponse.None -> Unit
                    else -> Unit
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        job?.cancel()
    }
}

