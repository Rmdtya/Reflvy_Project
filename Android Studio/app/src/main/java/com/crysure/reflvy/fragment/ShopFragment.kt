package com.crysure.reflvy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.crysure.reflvy.databinding.FragmentShopBinding

class ShopFragment : Fragment() {

    private lateinit var binding: FragmentShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        val rootView = binding.root
        // Inflate the layout for this fragment

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return rootView
    }


}