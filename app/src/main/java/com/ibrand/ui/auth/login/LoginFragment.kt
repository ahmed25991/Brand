package com.ibrand.ui.auth.login

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.databinding.FragmentLoginBinding
import com.ibrand.models.response.CountryModel
import com.ibrand.ui.MainActivity
import com.ibrand.ui.auth.AuthViewModel
import com.ibrand.ui.auth.countries.CountriesBottomSheetFragment
import com.ibrand.ui.auth.countries.CountryCLickListener
import com.ibrand.utils.Constants.SELECTED_COUNTRY_KEY
import com.ibrand.utils.Constants.USER_PHONE_NUMBER
import com.ibrand.utils.KeyboardUtil.hideKeyboard
import com.ibrand.utils.NavigationUtil.navigateTo
import com.ibrand.utils.isPhoneValid
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login),
    CountryCLickListener {
    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })


        binding.txtInputCountry.setText("+${prefs.retrieve<CountryModel>(SELECTED_COUNTRY_KEY)?.phoneCode}")


        binding.txtInputCountry.setOnClickListener {
            CountriesBottomSheetFragment(this@LoginFragment).show(parentFragmentManager, "")
        }

        Glide.with(requireContext())
            .asDrawable().load(prefs.retrieve<CountryModel>(SELECTED_COUNTRY_KEY)?.image?.img128)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.txtInputCountry.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
        })

        binding.btnSkip.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }


        binding.btnStartApp.setOnClickListener {
            it.hideKeyboard()
            validateLogin()
        }
    }


    private fun liveValidateInputs() {
        binding.txtInputPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!isPhoneValid(charSequence.toString())) { binding.txtInputPhone.error = getString(R.string.label_not_allowed_phone) } else { binding.txtInputPhone.error = null }
            }
        })
    }


    private fun validateLogin() {
        liveValidateInputs()
        if (!isPhoneValid(binding.txtInputPhone.text.toString())) {
            binding.txtInputPhone.error = getString(R.string.label_not_allowed_phone)
            return
        }
        resetInputViews()
    }


    private fun resetInputViews() {
        binding.txtInputPhone.error = null
        verifyAccount()
    }




    private fun verifyAccount() {
        this.navigateTo(R.id.login_to_verifyPhone, bundleOf(USER_PHONE_NUMBER to binding.txtInputCountry.text.toString()+binding.txtInputPhone.text.toString()))
    }

    override fun sendCallback(model: CountryModel) {
        prefs.save(model,SELECTED_COUNTRY_KEY)
        binding.txtInputCountry.setText("+${model?.phoneCode}")
        Glide.with(requireContext())
            .asDrawable().load(model.image.img128)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.txtInputCountry.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

}

