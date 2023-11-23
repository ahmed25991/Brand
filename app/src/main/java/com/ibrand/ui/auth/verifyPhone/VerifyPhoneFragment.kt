package com.ibrand.ui.auth.verifyPhone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.databinding.FragmentVerifyPhoneBinding
import com.ibrand.models.response.CountryModel
import com.ibrand.ui.MainActivity
import com.ibrand.ui.auth.AuthViewModel
import com.ibrand.utils.Constants
import com.ibrand.utils.Constants.USER_MODEL
import com.ibrand.utils.Constants.USER_TOKEN
import com.ibrand.utils.PausableDispatcher
import com.ibrand.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>(R.layout.fragment_verify_phone) {
    private val viewModel by activityViewModels<AuthViewModel>()
    private val dispatcher = PausableDispatcher(Handler(Looper.getMainLooper()))
    var phoneNumber = ""
   // lateinit var auth :FirebaseAuth
    var storedVerificationId = ""
    var verifyjob: Job? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // auth = FirebaseAuth.getInstance()
        phoneNumber = arguments?.getString(Constants.USER_PHONE_NUMBER)!!
        binding.viewModel = viewModel
        prepareCounter()
        liveValidateInput()

        binding.btnBack.setOnClickListener { findNavController().popBackStack()}
        binding.btnResend.setOnClickListener { prepareCounter() }
    }




    private fun liveValidateInput() {
        binding.otpView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 5) {
                    binding.otpView.setItemBackground(getDrawable(requireContext(),R.drawable.filled_item_bg))
                    validateVerify()
                }
            }
        })
    }



    private fun prepareCounter() {
      //  receiveOTP()
        verifyjob?.cancel()
        verifyjob =  lifecycleScope.launch {
            binding.btnResend.isClickable = false
            binding.btnResend.isEnabled = false
            requireActivity().getColor(R.color.greyLevel6)
                .let { binding.btnResend.setTextColor(it) }
            binding.otpView.setText("")

           val totalSeconds = TimeUnit.MINUTES.toSeconds(2)
            val tickSeconds = 0
            for (second in totalSeconds downTo tickSeconds) {
                binding.tvDidnotReceiveCode.apply {
                    text = getString(R.string.verification_time, String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(second), second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))))
                }

                delay(1000)
                binding.btnResend.isClickable = second < 1
                binding.btnResend.isEnabled = second < 1
                if (second < 1) {
                    requireActivity().getColor(R.color.darkgreen)
                        .let { binding.btnResend.setTextColor(it) }
                } else {
                    requireActivity().getColor(R.color.greyLevel6)
                        .let { binding.btnResend.setTextColor(it) }

                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        dispatcher.pause()
        verifyjob?.cancel()
    }

    override fun onResume() {
        super.onResume()
        dispatcher.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        dispatcher.cancel()
    }


    private fun validateVerify() {
        if (binding.otpView.text.toString().length < 6) {
            showToast(requireContext(), getString(R.string.label_not_allowed_otp))
            return
        }
      //  sendOTP()

        observeLogin("user/login/")
    }


//    private fun receiveOTP(){
//        auth.setLanguageCode("en")
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber("+${prefs.retrieve<CountryModel>(Constants.SELECTED_COUNTRY_KEY)?.phoneCode}${phoneNumber}")
//            .setTimeout(120L, TimeUnit.SECONDS)
//            .setActivity(requireActivity())
//            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
//                override fun onVerificationFailed(e: FirebaseException) { prepareCounter() }
//                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//                    storedVerificationId = verificationId
//                }
//            })
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }

//    private fun sendOTP(){
//        if (storedVerificationId.isNotEmpty()){
//            val credential = PhoneAuthProvider.getCredential(storedVerificationId, binding.otpView.text.toString())
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener(requireActivity()) { task ->
//                    if (task.isSuccessful) {
//                        observeLogin("user/login/")
//                    } else {
//                        showToast(requireContext(),getString(R.string.verification_failed))
//                    }
//                }
//        }
//    }


    private fun observeLogin(url:String) {
        lifecycleScope.launch {
            viewModel.login(phoneNumber =phoneNumber,password="IBRAND",url=url)
            viewModel.loginResponse.liveState.onSuccessObserve {
                    if (it.status) {
                        prefs.save(it.data?.token, USER_TOKEN)
                        prefs.save(it.data,USER_MODEL)
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        if (url.contains("register",true)){
                            intent.putExtra("GOTO_EDIT_PROFILE_PAGE",true)
                        }
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        showToast(requireContext(), it.message)
                        observeLogin("user/register/")
                    }
                }
        }
    }
}



