package com.ibrand.ui.generalSettings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.FragmentGeneralSettingsBinding
import com.ibrand.models.response.CountryModel
import com.ibrand.ui.auth.AuthViewModel
import com.ibrand.utils.Constants.SELECTED_COUNTRY_KEY
import com.ibrand.utils.NavigationUtil.navigateTo
import com.ibrand.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GeneralSettingsFragment : BaseFragment<FragmentGeneralSettingsBinding>(R.layout.fragment_general_settings) , ItemClickListener {
    private val viewModel by activityViewModels<AuthViewModel>()
    lateinit var adapter : CountryAdapter
    private var selectedCountry : CountryModel ?= null
    private var  currentLang = "en"
    private var  currentGender = "male"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        setupRecViews()


        binding.tvEnglish.setOnClickListener {
            if (currentLang != "en"){
                binding.tvEnglish.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_white_corners_dark)
                binding.tvArabic.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_pink_solid_corners)
                currentLang = "ar"
            }
        }

        binding.tvArabic.setOnClickListener {
            if (currentLang != "ar"){
                binding.tvArabic.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_white_corners_dark)
                binding.tvEnglish.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_pink_solid_corners)
                currentLang = "en"
            }
        }


        binding.tvMale.setOnClickListener {
            if (currentGender != "male"){
                binding.tvMale.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_white_corners_dark)
                binding.tvFemale.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_pink_solid_corners)
                currentGender = "female"
            }
        }

        binding.tvFemale.setOnClickListener {
            if (currentGender != "female"){
                binding.tvFemale.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_white_corners_dark)
                binding.tvMale.background= getDrawable(requireContext(), R.drawable.custom_bg_btns_pink_solid_corners)
                currentGender = "male"
            }
        }


        binding.btnShoppingNow.setOnClickListener {
            if (selectedCountry == null){
                showToast(requireContext(),getString(R.string.choose_country))
            }else{
                prefs.save(selectedCountry,SELECTED_COUNTRY_KEY)
                this@GeneralSettingsFragment.navigateTo(R.id.general_settings_to_walk_through)
            }
        }

    }

    private fun setupRecViews() {
        adapter = CountryAdapter(this@GeneralSettingsFragment)
        binding.recViewCountries.adapter = adapter
        observeCountries()
    }


    private fun observeCountries() {
        lifecycleScope.launch {
            viewModel.displayCountries()
            viewModel.countriesResponse.liveState.onSuccessObserve {
                if (it.status) {
                    adapter.differ.submitList(it.data)
                } else {
                    showToast(requireContext(), it.message)
                }
            }
        }
    }

    override fun <T> onItemClick(item: T, type: Int?) {
        item as CountryModel
        selectedCountry = item
    }

}

