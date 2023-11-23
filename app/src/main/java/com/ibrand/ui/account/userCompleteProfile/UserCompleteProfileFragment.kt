package com.ibrand.ui.account.userCompleteProfile

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.databinding.FragmentUserCompleteProfileBinding
import com.ibrand.models.response.CountryModel
import com.ibrand.ui.MainActivity
import com.ibrand.ui.auth.AuthViewModel
import com.ibrand.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserCompleteProfileFragment : BaseFragment<FragmentUserCompleteProfileBinding>(R.layout.fragment_user_complete_profile) {
    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        val backStackEntryCount = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_main_nav_host)?.childFragmentManager?.backStackEntryCount ?: 0

        binding.btnBack.setOnClickListener {
            if (backStackEntryCount == 1) {
                requireActivity().finish()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backStackEntryCount == 1) {
                    requireActivity().finish()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    findNavController().popBackStack()
                }
            }
        })



        binding.txtInputCountry.setText("+${prefs.retrieve<CountryModel>(Constants.SELECTED_COUNTRY_KEY)?.phoneCode}")
        Glide.with(requireContext())
            .asDrawable().load(prefs.retrieve<CountryModel>(Constants.SELECTED_COUNTRY_KEY)?.image?.img128)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.txtInputCountry.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })



    }
}
