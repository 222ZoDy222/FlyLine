package com.zdy.flyline.activities.settings.fragments.configuration

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zdy.flyline.R
import com.zdy.flyline.activities.scanning.interfaces.INavigationActivity
import com.zdy.flyline.activities.settings.recycler.ParametersAdapter
import com.zdy.flyline.databinding.FragmentConfigurationBinding
import com.zdy.flyline.protocol.parameters.Parameter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment : Fragment() {


    private val viewModel: ConfigurationViewModel by viewModels()

    private val adapter = ParametersAdapter()


    private lateinit var binding: FragmentConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paramToShow = arguments?.getParcelable<Parameter>(PARAMETER_TAG)
        viewModel.initParameters(paramToShow)

        viewModel.label.observe(viewLifecycleOwner){
            if(it != null)
                (activity as AppCompatActivity).supportActionBar?.title = context?.getString(it as Int)
        }

        setupRecycle()

        viewModel.setOnUpdateItem {
            adapter.updateItem(it)
        }

        viewModel.setToastListener{
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupRecycle(){

        binding.rcViewAdapter.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        adapter.setOnMenuClickListener {
            val bundle = bundleOf(PARAMETER_TAG to it.menuParameters)
            (activity as INavigationActivity).getNavController().navigate(R.id.action_configurationFragment_self, bundle)
        }

        adapter.setOnParameterValueChanged {
            viewModel.setParameterValue(it.first,it.second)
        }

        adapter.setOnSaveButtonListener {
            viewModel.saveParametersValue()
        }

        viewModel.getList().observe(viewLifecycleOwner){
            if(it != null){
                adapter.differ.submitList(it)
            }
        }

    }


    companion object{
        private const val PARAMETER_TAG = "PARAMETER_TAG"
    }
}