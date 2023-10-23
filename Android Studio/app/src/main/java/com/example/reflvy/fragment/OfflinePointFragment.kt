package com.example.reflvy.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reflvy.databinding.FragmentOfflinePointBinding

class OfflinePointFragment : Fragment() {

    private lateinit var binding: FragmentOfflinePointBinding
    private var listener: OnActionDragonStatus? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOfflinePointBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val totalCoin = arguments?.getFloat("totalCoin")

        binding.btnBack.setOnClickListener {
            listener?.BackFunction(true)
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        if (totalCoin != null) {
            binding.textCoin.text = FloatToShortString(totalCoin)
        }

        return rootView
    }

    fun FloatToShortString(value: Float): String {
        if (value >= 1000) {
            val thousands = value / 1000
            return String.format("%.1fk", thousands)
        }
        return value.toString()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Pastikan aktivitas mengimplementasikan interface
        if (context is OnActionDragonStatus) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AddActivityListener")
        }
    }
}
