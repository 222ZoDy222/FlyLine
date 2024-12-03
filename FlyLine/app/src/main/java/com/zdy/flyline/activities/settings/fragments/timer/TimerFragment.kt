package com.zdy.flyline.activities.settings.fragments.timer

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.zdy.flyline.R
import com.zdy.flyline.databinding.FragmentTimerBinding
import dagger.hilt.android.AndroidEntryPoint

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



        addListeners()
        viewModel.reloadParameters()

    }

    private fun addListeners(){

        viewModel.version.observe(viewLifecycleOwner){
            (activity as AppCompatActivity).supportActionBar?.title ="Fly-controller version: $it"
        }

        viewModel.flyTime.observe(viewLifecycleOwner){
            binding.textTimeFly.text = it.toString()
        }

        viewModel.sensorSAS.observe(viewLifecycleOwner){
            binding.textSensorSas.text =  "${getString(R.string.sas)} - $it"
        }
        viewModel.sensorSIY.observe(viewLifecycleOwner){
            binding.textSensorSiy.text = "${getString(R.string.siy)} - $it"
        }
        viewModel.mode.observe(viewLifecycleOwner){
            binding.textMod.text = "${getString(R.string.mode)} - $it"
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

        binding.stopEngineButton.setOnClickListener {
            viewModel.engineStop()
        }

        binding.downRpmButton.setOnClickListener {
            viewModel.minusRPM()
        }

        binding.upRpmButton.setOnClickListener {
            viewModel.plusRPM()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner){
            if(it == null){
                binding.warningLayout.visibility = View.GONE
            } else if(it != ""){
                binding.warningLayout.visibility = View.VISIBLE
                binding.warningMessage.text = it
            }
        }

        var clickWarningCount = 0
        binding.warningLayout.setOnClickListener {
            clickWarningCount++
            if(clickWarningCount == 5){
                binding.warningLayout.visibility = View.GONE
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