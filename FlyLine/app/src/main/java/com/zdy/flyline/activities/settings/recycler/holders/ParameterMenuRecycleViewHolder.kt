package com.zdy.flyline.activities.settings.recycler.holders

import android.content.Intent
import com.zdy.flyline.activities.calibration.CalibrationActivity
import com.zdy.flyline.activities.changepassword.ChangePasswordActivity
import com.zdy.flyline.activities.settings.recycler.items.ItemRecycle
import com.zdy.flyline.databinding.ItemMenuSettingsLayoutBinding

class ParameterMenuRecycleViewHolder(private val binding: ItemMenuSettingsLayoutBinding) : ItemsRecycleViewHolder(binding) {

    private lateinit var currentParameter : ItemRecycle.ParameterMenuItem


    private var onItemClickListener: ((ItemRecycle.ParameterMenuItem)->Unit)? = null
    fun setOnItemClickListener(callback: ((ItemRecycle.ParameterMenuItem) -> Unit)?){
        onItemClickListener = callback
    }

    private var clickCounter = 0
    fun bind(parameter: ItemRecycle.ParameterMenuItem){
        currentParameter = parameter

        itemView.apply {
            binding.parameterName.text = context.getString(currentParameter.menuParameters.name)
            setOnClickListener {
                when(currentParameter.menuParameters.activityToStart){
                    0 -> onItemClickListener?.invoke(currentParameter)
                    1 -> {
                        val intent = Intent(context,ChangePasswordActivity::class.java)
                        context.startActivity(intent)
                    }
                    2 ->{
                        val intent = Intent(context, CalibrationActivity::class.java)
                        context.startActivity(intent)
                    }
                    5 ->{
                        clickCounter++
                        if(clickCounter >= 5){
                            clickCounter = 0
                            onItemClickListener?.invoke(currentParameter)
                        }
                    }
                }


            }

        }

    }





}