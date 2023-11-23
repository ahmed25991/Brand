package com.ibrand.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.FragmentHomeBinding
import com.ibrand.models.response.CategoryModel
import com.ibrand.models.response.OfferModel
import com.ibrand.utils.Constants
import com.ibrand.utils.NavigationUtil.navigateTo
import com.ibrand.utils.setCurrentIndicator
import com.ibrand.utils.setupFunctionIndicator
import com.ibrand.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) ,ItemClickListener{
    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var mainCategoryAdapter : HomeCategoryAdapter
    private lateinit var mainOfferAdapter: HomeMainOfferAdapter

    private lateinit var promotions1Adapter: PromotionOfferAdapter
    private lateinit var promotions2Adapter: PromotionOfferAdapter
    private lateinit var promotions3Adapter: PromotionOfferAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        initializeViewPager()
        setupRecViews()
    }

    private fun setupRecViews() {
        mainCategoryAdapter = HomeCategoryAdapter(this@HomeFragment)
        binding.recViewCategories.adapter = mainCategoryAdapter
        observeCategories()
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            viewModel.displayCategories()
            viewModel.categoriesResponse.liveState.onSuccessObserve {
                if (it.status) {
                    mainCategoryAdapter.differ.submitList(it.data)
                } else {
                    showToast(requireContext(), it.message)
                }
            }
        }
    }

    private fun initializeViewPager() {
        mainOfferAdapter = HomeMainOfferAdapter(this@HomeFragment)
        binding.viewPager.adapter = mainOfferAdapter

        promotions1Adapter = PromotionOfferAdapter(this@HomeFragment)
        binding.recViewPromotions1.adapter = promotions1Adapter

        promotions2Adapter = PromotionOfferAdapter(this@HomeFragment)
        binding.recViewPromotions2.adapter = promotions2Adapter

        promotions3Adapter = PromotionOfferAdapter(this@HomeFragment)
        binding.recViewPromotions3.adapter = promotions3Adapter

        observeMainOffers()
        observeMainPromotions()
    }

    private fun observeMainOffers() {
        lifecycleScope.launch {
            viewModel.displaySliderOffers()
            viewModel.sliderOffersResponse.liveState.onSuccessObserve {
                if (it.status) {
                    mainOfferAdapter.differ.submitList(it.data)
                    setUpViewPager()
                } else {
                    showToast(requireContext(), it.message)
                }
            }
        }
    }


    private fun observeMainPromotions() {
        lifecycleScope.launch {
            viewModel.displayPromotions()
            viewModel.sliderOffersResponse.liveState.onSuccessObserve { response ->
                if (response.status) {
                    val promotions = response.data
                    arrangePromotionsAdapters(promotions)
                } else {
                    showToast(requireContext(), response.message)
                }
            }
        }
    }

    private fun arrangePromotionsAdapters(promotions: List<OfferModel>?) {
        if ((promotions?.size?:0) >= 6) {
            promotions1Adapter.differ.submitList(promotions?.subList(0, 6))
            if ((promotions?.size ?: 0) >= 10) {
                promotions2Adapter.differ.submitList(promotions?.subList(6, 10))
                promotions3Adapter.differ.submitList(promotions?.subList(10, promotions.size))
            } else {
                promotions2Adapter.differ.submitList(promotions?.subList(6, promotions.size))
            }
        } else {
            promotions1Adapter.differ.submitList(promotions)
        }
    }



    private fun setUpViewPager() {
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicators.setupFunctionIndicator(mainOfferAdapter.differ.currentList.size, R.drawable.ic_slider_inactive_main_offer)
        val currentPageIndex = 0
        binding.viewPager.currentItem = currentPageIndex
        binding.indicators.setCurrentIndicator(0,R.drawable.ic_sldier_active_main_offer, R.drawable.ic_slider_inactive_main_offer)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    changeIndicator(position)
                }
            }
        )
    }

    private fun changeIndicator(position: Int) {
        binding.indicators.setCurrentIndicator(position,R.drawable.ic_sldier_active_main_offer, R.drawable.ic_slider_inactive_main_offer)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})
    }

    override fun <T> onItemClick(item: T, type: Int?) {
        item as CategoryModel
        when (type) {
            Constants.CLICK_ON_MAIN_CATEGORY -> {
                this.navigateTo(R.id.home_to_products, bundleOf(Constants.BUNDLE_MODEL to item))

            }
        }
    }


}