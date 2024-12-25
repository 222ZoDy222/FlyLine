package com.zdy.flyline.activities.settings.recycler.holders

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import com.zdy.flyline.R
import com.zdy.flyline.activities.settings.recycler.items.ItemRecycle
import com.zdy.flyline.databinding.DialogTimeInputBinding
import com.zdy.flyline.databinding.ItemParameterTimeSettingsLayoutBinding

class ParameterTimeRecycleViewHolder(private val binding : ItemParameterTimeSettingsLayoutBinding) : ItemsRecycleViewHolder(binding) {

    private lateinit var currentParameter : ItemRecycle.ParameterTimeItem

    private var onValueChangedListener: ((Pair<ItemRecycle.ParameterTimeItem,Int>)->Unit)? = null
    fun setOnValueChangedListener(callback: ((Pair<ItemRecycle.ParameterTimeItem,Int>)->Unit)?){
        onValueChangedListener = callback
    }


    fun bind(parameter : ItemRecycle.ParameterTimeItem){

        currentParameter = parameter

        itemView.apply {

            binding.apply {
                parameterName.text = context.getString(currentParameter.parameterInt.name)

                setOnClickListener {
                    showTimePickerDialog()
                }
                if(currentParameter.parameterInt.currentValue == null){
                    inputField.text = "-"

                } else{

                    updateValue(currentParameter.parameterInt.currentValue!!)
                }


            }


        }

    }

    private fun showTimePickerDialog() {

        if(currentParameter.parameterInt.currentValue == null) return


        val dialogBinding = DialogTimeInputBinding.inflate(LayoutInflater.from(itemView.context))

        // Получаем текущее время
        val currentTime = currentParameter.parameterInt.currentValue
        val minute = currentTime?.div(60)
        val second = currentTime?.rem(60)

        dialogBinding.minutePicker.apply {
            minValue = 0
            maxValue = currentParameter.parameterInt.max / 60
            if (minute != null) {
                value = minute
            }
        }

        dialogBinding.secondPicker.apply {
            minValue = 0
            if(currentParameter.parameterInt.max < 60){
                maxValue = currentParameter.parameterInt.max
            } else{
                maxValue = 59
            }

            if (second != null) {
                value = second
            }
        }


        // Создаем диалог
        val dialog = AlertDialog.Builder(itemView.context, R.style.CustomAlertDialogTheme)
            .setView(dialogBinding.root)
            .create()



        dialogBinding.titleViewTime.text = itemView.context.getString(currentParameter.parameterInt.name)

        dialogBinding.okButton.setOnClickListener {
            val newMinute = dialogBinding.minutePicker.value
            val newSecond = dialogBinding.secondPicker.value
            var value = newMinute * 60 + newSecond
            sendValue(value)
            dialog.dismiss()

        }


        dialog.show()
    }

    private fun updateValue(value: Int){
        val minute = value.div(60)
        val second = value.rem(60)
        binding.inputField.text = "${minute}:${second}"

    }

    private fun sendValue(newValue: Int) {

        var value = newValue
        if(value == currentParameter.parameterInt.currentValue) return
        // Проверка на допустимые пределы
        if (value > currentParameter.parameterInt.max) value = currentParameter.parameterInt.max
        if (value < currentParameter.parameterInt.min) value = currentParameter.parameterInt.min
        updateValue(value)
        onValueChangedListener?.invoke(Pair(currentParameter, value))

    }
}





