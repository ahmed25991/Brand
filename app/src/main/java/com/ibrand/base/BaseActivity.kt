package com.ibrand.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.ibrand.sharedPref.UserPreferenceHelper
import com.ibrand.utils.AppUtils
import com.ibrand.utils.Constants
import com.ibrand.utils.LangUtil.setLanguage
import com.ibrand.utils.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding> constructor(@LayoutRes private val contentLayoutId: Int) : AppCompatActivity() {
    @Inject
    lateinit var prefs: UserPreferenceHelper
    private lateinit var _binding: T
    val binding: T get() = _binding
    var job: Job? = null

    inline fun binding(block: T.() -> Unit): T { return binding.apply(block) }


    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.onCreate(savedInstanceState)
        if (prefs.retrieve<String>(Constants.LANGUAGE_KEY) == null){
            if (prefs.retrieve<String>(Constants.LANGUAGE_KEY)== "en"){
                prefs.save("en", Constants.LANGUAGE_KEY)
            }else{
                prefs.save("en", Constants.LANGUAGE_KEY)
            }
        }

        setLanguage("en")

        _binding = DataBindingUtil.setContentView<T>(this@BaseActivity, contentLayoutId)
        _binding.executePendingBindings()
        _binding.lifecycleOwner = this
    }


    private val networkChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(AppUtils.hasInternetConnection(context!!)){
                statusOfInternetChangedToConnected("yes")
            }else{
                statusOfInternetChangedToConnected("no")
            }
        }
    }



    abstract fun statusOfInternetChangedToConnected (status:String)


    override fun onResume() {
        super.onResume()
        setLanguage("en")
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
        unregisterReceiver(networkChangeReceiver)
    }


    private fun onError(throwable: String) {
        if(AppUtils.hasInternetConnection(this@BaseActivity)) {
            showToast(this@BaseActivity, throwable)
        }
    }


    private fun <T : Any> Flow<BaseResponse<T>>.onSuccessCollect(onSuccess: (T) -> Unit) {
        job?.cancel()
        job = lifecycleScope.launchWhenStarted {
            this@onSuccessCollect.cancellable().collectLatest { response ->
                when (response) {
                    is BaseResponse.Loading -> Unit
                    is BaseResponse.Success -> onSuccess(response.data)
                    is BaseResponse.Error -> onError(response.throwable)
                    is BaseResponse.None -> Unit
                }
            }
        }
    }
}
