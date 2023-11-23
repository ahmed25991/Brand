package com.ibrand.ui.categories

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.FragmentCategoriesBinding
import com.ibrand.models.response.CategoryModel
import com.ibrand.models.response.CountryModel
import com.ibrand.ui.generalSettings.CountryAdapter
import com.ibrand.ui.home.HomeViewModel
import com.ibrand.utils.Constants
import com.ibrand.utils.Constants.CLICK_ON_MAIN_CATEGORY
import com.ibrand.utils.Constants.CLICK_ON_SUB_CATEGORY
import com.ibrand.utils.Constants.CLICK_ON_SUB_OF_SUB_CATEGORY
import com.ibrand.utils.NavigationUtil.navigateTo
import com.ibrand.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainCategoriesFragment : BaseFragment<FragmentCategoriesBinding>(R.layout.fragment_categories),ItemClickListener {

    private val viewModel by activityViewModels<HomeViewModel>()

    lateinit var mainCategoryAdapter : MainCategoryAdapter
    lateinit var subCategoryAdapter : SubCategoryAdapter


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

    }

    private fun setupRecViews() {
        mainCategoryAdapter = MainCategoryAdapter(this@MainCategoriesFragment)
        binding.recViewCategories.adapter = mainCategoryAdapter

        subCategoryAdapter = SubCategoryAdapter(this@MainCategoriesFragment)
        binding.recViewSubCategories.adapter = subCategoryAdapter

        observeCategories()
    }


    private fun observeCategories() {
        lifecycleScope.launch {
            viewModel.displayCategories()
            viewModel.categoriesResponse.liveState.onSuccessObserve {
                if (it.status) {
                    mainCategoryAdapter.differ.submitList(it.data)
                    Glide.with(requireContext()).load(it.data?.get(0)?.image).into(binding.ivCategoryImage)
                    subCategoryAdapter.differ.submitList(it.data?.get(0)?.childrens)
                } else {
                    showToast(requireContext(), it.message)
                }
            }
        }
    }

    override fun <T> onItemClick(item: T, type: Int?) {
        item as CategoryModel
        when (type) {
            CLICK_ON_MAIN_CATEGORY -> {
                Glide.with(requireContext()).load(item.image).into(binding.ivCategoryImage)
                subCategoryAdapter.differ.submitList(item.childrens)
                if (item.childrens.isNullOrEmpty()){
                    this.navigateTo(R.id.categories_to_products, bundleOf(Constants.BUNDLE_MODEL to item))
                }
            }
            CLICK_ON_SUB_CATEGORY -> {
                this.navigateTo(R.id.categories_to_products, bundleOf(Constants.BUNDLE_MODEL to item))
            }
            CLICK_ON_SUB_OF_SUB_CATEGORY -> {
                this.navigateTo(R.id.categories_to_products, bundleOf(Constants.BUNDLE_MODEL to item))
            }
        }
    }


}