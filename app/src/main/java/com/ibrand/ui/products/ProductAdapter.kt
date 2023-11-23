package com.ibrand.ui.products

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ibrand.base.baseAdapter.BaseRecAdapter
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.ItemProductBinding
import com.ibrand.models.response.ProductModel
import com.ibrand.utils.Constants.CLICK_ON_PRODUCT


class ProductAdapter (itemClickListener: ItemClickListener) : BaseRecAdapter<ProductModel>(itemClickListener) {

    lateinit var context : Context

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        context = parent.context
        return  ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int, itemClickListener: ItemClickListener?) {
        val item = differ.currentList[position]
        binding as ItemProductBinding

        binding.apply {
            if (item.images?.isNotEmpty() == true){
                Glide.with(context).load(item.images?.get(0)?.original).into(ivProduct)
            }
            tvProductName.text = item.title
            tvOldPrice.text = (item.price?.exclTax?: 0.0).toString() + item.price?.currency
            tvNewPrice.text = (item.price?.inclTax?: 0.0).toString() + item.price?.currency
        }

        binding.clContainer.setOnClickListener {
            itemClickListener?.onItemClick(item,CLICK_ON_PRODUCT)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean { return oldItem.id== newItem.id }
        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean { return oldItem == newItem }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun getItemCount(): Int { return differ.currentList.size }


}
