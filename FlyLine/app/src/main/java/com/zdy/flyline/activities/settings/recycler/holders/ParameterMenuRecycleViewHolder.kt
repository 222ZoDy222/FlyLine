package com.zdy.flyline.activities.settings.recycler.holders

import android.content.Intent
import com.zdy.flyline.activities.changepassword.ChangePasswordActivity
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
                if(currentParameter.menuParameters.isPassword){
                    val intent = Intent(context,ChangePasswordActivity::class.java)
                    context.startActivity(intent)
                } else{
                    onItemClickListener?.invoke(currentParameter)
                }

            }

        }

    }





}