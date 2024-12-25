package com.zdy.flyline.activities.scanning.scanningFragment.UI

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zdy.flyline.R
import com.zdy.flyline.activities.scanning.fragments.connection.ConnectionFragment
import com.zdy.flyline.activities.scanning.interfaces.INavigationActivity
import com.zdy.flyline.activities.scanning.scanningFragment.UI.Recycle.ScanningDeviceAdapter
import com.zdy.flyline.activities.scanning.scanningFragment.ViewModel.ScanningViewModel
import com.zdy.flyline.databinding.FragmentScanningBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanningFragment : Fragment() {

    lateinit var binding: FragmentScanningBinding

    private val mViewModel: ScanningViewModel by viewModels()


    val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { entry ->
            val permission = entry.key
            val isGranted = entry.value
            if (isGranted) {
                // Разрешение предоставлено
                mViewModel.tryScanning(requireContext(), null)
            } else {
                // Разрешение отклонено
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanningBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.onCreate(requireContext())
        setupRecycle()
        addListeners()
        mViewModel.tryScanning(requireContext(),requestPermissionsLauncher)

    }



    private fun addListeners(){


        binding.startScanButton.setOnClickListener {
            mViewModel.tryScanning(requireContext(),requestPermissionsLauncher)
        }

        mViewModel.getDevices().observe(viewLifecycleOwner){
            devicesAdapter.addDevices(it)
        }

        mViewModel.getErrorMessage().observe(viewLifecycleOwner){
            if(it != null){
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = context?.getString(it as Int)
            } else{
                binding.errorMessage.visibility = View.GONE
            }
        }

        mViewModel.getIsScanning().observe(viewLifecycleOwner){
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.rcViewScanning.visibility = View.GONE
            } else{
                binding.progressBar.visibility = View.GONE
                binding.rcViewScanning.visibility = View.VISIBLE
            }
        }

    }

    val devicesAdapter : ScanningDeviceAdapter by lazy {
        ScanningDeviceAdapter()
    }

    private fun setupRecycle(){

        binding.rcViewScanning.let {rc->
            rc.layoutManager = LinearLayoutManager(context)
            rc.adapter = devicesAdapter
        }

        devicesAdapter.setOnItemClickListener {
            goConnect(it)
        }

    }



    private fun goConnect(device: BluetoothDevice){
        val bundle = bundleOf(ConnectionFragment.DEVICE_TO_CONNECT to device)
        (activity as INavigationActivity).getNavController().navigate(R.id.action_scanningFragment_to_connectionFragment, bundle)
    }





}