package com.ibrand.ui.walkThrough

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.FragmentWalkThroughBinding
import com.ibrand.models.WalkThroughResponse
import com.ibrand.utils.Constants
import com.ibrand.utils.NavigationUtil.navigateTo
import com.ibrand.utils.setCurrentIndicator
import com.ibrand.utils.setupFunctionIndicator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WalkThroughFragment : BaseFragment<FragmentWalkThroughBinding>(R.layout.fragment_walk_through), ItemClickListener {

    private lateinit var adapter: WalkThroughAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs.save(false, Constants.IS_FIRST_TIME_KEY)

        observeWalkThrough()

        binding.btnSkip.setOnClickListener { this.navigateTo(R.id.walkthrough_to_login_fragment) }
        binding.btnStartApp.setOnClickListener { this.navigateTo(R.id.walkthrough_to_login_fragment) }


        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            val nextItem = if (currentItem < adapter.itemCount - 1) { currentItem + 1 } else { currentItem }
            binding.viewPager.currentItem = nextItem
            changeIndicator(nextItem)
        }
    }



    private var walkThroughList : ArrayList<WalkThroughResponse> = ArrayList()
    private fun observeWalkThrough() {

        walkThroughList.clear()
        walkThroughList.add(WalkThroughResponse(id= 1 , image = R.drawable.walkthrough_dummy1, title = "أطلبي منتجك الأن بكل سهولة متعة التسوق معنا", description = "لوريم إيبسوم هو نص مؤقت يستخدم في التصميم والنشر لإظهار شكل الوثيقة أو الخط دون الاعتماد على محتوى معنوي. قد يستخدم لوريم إيبسوم كنص بديل قبل وضع النص النهائي "))
        walkThroughList.add(WalkThroughResponse(id= 2 , image = R.drawable.walkthrough_dummy_2, title = "أطلبي منتجك الأن بكل سهولة متعة التسوق معنا", description = "لوريم إيبسوم هو نص مؤقت يستخدم في التصميم والنشر لإظهار شكل الوثيقة أو الخط دون الاعتماد على محتوى معنوي. قد يستخدم لوريم إيبسوم كنص بديل قبل وضع النص النهائي "))
        walkThroughList.add(WalkThroughResponse(id= 3 , image = R.drawable.walkthrough_dummy3, title = "أطلبي منتجك الأن بكل سهولة متعة التسوق معنا", description = "لوريم إيبسوم هو نص مؤقت يستخدم في التصميم والنشر لإظهار شكل الوثيقة أو الخط دون الاعتماد على محتوى معنوي. قد يستخدم لوريم إيبسوم كنص بديل قبل وضع النص النهائي "))


        adapter = WalkThroughAdapter(this@WalkThroughFragment)
        binding.viewPager.adapter = adapter
        adapter.differ.submitList(walkThroughList)
        setUpViewPager()
    }



    private fun setUpViewPager() {
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicators.setupFunctionIndicator(adapter.differ.currentList.size)

        val currentPageIndex = 0
        binding.viewPager.currentItem = currentPageIndex
        binding.indicators.setCurrentIndicator(0)
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    changeIndicator(position)
                }
            }
        )
    }



    private fun changeIndicator(position: Int) {
        binding.indicators.setCurrentIndicator(position)
        if (position < adapter.itemCount - 1) {
           binding.btnSkip.visibility = VISIBLE
           binding.btnNext.visibility = VISIBLE
           binding.btnStartApp.visibility = GONE
        } else {
            binding.btnSkip.visibility = GONE
            binding.btnNext.visibility = GONE
            binding.btnStartApp.visibility = VISIBLE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {}
        )
    }


    override fun <T> onItemClick(item: T, type: Int?) {}





}

