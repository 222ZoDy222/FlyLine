package com.zdy.flyline.activities.settings.recycler.holders


import com.zdy.flyline.databinding.ItemButtonSettingsLayoutBinding

class ParameterButtonRecycleViewHolder(private val binding: ItemButtonSettingsLayoutBinding) : ItemsRecycleViewHolder(binding) {

    private var onButtonClickListener : (()->Unit)? = null
    fun bind(callback: (()->Unit)?){
        onButtonClickListener = callback

        binding.saveButton.setOnClickListener{
            onButtonClickListener?.invoke()
        }
    }


}