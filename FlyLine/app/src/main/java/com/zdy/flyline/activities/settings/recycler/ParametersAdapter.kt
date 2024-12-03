package com.zdy.flyline.activities.settings.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zdy.flyline.R
import com.zdy.flyline.activities.settings.recycler.holders.ItemsRecycleViewHolder
import com.zdy.flyline.activities.settings.recycler.holders.ParameterButtonRecycleViewHolder
import com.zdy.flyline.activities.settings.recycler.holders.ParameterIntRecycleViewHolder
import com.zdy.flyline.activities.settings.recycler.holders.ParameterMenuRecycleViewHolder
import com.zdy.flyline.activities.settings.recycler.holders.ParameterTimeRecycleViewHolder
import com.zdy.flyline.activities.settings.recycler.items.ItemRecycle
import com.zdy.flyline.databinding.ItemButtonSettingsLayoutBinding
import com.zdy.flyline.databinding.ItemMenuSettingsLayoutBinding
import com.zdy.flyline.databinding.ItemParameterSettingsLayoutBinding
import com.zdy.flyline.databinding.ItemParameterTimeSettingsLayoutBinding
import java.lang.IllegalArgumentException

class ParametersAdapter() : RecyclerView.Adapter<ItemsRecycleViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<ItemRecycle>(){
        override fun areItemsTheSame(oldItem: ItemRecycle, newItem: ItemRecycle): Boolean {
            return if(oldItem is ItemRecycle.ParameterIntItem
                && newItem is ItemRecycle.ParameterIntItem
            ){
                (oldItem).parameterInt.command == (newItem).parameterInt.command
            } else false

        }
        override fun areContentsTheSame(oldItem: ItemRecycle, newItem: ItemRecycle): Boolean{
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsRecycleViewHolder {
        return when(viewType){
            R.layout.item_parameter_settings_layout ->{
                ParameterIntRecycleViewHolder(
                    ItemParameterSettingsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            R.layout.item_parameter_time_settings_layout ->{
                ParameterTimeRecycleViewHolder(
                    ItemParameterTimeSettingsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            R.layout.item_menu_settings_layout ->{
                ParameterMenuRecycleViewHolder(
                    ItemMenuSettingsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            R.layout.item_button_settings_layout ->{
                ParameterButtonRecycleViewHolder(
                    ItemButtonSettingsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }



            else -> throw throw  IllegalArgumentException("Invalid view type provider")
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ItemsRecycleViewHolder, position: Int) {

        val item = differ.currentList[position]

        when(holder){

            is ParameterIntRecycleViewHolder -> {
                holder.bind(item as ItemRecycle.ParameterIntItem)
                holder.setOnValueChangedListener(onParameterValueChanged)
            }

            is ParameterTimeRecycleViewHolder ->{
                holder.bind(item as ItemRecycle.ParameterTimeItem)
                holder.setOnValueChangedListener(onParameterValueChanged)
            }

            is ParameterMenuRecycleViewHolder -> {
                holder.setOnItemClickListener(onMenuClickListener)
                holder.bind(item as ItemRecycle.ParameterMenuItem)
            }

            is ParameterButtonRecycleViewHolder -> {
                holder.bind(onSaveButtonListener)
            }

        }

    }


    override fun getItemViewType(position: Int): Int {
        return when(differ.currentList[position]){
            is ItemRecycle.ParameterTimeItem -> R.layout.item_parameter_time_settings_layout
            is ItemRecycle.ParameterIntItem -> R.layout.item_parameter_settings_layout
            is ItemRecycle.ParameterMenuItem -> R.layout.item_menu_settings_layout
            is ItemRecycle.ButtonItem -> R.layout.item_button_settings_layout

        }
    }

    private var onMenuClickListener : ((ItemRecycle.ParameterMenuItem) -> Unit)? = null
    fun setOnMenuClickListener( callback : ((ItemRecycle.ParameterMenuItem) -> Unit)){
        onMenuClickListener = callback
    }

    private var onParameterValueChanged : ((Pair<ItemRecycle.ParameterIntItem,Int>) -> Unit)? = null
    fun setOnParameterValueChanged(callback: (Pair<ItemRecycle.ParameterIntItem,Int>) -> Unit){
        onParameterValueChanged = callback
    }

    private var onSaveButtonListener : (()->Unit)? = null
    fun setOnSaveButtonListener(callback: (()->Unit)){
        onSaveButtonListener = callback
    }

    fun updateItem(item: ItemRecycle.ParameterIntItem){
        val index = differ.currentList.indexOf(item)
        notifyItemChanged(index)
    }

}