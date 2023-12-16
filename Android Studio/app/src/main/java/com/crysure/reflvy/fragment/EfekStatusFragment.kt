package com.crysure.reflvy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.crysure.reflvy.data.DataEventGame
import com.crysure.reflvy.data.EfekStatusDragon
import com.crysure.reflvy.databinding.FragmentEfekStatusBinding

class EfekStatusFragment : Fragment() {

    private lateinit var binding: FragmentEfekStatusBinding
    private var listener: AddActivityListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEfekStatusBinding.inflate(inflater, container, false)
        val rootView = binding.root

        SetStatusPoint()

        return rootView
    }


    private fun SetStatusPoint(){

        val nilaiFloat = -0.01f
        val text = String.format("%.2f", nilaiFloat) // Menggunakan format dua desimal
        binding.textLapar.text = text

        binding.textLapar.text = EfekStatusDragon.efekNow.statLapar.toString()
        binding.textPengetahuan.text = EfekStatusDragon.efekNow.statPengetahuan.toString()
        binding.textKesehatanfisik.text = EfekStatusDragon.efekNow.statKesehatanFisik.toString()
        binding.textKesehatanmental.text = EfekStatusDragon.efekNow.statKesehatanMental.toString()
        binding.textCinta.text = EfekStatusDragon.efekNow.statCinta.toString()
        binding.textStress.text = EfekStatusDragon.efekNow.statHiburan.toString()
        binding.textIstirahat.text = EfekStatusDragon.efekNow.statIstirahat.toString()
        binding.textSosial.text = EfekStatusDragon.efekNow.statSosial.toString()

        var stringEfek: String = ""
        for (status in EfekStatusDragon.continuosStatus) {
            if (status.isActive) {
                val namaEfek = status.namaStatus
                stringEfek += ", $namaEfek"
            }
        }
        binding.textEfekcontinuos.text = stringEfek

        var stringEfeknegatif: String = ""
        for (status in DataEventGame.isNegatifEvent) {
            if (status.isActive) {
                val namaEfek = status.namaEvent
                stringEfeknegatif += ", $namaEfek"
            }
        }
        binding.textEfeknegatif.text = stringEfeknegatif



        //Toast.makeText(requireContext(), EfekStatusDragon.continuosStatus.size.toString(), Toast.LENGTH_SHORT).show()
    }
}