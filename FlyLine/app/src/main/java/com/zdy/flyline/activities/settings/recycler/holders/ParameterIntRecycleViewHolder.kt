package com.zdy.flyline.activities.settings.recycler.holders

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.zdy.flyline.R
import com.zdy.flyline.activities.settings.recycler.items.ItemRecycle
import com.zdy.flyline.databinding.DialogIntInputBinding
import com.zdy.flyline.databinding.DialogTimeInputBinding
import com.zdy.flyline.databinding.ItemParameterSettingsLayoutBinding


// Holder for PARAMETER of FlyController
class ParameterIntRecycleViewHolder(private val binding: ItemParameterSettingsLayoutBinding) : ItemsRecycleViewHolder(binding){

    private lateinit var currentParameter : ItemRecycle.ParameterIntItem

    private var onValueChangedListener: ((Pair<ItemRecycle.ParameterIntItem,Int>)->Unit)? = null
    fun setOnValueChangedListener(callback: ((Pair<ItemRecycle.ParameterIntItem,Int>)->Unit)?){
        onValueChangedListener = callback
    }

    fun bind(parameter: ItemRecycle.ParameterIntItem){

        currentParameter = parameter

        itemView.apply {
            binding.parameterName.text = currentParameter.parameterInt.name


            if(currentParameter.parameterInt.currentValue == null) {
                binding.textField.text = "-"
            } else{
                binding.textField.text = currentParameter.parameterInt.currentValue.toString()
            }

            // Выделяем текст при нажатии на поле
            setOnClickListener {
                showDialog()

            }
        }


    }

    private fun showDialog(){
        if(currentParameter.parameterInt.currentValue == null) return
        val dialogBinding = DialogIntInputBinding.inflate(LayoutInflater.from(itemView.context))

        dialogBinding.editText.hint = "${currentParameter.parameterInt.min}-${currentParameter.parameterInt.max}"
        dialogBinding.editText.setText(currentParameter.parameterInt.currentValue.toString())
        // Создаем диалог
        val dialog = AlertDialog.Builder(itemView.context, R.style.CustomAlertDialogTheme)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.titleViewInt.text = currentParameter.parameterInt.name


        dialogBinding.okButton.setOnClickListener {

            if(dialogBinding.editText.text.toString().isNotEmpty()){
                var value = dialogBinding.editText.text
                sendValue(value.toString().toInt())

            }

            dialog.dismiss()

        }


        dialog.show()

        dialogBinding.editText.focus()
        dialogBinding.editText.post {
            dialogBinding.editText.selectAll()
        }


    }

    private fun sendValue(newValue: Int) {
        try {
            var value: Int = newValue
            if(value == currentParameter.parameterInt.currentValue) return
            // Проверка на допустимые пределы
            if (value > currentParameter.parameterInt.max) value = currentParameter.parameterInt.max
            if (value < currentParameter.parameterInt.min) value = currentParameter.parameterInt.min

            onValueChangedListener?.invoke(Pair(currentParameter, value))
        } catch (ex: NumberFormatException) {
            // Обработка ошибки, если ввод не является числом
            binding.textField.text = currentParameter.parameterInt.currentValue.toString()
        }
    }



}

fun EditText.focus() {
    text?.let { setSelection(it.length) }
    postDelayed({
        requestFocus()
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 200)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}