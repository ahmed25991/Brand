package com.ibrand.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ibrand.base.baseAdapter.BaseRecAdapter
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.ItemMainOfferBinding
import com.ibrand.models.response.OfferModel

class HomeMainOfferAdapter (itemClickListener: ItemClickListener) : BaseRecAdapter<OfferModel>(itemClickListener) {
    lateinit var context : Context
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        context = parent.context
        return  ItemMainOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int, itemClickListener: ItemClickListener?) {
        val item = differ.currentList[position]
        binding as ItemMainOfferBinding
        Glide.with(context).load(item.imageSmall).into(binding.ivImage)
    }

    private val differCallback = object : DiffUtil.ItemCallback<OfferModel>() {
        override fun areItemsTheSame(oldItem: OfferModel, newItem: OfferModel): Boolean { return oldItem.id== newItem.id }
        override fun areContentsTheSame(oldItem: OfferModel, newItem: OfferModel): Boolean { return oldItem == newItem }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun getItemCount(): Int { return differ.currentList.size }
}