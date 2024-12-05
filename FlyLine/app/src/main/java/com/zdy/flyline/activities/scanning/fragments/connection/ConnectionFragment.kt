package com.zdy.flyline.activities.scanning.fragments.connection

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zdy.flyline.utils.connectionState.*
import com.zdy.flyline.R
import com.zdy.flyline.activities.scanning.interfaces.INavigationActivity
import com.zdy.flyline.databinding.FragmentConnectionBinding
import com.zdy.flyline.protocol.parameters.Parameter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConnectionFragment : Fragment() {


    private lateinit var binding: FragmentConnectionBinding

    private val viewModel: ConnectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentConnectionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val deviceToConnect = arguments?.getParcelable<BluetoothDevice>(DEVICE_TO_CONNECT)
        if(deviceToConnect == null) {

            return
        } else{
            viewModel.connect(deviceToConnect,requireContext())
        }
        viewModel.isConnected().observe(viewLifecycleOwner){ isConnected->
            when(isConnected){
                connected -> {
                    binding.connectionStateTextView.text = "Connected to device"
                }

                disconnected -> {

                }

                error -> {
                    //TODO: Error to connect device
                    binding.connectionStateTextView.text = "error to connect"
                    lifecycleScope.launch {
                        delay(2000)
                        activity?.finish()
                    }

                }

                services_discovered ->{
                    binding.connectionStateTextView.text = "services discovered"
                    (activity as INavigationActivity).getNavController().navigate(R.id.action_connectionFragment_to_passwordFragment)
                }

                mtu_changed -> {

                }

                else ->{}
            }
        }




    }

    companion object{
        public const val DEVICE_TO_CONNECT = "DEVICE_TO_CONNECT_TAG"
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume(requireContext())
    }

}

