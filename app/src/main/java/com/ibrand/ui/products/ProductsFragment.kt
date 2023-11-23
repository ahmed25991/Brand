package com.ibrand.ui.products

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.FragmentCategoriesBinding
import com.ibrand.databinding.FragmentProductsBinding
import com.ibrand.models.response.CategoryModel
import com.ibrand.models.response.CountryModel
import com.ibrand.models.response.ProductModel
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
class ProductsFragment : BaseFragment<FragmentProductsBinding>(R.layout.fragment_products),ItemClickListener {

    private val viewModel by activityViewModels<HomeViewModel>()
    lateinit var catAdapter : ProductsCategoryAdapter
    lateinit var productAdapter : ProductAdapter
    var categoryModel : CategoryModel ?= null
    private var categoriesList : ArrayList<CategoryModel> = ArrayList()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        categoryModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { arguments?.getParcelable(Constants.BUNDLE_MODEL,CategoryModel::class.java) } else{ arguments?.getParcelable(Constants.BUNDLE_MODEL) }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        categoriesList.clear()
        categoriesList.add(CategoryModel(id=-1,name=getString(R.string.all_products)))
        categoriesList.addAll((categoryModel?.childrens?: emptyList()) as Collection<CategoryModel>)
        setupRecViews()
    }

    private fun setupRecViews() {
        catAdapter = ProductsCategoryAdapter(this@ProductsFragment)
        binding.recViewCategories.adapter = catAdapter
        catAdapter.differ.submitList(categoriesList)

        productAdapter = ProductAdapter(this@ProductsFragment)
        binding.recViewProducts.adapter = productAdapter

        observeProducts()
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            viewModel.displayProducts(categoryModel?.id?:-1)
            viewModel.productsResponse.liveState.onSuccessObserve {
                if (it.status) {
                    productAdapter.differ.submitList(it.data)
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
                categoryModel = item
                observeProducts()
            }
        }
    }


}