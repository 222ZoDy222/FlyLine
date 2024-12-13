package com.zdy.flyline.activities.settings.fragments.configuration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import com.zdy.flyline.activities.settings.recycler.items.ItemRecycle
import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.Parameter
import com.zdy.flyline.protocol.parameters.ParameterInt
import com.zdy.flyline.utils.settingsMenues.SettingsMenu_main
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val bluetoothSendingModel: BleSendingModel,
) : ViewModel() {


    var label: MutableLiveData<Any> = MutableLiveData(null)

    private val list : MutableLiveData<MutableList<ItemRecycle>> = MutableLiveData(mutableListOf())
    fun getList() = list

    private lateinit var menuParameters: MenuParameters

    private var toastListener: ((String) -> Unit)? = null
    fun setToastListener(listener: ((String) -> Unit)){
        toastListener = listener
    }


    fun initParameters(menu: Parameter? = null){

        menuParameters = (menu ?: SettingsMenu_main.getMenu()) as MenuParameters

        label.postValue(menuParameters.name)

        list.value = mutableListOf()
        for (param in menuParameters.parameters){
            when(param){
                is MenuParameters -> {
                    list.value!!.add(ItemRecycle.ParameterMenuItem(param))
                }
                is ParameterInt ->{
                    if(param.inputType == 1){
                        val item = ItemRecycle.ParameterIntItem(param)
                        list.value!!.add(item)
                        getParameterValue(item)
                    } else if(param.inputType == 2){
                        val item = ItemRecycle.ParameterTimeItem(param)
                        list.value!!.add(item)
                        getParameterValue(item)
                    }

                }
                else -> {}
            }
        }

        if(menu != null) list.value!!.add(ItemRecycle.ButtonItem())

    }

    private fun getParameterValue(item: ItemRecycle.ParameterIntItem){

        item.parameterInt.getValue(bluetoothSendingModel){
            updateItem?.invoke(item)
        }

    }


    fun setParameterValue(item: ItemRecycle.ParameterIntItem, value: Int){
        item.parameterInt.setValue(bluetoothSendingModel,value){
            updateItem?.invoke(item)
        }
    }

    fun saveParametersValue(){
        bluetoothSendingModel.save {
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                toastListener?.invoke("Saved")
            } else{
                toastListener?.invoke("Save Error")
            }
        }
    }

    private var updateItem : ((ItemRecycle.ParameterIntItem) -> Unit)? = null
    fun setOnUpdateItem(callback: (ItemRecycle.ParameterIntItem) -> Unit){
        updateItem = callback
    }
}