package com.example.reflvy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.reflvy.data.DataCard
import com.example.reflvy.databinding.FragmentLibraryRoadmapsBinding
import com.example.reflvy.model.ViewPagerLibraryRoadmap

class LibraryRoadmapsFragment : Fragment() {

    private lateinit var binding: FragmentLibraryRoadmapsBinding
    private var listener: OnActionDragonStatus? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryRoadmapsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        AddtoList()

        return rootView
    }

    private fun AddtoList(){
        binding.viewPager.adapter = ViewPagerLibraryRoadmap(DataCard.dataCard)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.indicator.setViewPager(binding.viewPager)
    }
}