package com.zdy.flyline.activities.settings.fragments.timer

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zdy.flyline.R
import com.zdy.flyline.activities.scanning.interfaces.INavigationActivity
import com.zdy.flyline.databinding.FragmentTimerBinding
import com.zdy.flyline.utils.extensions.vibratePhone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


@AndroidEntryPoint
class TimerFragment : Fragment() {



    private lateinit var binding : FragmentTimerBinding

    private val viewModel : TimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTimerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.getProgressBarValue().observe(viewLifecycleOwner){
            binding.buttonProgress.progress = it
        }
        binding.stopTouchFrame.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.actionDownStop()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    viewModel.actionUpStop()
                    binding.constraintLayoutStopButton.performClick()
                    true
                }
                else -> false
            }
        }



        addListeners()
        viewModel.reloadParameters()

    }



    private fun addListeners(){

        viewModel.version.observe(viewLifecycleOwner){
            (activity as AppCompatActivity).supportActionBar?.title ="${viewModel.getControllerName()} version: $it"
        }



        viewModel.currentFlyTime.observe(viewLifecycleOwner){
            if(it != null){
                binding.currentFlyTime.text = it
                binding.currentFlyTime.visibility = View.VISIBLE
            } else{
                binding.currentFlyTime.text = "0:00"
            }
        }

        viewModel.setVibroListener(requireContext()) {
            vibratePhone()
        }


        viewModel.flyTime.observe(viewLifecycleOwner){
            binding.textTimeFly.text = "${getString(R.string.fly_time)} - $it"
        }
        viewModel.sensorSAS.observe(viewLifecycleOwner){
            binding.textSensorSas.text =  "${getString(R.string.sas)} - $it"
        }
        viewModel.sensorSIY.observe(viewLifecycleOwner){
            binding.textSensorSiy.text = "${getString(R.string.siy)} - $it"
        }
        viewModel.mode.observe(viewLifecycleOwner){
            binding.textMod.text = "${getString(R.string.mode)} - MOD $it"
        }
        viewModel.rpmMin.observe(viewLifecycleOwner){
            binding.textMin.text = "${getString(R.string.min_rpm)} - $it"
        }
        viewModel.rpmMid.observe(viewLifecycleOwner){
            binding.textMid.text = "${getString(R.string.midRpm)} - $it"
            binding.currentRPM.text = it.toString()
        }
        viewModel.rpmMax.observe(viewLifecycleOwner){
            binding.textMax.text = "${getString(R.string.maxRpm)} - $it"
        }


        binding.downRpmButton.setOnClickListener {
            viewModel.minusRPM()
        }

        binding.upRpmButton.setOnClickListener {
            viewModel.plusRPM()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner){
            if(it == null){
                binding.warningLayout.visibility = View.INVISIBLE
            } else if(it != ""){
                binding.warningLayout.visibility = View.VISIBLE
                binding.warningMessage.text = context?.getString(it as Int)
            }
        }


        var clickWarningCount = 0
        binding.warningLayout.setOnClickListener {
            clickWarningCount++
            if(clickWarningCount == 5){
                binding.warningLayout.visibility = View.INVISIBLE
            }
            @Suppress("DEPRECATION")
            Handler().postDelayed({ clickWarningCount = 0 }, 2000)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadParameters()
    }

}