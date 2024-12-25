package com.zdy.flyline.activities.scanning.scanningFragment.UI.Recycle

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zdy.flyline.R
import com.zdy.flyline.databinding.ItemScanningBluetoothDeviceBinding

class ScanningDeviceAdapter() : RecyclerView.Adapter<ScanningDeviceAdapter.ScanningRCHolder>() {

    private val deviceList = mutableListOf<BluetoothDevice>()

    inner class ScanningRCHolder(private val item: View) : RecyclerView.ViewHolder(item){

        val binding = ItemScanningBluetoothDeviceBinding.bind(itemView)

        fun bind(dev: BluetoothDevice) = with(binding){
            macValue.text = dev.address
            try{
                @SuppressLint("MissingPermission")
                nameValue.text = dev.name ?: item.context.getString(R.string.unknown_device)
            } catch (ex: Exception){
                nameValue.text = item.context.getString(R.string.unknown_device)
            }
            itemView.setOnClickListener {
                onItemClickConnect?.invoke(dev)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanningRCHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scanning_bluetooth_device,parent,false)
        return ScanningRCHolder(view)
    }

    override fun getItemCount(): Int = deviceList.size

    override fun onBindViewHolder(holder: ScanningRCHolder, position: Int) {
        val device = deviceList[position]
        holder.bind(device)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDevice(device: BluetoothDevice){
        if(!deviceList.contains(device)){
            deviceList.add(device)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDevices(devices: MutableList<BluetoothDevice>){
        deviceList.clear()
        deviceList.addAll(devices)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearDevices(){
        deviceList.clear()
        notifyDataSetChanged()
    }


    private var onItemClickConnect : ((BluetoothDevice) -> Unit)? = null

    fun setOnItemClickListener(listener : (BluetoothDevice) -> Unit){
        onItemClickConnect = listener
    }

}