package com.zdy.flyline.activities.scanning.fragments.password

import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.zdy.flyline.R
import com.zdy.flyline.activities.scanning.interfaces.INavigationActivity
import com.zdy.flyline.activities.settings.SettingsActivity
import com.zdy.flyline.activities.settings.interfaces.ISettingsActivity
import com.zdy.flyline.databinding.FragmentPasswordBinding
import com.zdy.flyline.utils.connectionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordFragment : Fragment() {


    private val viewModel: PasswordViewModel by viewModels()

    private lateinit var binding : FragmentPasswordBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadingContainer.visibility = View.VISIBLE
        viewModel.tryDefaultPass {
            if(it){
                goToSettings()
            } else{
                binding.loadingContainer.visibility = View.GONE
            }
        }

        binding.enterPasswordButton.setOnClickListener{
            viewModel.applyPass()
        }

        binding.appCompatEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setPass(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}

        })




        viewModel.getError().observe(viewLifecycleOwner){
            if(it == null || it == ""){
                binding.errorMessage.visibility = View.GONE
            } else{
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = context?.getString(it as Int)
            }
        }

        viewModel.getIsAuthenticated().observe(viewLifecycleOwner){isAuth ->
            if(isAuth){
                goToSettings()
            } else{
                // TODO: Wrong password
            }
        }

    }


    private var registrationLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
            (activity as INavigationActivity).getNavController().navigate(R.id.action_passwordFragment_to_scanningFragment)
    }
    fun goToSettings(){

        val intent = Intent(context, SettingsActivity::class.java)
        registrationLauncher.launch(intent)
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