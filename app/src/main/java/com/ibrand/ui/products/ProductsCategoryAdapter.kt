package com.ibrand.ui.products

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.ibrand.R
import com.ibrand.base.baseAdapter.BaseRecAdapter
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.ItemCatOfProductBinding
import com.ibrand.models.response.CategoryModel
import com.ibrand.utils.Constants.CLICK_ON_MAIN_CATEGORY


class ProductsCategoryAdapter (itemClickListener: ItemClickListener) : BaseRecAdapter<CategoryModel>(itemClickListener) {

    lateinit var context : Context
    var selectedPosition = 0

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        context = parent.context
        return  ItemCatOfProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int, itemClickListener: ItemClickListener?) {
        val item = differ.currentList[position]
        binding as ItemCatOfProductBinding

        binding.apply { tvCategoryName.text = item.name }

        if (selectedPosition == position) {
            binding.tvCategoryName.setTextColor(context.getColor(R.color.pink))
            binding.lineSelectedIndicator.visibility = VISIBLE
        } else{
            binding.tvCategoryName.setTextColor(context.getColor(R.color.black))
            binding.lineSelectedIndicator.visibility = GONE
        }

        binding.tvCategoryName.setOnClickListener {
            if (selectedPosition != position) {
                selectedPosition = position
                itemClickListener?.onItemClick(item,CLICK_ON_MAIN_CATEGORY)
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


}
