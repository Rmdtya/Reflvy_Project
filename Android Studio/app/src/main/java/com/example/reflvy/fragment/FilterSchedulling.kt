package com.example.reflvy.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reflvy.OldDailyActivity
import com.example.reflvy.R
import com.example.reflvy.SchedullingActivity
import com.example.reflvy.databinding.FragmentFilterActivityBinding
import com.example.reflvy.databinding.FragmentFilterSchedullingBinding

class FilterSchedulling : Fragment() {


    private lateinit var binding: FragmentFilterSchedullingBinding
    private var listener: AddActivityListener? = null

    private var senin : Boolean = false
    private var selasa : Boolean = false
    private var rabu : Boolean = false
    private var kamis : Boolean = false
    private var jumat : Boolean = false
    private var sabtu : Boolean = false
    private var minggu : Boolean = false

    private lateinit var status : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilterSchedullingBinding.inflate(inflater, container, false)
        val rootView = binding.root

        GetDay()
        GetStatus()

        if(SchedullingActivity.progres == "semua"){
            binding.semua.isChecked = true
        }else if(SchedullingActivity.progres == "belum"){
            binding.belum.isChecked = true
        }

        binding.senin.isChecked = SchedullingActivity.sn
        binding.selasa.isChecked = SchedullingActivity.sl
        binding.rabu.isChecked = SchedullingActivity.rb
        binding.kamis.isChecked = SchedullingActivity.km
        binding.jumat.isChecked = SchedullingActivity.jm
        binding.sabtu.isChecked = SchedullingActivity.st
        binding.minggu.isChecked = SchedullingActivity.mg

        binding.btnConfirm.setOnClickListener {

            listener?.OnFilterSchedulling(status, senin, selasa, rabu, kamis, jumat, sabtu, minggu)
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()

            // Set onClickListener to open TimePicker when clicked
        }

        binding.btnBack.setOnClickListener {
            listener?.GetBack()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }


        return rootView
    }

    private fun GetDay(){

        binding.senin.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                ResetDay()
                binding.senin.isChecked = true
                binding.conSenin.setBackgroundResource(R.drawable.checkbox_day)
                binding.textSenin.setTextColor(resources.getColor(R.color.textday_aktif))
            } else {
                binding.conSenin.setBackgroundResource(R.drawable.edit_text1)
                binding.textSenin.setTextColor(resources.getColor(R.color.textday_nonaktif))
            }

            senin = isChecked
        }

        binding.selasa.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                ResetDay()
                binding.selasa.isChecked = true

                binding.conSelasa.setBackgroundResource(R.drawable.checkbox_day)
                binding.textSelasa.setTextColor(resources.getColor(R.color.textday_aktif))
            } else {
                binding.conSelasa.setBackgroundResource(R.drawable.edit_text1)
                binding.textSelasa.setTextColor(resources.getColor(R.color.textday_nonaktif))
            }
            selasa = isChecked
        }

        binding.rabu.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                ResetDay()
                binding.rabu.isChecked = true
                binding.conRabu.setBackgroundResource(R.drawable.checkbox_day)
                binding.textRabu.setTextColor(resources.getColor(R.color.textday_aktif))
            } else {
                binding.conRabu.setBackgroundResource(R.drawable.edit_text1)
                binding.textRabu.setTextColor(resources.getColor(R.color.textday_nonaktif))
            }
            rabu = isChecked
        }

        binding.kamis.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                ResetDay()
                binding.kamis.isChecked = true
                binding.conKamis.setBackgroundResource(R.drawable.checkbox_day)
                binding.textKamis.setTextColor(resources.getColor(R.color.textday_aktif))
            } else {
                binding.conKamis.setBackgroundResource(R.drawable.edit_text1)
                binding.textKamis.setTextColor(resources.getColor(R.color.textday_nonaktif))
            }
            kamis = isChecked
        }

        binding.jumat.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                ResetDay()
                binding.jumat.isChecked = true
                binding.conJumat.setBackgroundResource(R.drawable.checkbox_day)
                binding.textJumat.setTextColor(resources.getColor(R.color.textday_aktif))
            } else {
                binding.conJumat.setBackgroundResource(R.drawable.edit_text1)
                binding.textJumat.setTextColor(resources.getColor(R.color.textday_nonaktif))
            }
            jumat = isChecked
        }

        binding.sabtu.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                ResetDay()
                binding.sabtu.isChecked = true
                binding.conSabtu.setBackgroundResource(R.drawable.checkbox_day)
                binding.textSabtu.setTextColor(resources.getColor(R.color.textday_aktif))
            } else {
                binding.conSabtu.setBackgroundResource(R.drawable.edit_text1)
                binding.textSabtu.setTextColor(resources.getColor(R.color.textday_nonaktif))
            }
            sabtu = isChecked
        }

        binding.minggu.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                ResetDay()
                binding.minggu.isChecked = true
                binding.conMinggu.setBackgroundResource(R.drawable.checkbox_day)
                binding.textMinggu.setTextColor(resources.getColor(R.color.textday_aktif))
            } else {
                binding.conMinggu.setBackgroundResource(R.drawable.edit_text1)
                binding.textMinggu.setTextColor(resources.getColor(R.color.textday_nonaktif))
            }
            minggu = isChecked
        }
    }

    private fun GetStatus(){

        binding.semua.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.conSemua.setBackgroundResource(R.drawable.checkbox_day)
                binding.textSemua.setTextColor(resources.getColor(R.color.textday_aktif))

                binding.belum.isChecked = false
                binding.semua.isEnabled = false

                status = "semua"
            } else {
                binding.conSemua.setBackgroundResource(R.drawable.edit_text1)
                binding.textSemua.setTextColor(resources.getColor(R.color.textday_nonaktif))

                binding.semua.isEnabled = true
            }
        }

        binding.belum.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.conBelum.setBackgroundResource(R.drawable.checkbox_day)
                binding.textBelum.setTextColor(resources.getColor(R.color.textday_aktif))

                binding.semua.isChecked = false

                binding.belum.isEnabled = false

                status = "selesai"
            } else {
                binding.conBelum.setBackgroundResource(R.drawable.edit_text1)
                binding.textBelum.setTextColor(resources.getColor(R.color.textday_nonaktif))

                binding.belum.isEnabled = true
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Pastikan aktivitas mengimplementasikan interface
        if (context is AddActivityListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AddActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    private fun ResetDay(){
        binding.senin.isChecked = false
        binding.selasa.isChecked = false
        binding.rabu.isChecked = false
        binding.kamis.isChecked = false
        binding.jumat.isChecked = false
        binding.sabtu.isChecked = false
        binding.minggu.isChecked = false

    }

}