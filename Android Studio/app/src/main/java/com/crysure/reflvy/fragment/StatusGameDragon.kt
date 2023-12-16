package com.crysure.reflvy.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.crysure.reflvy.R
import com.crysure.reflvy.data.GameStatusManager
import com.crysure.reflvy.databinding.FragmentStatusGameDragonBinding

class StatusGameDragon : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentStatusGameDragonBinding
    private var listener: OnActionDragonStatus? = null

    private lateinit var statusDragon : GameStatusManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStatusGameDragonBinding.inflate(inflater, container, false)
        val rootView = binding.root

        statusDragon = GameStatusManager.dragonStatus

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        SetStatusValue()

        return rootView
    }

    private fun SetStatusValue(){
        SetProgresColor(binding.barLapar, statusDragon.statLapar)
        SetProgresColor(binding.barPengetahuan, statusDragon.statPengetahuan)
        SetProgresColor(binding.barKesehatanfisik, statusDragon.statKesehatanFisik)
        SetProgresColor(binding.barKesehatanmental, statusDragon.statKesehatanMental)
        SetProgresColor(binding.barCinta, statusDragon.statCinta)
        SetProgresColor(binding.barHiburan, statusDragon.statHiburan)
        SetProgresColor(binding.barIstirahat, statusDragon.statIstirahat)
        SetProgresColor(binding.barSosial, statusDragon.statSosial)
    }

    private fun SetProgresColor(bar : ProgressBar, progres : Float){
        val colorRes1 = R.color.gamestatus_bar1
        val colorRes2 = R.color.gamestatus_bar2
        val colorRes3 = R.color.gamestatus_bar3
        val colorRes4 = R.color.gamestatus_bar4
        val colorRes5 = R.color.gamestatus_bar5

        val value : Int = progres.toInt()
        bar.progress = value

        if(progres <= 20){
            bar.progressTintList = ContextCompat.getColorStateList(requireContext(), colorRes1)
        }else if(progres in 20.001..39.999){
            bar.progressTintList = ContextCompat.getColorStateList(requireContext(), colorRes2)
        }else if(progres in 40.001..59.999){
            bar.progressTintList = ContextCompat.getColorStateList(requireContext(), colorRes3)
        }else if(progres in 60.001..79.999){
            bar.progressTintList = ContextCompat.getColorStateList(requireContext(), colorRes4)
        }else if(progres >= 80){
            bar.progressTintList = ContextCompat.getColorStateList(requireContext(), colorRes5)
        }
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