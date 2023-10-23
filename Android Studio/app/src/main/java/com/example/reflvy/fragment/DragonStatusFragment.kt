package com.example.reflvy.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.reflvy.R
import com.example.reflvy.data.EvolusiDragon
import com.example.reflvy.data.GameStatusManager
import com.example.reflvy.databinding.FragmentDragonStatusBinding


class DragonStatusFragment : Fragment() {

    private lateinit var binding: FragmentDragonStatusBinding
    private var listener: OnActionDragonStatus? = null

    private var progresNow : Int = 0
    private var progresMax : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDragonStatusBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val dragonStatus = GameStatusManager.dragonStatus
        val levelDragon = dragonStatus.levelDragon - 1

        val evolusiDragon = EvolusiDragon.evolusiDragon[levelDragon]
        val maxProgres = evolusiDragon.maxProgres

        progresNow = dragonStatus.progres
        progresMax = evolusiDragon.maxProgres

        binding.barProgres.max = maxProgres
        binding.barProgres.progress = dragonStatus.progres

        binding.textProgres.text = (dragonStatus.progres).toString()
        binding.textmaxProgres.text = evolusiDragon.maxProgres.toString()
        UpdateImage()

        if(dragonStatus.progres <= maxProgres){
            val biaya = evolusiDragon.biayaUpgrade[dragonStatus.progres]
            binding.btnUpgrade.text = "Upgrade Dragon : $biaya"

            binding.btnUpgrade.setOnClickListener {
                if (dragonStatus.coinPoint >= evolusiDragon.biayaUpgrade[dragonStatus.progres]){

                    if (progresNow < progresMax){
                        dragonStatus.progres++
                        dragonStatus.coinPoint -= biaya
                        binding.barProgres.progress = dragonStatus.progres
                        binding.textProgres.text = (dragonStatus.progres).toString()
                        progresNow++

                        val biaya = evolusiDragon.biayaUpgrade[dragonStatus.progres]
                        binding.btnUpgrade.text = "Upgrade Dragon : ${String.format("%.0f", biaya)}"

                        if (progresNow == progresMax){
                            val biaya = evolusiDragon.biayaUpgrade[dragonStatus.progres]
                            binding.btnUpgrade.text = "Evolusi Dragon : ${String.format("%.0f", biaya)}"
                        }

                        listener?.MinusCoin()
                    }else if (progresNow >= progresMax){
                        dragonStatus.levelDragon += 1
                        dragonStatus.coinPoint -= biaya
                        dragonStatus.progres = 0

                        dragonStatus.SaveDataStatus(requireContext())
                        listener?.IsEvolution(evolusiDragon.drawable)
                        listener?.BackFunction(true)
                        val fragmentManager = requireActivity().supportFragmentManager
                        fragmentManager.popBackStack()
                    }

                    dragonStatus.SaveDataStatus(requireContext())
                }else{
                    Toast.makeText(requireContext(), "Koin Tidak Cukup", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.btnSelengkapnya.setOnClickListener {

            val fragmentTransaction = parentFragmentManager.beginTransaction()
            val fragmentSecond = StatusGameDragon()
            fragmentTransaction.replace(R.id.fragment_container, fragmentSecond)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return rootView
    }

    private fun UpdateImage(){
        if (GameStatusManager.dragonStatus.levelDragon == 1){
            binding.imgDragon.setImageResource(R.drawable.roadmap_lv1)
        }else if (GameStatusManager.dragonStatus.levelDragon == 2) {
            binding.imgDragon.setImageResource(R.drawable.roadmap_lv2)
        }else if (GameStatusManager.dragonStatus.levelDragon == 3) {
            binding.imgDragon.setImageResource(R.drawable.roadmap_lv3)
        }else if (GameStatusManager.dragonStatus.levelDragon == 4) {
            binding.imgDragon.setImageResource(R.drawable.roadmap_lv4)
        }else if (GameStatusManager.dragonStatus.levelDragon == 5) {
            binding.imgDragon.setImageResource(R.drawable.roadmap_lv5)
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


interface OnActionDragonStatus {
    fun BackFunction(back : Boolean) // Ganti String dengan tipe data yang ingin Anda kirim

    fun IsEvolution(gambar : Int)

    fun MinusCoin()
}

