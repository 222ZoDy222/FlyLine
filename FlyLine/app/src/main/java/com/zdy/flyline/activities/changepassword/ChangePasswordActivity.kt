package com.zdy.flyline.activities.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.zdy.flyline.R
import com.zdy.flyline.databinding.ActivityChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    private val viewModel : ChangePasswordViewModel by viewModels()

    lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBinding()
    }

    private fun setupBinding(){
        binding.okButton.setOnClickListener {
            viewModel.sendPassword(
                binding.oldPassword.text.toString(),
                binding.newPassword.text.toString()
            ){
                if(it) finish()
            }
        }
        viewModel.getErrorMessage().observe(this){
            if(it != null){
                binding.errorMessage.text = applicationContext.getString(it as Int)
                binding.errorMessage.visibility = View.VISIBLE
            } else{
                binding.errorMessage.visibility = View.GONE
            }
        }

        viewModel.getShouldCheckOldPassword().observe(this){
            if(it){
                binding.oldPassword.visibility = View.VISIBLE
            } else{
                binding.oldPassword.visibility = View.GONE
            }
        }
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