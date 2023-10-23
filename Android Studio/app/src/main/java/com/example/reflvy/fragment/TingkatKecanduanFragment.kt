package com.example.reflvy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reflvy.databinding.FragmentTingkatKecanduanBinding

class TingkatKecanduanFragment : Fragment() {

    private lateinit var binding: FragmentTingkatKecanduanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTingkatKecanduanBinding.inflate(inflater, container, false)
        val rootView = binding.root
        // Inflate the layout for this fragment

        binding.btnBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        return rootView
    }

}