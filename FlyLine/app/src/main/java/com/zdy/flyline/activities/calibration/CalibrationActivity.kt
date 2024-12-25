package com.zdy.flyline.activities.calibration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.zdy.flyline.R
import com.zdy.flyline.databinding.ActivityCalibrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalibrationActivity : AppCompatActivity() {


    private val viewModel: CalibrationViewModel by viewModels()

    lateinit var binding: ActivityCalibrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalibrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        viewModel.getProgressBarValue().observe(this){
            binding.buttonProgress.progress = it
        }

        viewModel.setOnCalibrationComplete {
            if(it){
                Toast.makeText(applicationContext,
                    getString(R.string.CalibrationStarted),Toast.LENGTH_LONG).show()
                finish()
            } else{
                Toast.makeText(applicationContext, getString(R.string.Error),Toast.LENGTH_SHORT).show()
            }
        }

        binding.stopTouchFrame.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.actionDownStop()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    viewModel.actionUpStop()
                    binding.constraintLayoutcCalibrationButton.performClick()
                    true
                }
                else -> false
            }
        }

        viewModel.getIsLoading().observe(this){
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.constraintLayoutcCalibrationButton.visibility = View.GONE
            } else{
                binding.progressBar.visibility = View.GONE
                binding.constraintLayoutcCalibrationButton.visibility = View.VISIBLE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume(applicationContext)
    }
}