package com.ibrand.ui.auth.countries

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ibrand.R
import com.ibrand.base.BaseBottomSheetFragment
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.BottomSheetCountriesBinding
import com.ibrand.models.response.CountryModel
import com.ibrand.ui.auth.AuthViewModel
import com.ibrand.ui.generalSettings.CountryAdapter
import com.ibrand.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CountriesBottomSheetFragment(val clickListener: CountryCLickListener) :
    BaseBottomSheetFragment<BottomSheetCountriesBinding>(R.layout.bottom_sheet_countries), ItemClickListener {

    private lateinit var adapter: CountryAdapter
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPatientsRecView()
    }


    private fun setupPatientsRecView() {
        adapter = CountryAdapter(this@CountriesBottomSheetFragment)
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
        clickListener.sendCallback(item)
        dismiss()
    }


}