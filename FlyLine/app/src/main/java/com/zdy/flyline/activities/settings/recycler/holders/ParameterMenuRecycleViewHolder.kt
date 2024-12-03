package com.zdy.flyline.activities.settings.recycler.holders

import com.zdy.flyline.activities.settings.recycler.items.ItemRecycle
import com.zdy.flyline.databinding.ItemMenuSettingsLayoutBinding

class ParameterMenuRecycleViewHolder(private val binding: ItemMenuSettingsLayoutBinding) : ItemsRecycleViewHolder(binding) {

    private lateinit var currentParameter : ItemRecycle.ParameterMenuItem


    private var onItemClickListener: ((ItemRecycle.ParameterMenuItem)->Unit)? = null
    fun setOnItemClickListener(callback: ((ItemRecycle.ParameterMenuItem) -> Unit)?){
        onItemClickListener = callback
    }

    fun bind(parameter: ItemRecycle.ParameterMenuItem){
        currentParameter = parameter

        itemView.apply {

            binding.parameterName.text = currentParameter.menuParameters.name
            setOnClickListener {
                onItemClickListener?.invoke(currentParameter)
            }

        }

    }



}