package com.example.reflvy.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.reflvy.R
import com.example.reflvy.data.DataDaily
import com.example.reflvy.databinding.FragmentDragonStatusBinding
import java.text.SimpleDateFormat
import java.util.Locale


class DragonStatusFragment : Fragment() {

    private lateinit var binding: FragmentDragonStatusBinding
    private var listener: OnActionDragonStatus? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDragonStatusBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.btnUpgrade.setOnClickListener {
            listener?.BackFunction(true)
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        return rootView
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


interface OnActionDragonStatus {
    fun BackFunction(back : Boolean) // Ganti String dengan tipe data yang ingin Anda kirim
}

