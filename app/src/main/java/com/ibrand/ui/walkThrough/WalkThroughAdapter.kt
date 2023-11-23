package com.ibrand.ui.walkThrough

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ibrand.databinding.ItemWalkThroughBinding
import com.ibrand.base.baseAdapter.BaseRecAdapter
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.models.WalkThroughResponse
import com.ibrand.utils.setImageUrl

class WalkThroughAdapter (itemClickListener: ItemClickListener) : BaseRecAdapter<WalkThroughResponse>(itemClickListener) {

    lateinit var context : Context

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        context = parent.context
        return  ItemWalkThroughBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    }

    override fun bind(binding: ViewDataBinding, position: Int, itemClickListener: ItemClickListener?) {
        val item = differ.currentList[position]
        binding as ItemWalkThroughBinding

        Glide.with(context).load(item.image).into(binding.ivImage)
        binding.tvSliderTitle.text = item.title
        binding.tvSliderDesc.text = item.description

    }



    private val differCallback = object : DiffUtil.ItemCallback<WalkThroughResponse>() {
        override fun areItemsTheSame(oldItem: WalkThroughResponse, newItem: WalkThroughResponse): Boolean {
            return oldItem.id== newItem.id
        }

        override fun areContentsTheSame(oldItem: WalkThroughResponse, newItem: WalkThroughResponse): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun getItemCount(): Int { return differ.currentList.size }


}