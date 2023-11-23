package com.ibrand.ui.generalSettings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ibrand.R
import com.ibrand.base.baseAdapter.BaseRecAdapter
import com.ibrand.base.baseAdapter.ItemClickListener
import com.ibrand.databinding.ItemMCountryBinding
import com.ibrand.models.response.CountryModel


class CountryAdapter (itemClickListener: ItemClickListener) : BaseRecAdapter<CountryModel>(itemClickListener) {

    lateinit var context : Context
    var selectedPosition = -1

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        context = parent.context
        return  ItemMCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int, itemClickListener: ItemClickListener?) {
        val item = differ.currentList[position]
        binding as ItemMCountryBinding

        binding.apply {
            Glide.with(context).load(item.image.img32).into(icon)
            name.text = item.name
        }

        if (selectedPosition == position) { binding.container.background = getDrawable(context, R.drawable.custom_bg_selected_country) }
        else{ binding.container.background = getDrawable(context,R.drawable.custom_bg_btns_white_corners_dark) }

        binding.container.setOnClickListener {
            if (selectedPosition != position) {
                selectedPosition = position
                itemClickListener?.onItemClick(item)
                notifyDataSetChanged()
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CountryModel>() {
        override fun areItemsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean { return oldItem.id== newItem.id }
        override fun areContentsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean { return oldItem == newItem }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun getItemCount(): Int { return differ.currentList.size }


}
