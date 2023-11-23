package com.ibrand.ui.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.ibrand.base.baseAdapter.BaseRecAdapter
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.ItemSubCategoryBinding
import com.ibrand.models.response.CategoryModel
import com.ibrand.utils.Constants
import com.ibrand.utils.Constants.CLICK_ON_SUB_CATEGORY


class SubCategoryAdapter (itemClickListener: ItemClickListener) :
    BaseRecAdapter<CategoryModel>(itemClickListener) ,ItemClickListener{

    lateinit var context : Context
    lateinit var adapter : SubOfSubCategoryAdapter


    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        context = parent.context
        return  ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int, itemClickListener: ItemClickListener?) {
        val item = differ.currentList[position]
        binding as ItemSubCategoryBinding

        binding.apply {
            tvCategoryName.text = item.name
        }

        adapter = SubOfSubCategoryAdapter(this@SubCategoryAdapter)
        binding.recViewSubOfSubCategories.adapter = adapter
        adapter.differ.submitList(item.childrens)

        binding.ivArrow.visibility = if ((item.getNumChildren ?: 0) > 0){ VISIBLE }else{ GONE }
        binding.tvViewAll.visibility = if ((item.getNumChildren ?: 0) > 0){ VISIBLE }else{ GONE }


        binding.clContainer.setOnClickListener {
            if ((item.getNumChildren ?: 0) > 0){
                if (binding.recViewSubOfSubCategories.isVisible){
                    binding.recViewSubOfSubCategories.visibility = GONE
                }else{
                    binding.recViewSubOfSubCategories.visibility = VISIBLE
                }
            }else{
                itemClickListener?.onItemClick(item,CLICK_ON_SUB_CATEGORY)
                notifyDataSetChanged()
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean { return oldItem.id== newItem.id }
        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean { return oldItem == newItem }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun getItemCount(): Int { return differ.currentList.size }

    override fun <T> onItemClick(item: T, type: Int?) {
        item as CategoryModel
        itemClickListener?.onItemClick(item, Constants.CLICK_ON_SUB_OF_SUB_CATEGORY)
    }


}
