package com.ibrand.ui.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ibrand.base.baseAdapter.BaseRecAdapter
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.ItemSubOfSubCategoryBinding
import com.ibrand.models.response.CategoryModel
import com.ibrand.utils.Constants.CLICK_ON_SUB_OF_SUB_CATEGORY


class SubOfSubCategoryAdapter (itemClickListener: ItemClickListener) : BaseRecAdapter<CategoryModel>(itemClickListener) {

    lateinit var context : Context
    var selectedPosition = -1

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        context = parent.context
        return  ItemSubOfSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int, itemClickListener: ItemClickListener?) {
        val item = differ.currentList[position]
        binding as ItemSubOfSubCategoryBinding

        binding.apply {
            Glide.with(context).load(item.image).into(ivCategoryImage)
            tvCategoryName.text = item.name
        }


        binding.clContainer.setOnClickListener {
            itemClickListener?.onItemClick(item,CLICK_ON_SUB_OF_SUB_CATEGORY)
            notifyDataSetChanged()
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean { return oldItem.id== newItem.id }
        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean { return oldItem == newItem }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun getItemCount(): Int { return differ.currentList.size }


}
