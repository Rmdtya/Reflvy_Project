package com.example.reflvy.fragment

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.reflvy.OldDailyActivity
import com.example.reflvy.R
import com.example.reflvy.SchedullingActivity
import com.example.reflvy.databinding.FragmentAddActivityBinding
import com.example.reflvy.databinding.FragmentFilterActivityBinding

class FilterActivity : Fragment() {


    private lateinit var binding: FragmentFilterActivityBinding
    private var listener: FilterActivityListener? = null

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
        binding = FragmentFilterActivityBinding.inflate(inflater, container, false)
        val rootView = binding.root

        GetDay()
        GetStatus()

        if(OldDailyActivity.stat == "semua"){
            binding.semua.isChecked = true
        }else if(OldDailyActivity.stat == "belum"){
            binding.progres.isChecked = true
        }else if(OldDailyActivity.stat == "selesai"){
            binding.selesai.isChecked = true
        }

        binding.senin.isChecked = OldDailyActivity.sn
        binding.selasa.isChecked = OldDailyActivity.sl
        binding.rabu.isChecked = OldDailyActivity.rb
        binding.kamis.isChecked = OldDailyActivity.km
        binding.jumat.isChecked = OldDailyActivity.jm
        binding.sabtu.isChecked = OldDailyActivity.st
        binding.minggu.isChecked = OldDailyActivity.mg

        binding.btnConfirm.setOnClickListener {

            listener?.OnFilter(status, senin, selasa, rabu, kamis, jumat, sabtu, minggu)
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
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

                binding.selesai.isChecked = false
                binding.progres.isChecked = false

                binding.semua.isEnabled = false

                status = "semua"
            } else {
                binding.conSemua.setBackgroundResource(R.drawable.edit_text1)
                binding.textSemua.setTextColor(resources.getColor(R.color.textday_nonaktif))

                binding.semua.isEnabled = true
            }
        }

        binding.selesai.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.conSelesai.setBackgroundResource(R.drawable.checkbox_day)
                binding.textSelesai.setTextColor(resources.getColor(R.color.textday_aktif))

                binding.semua.isChecked = false
                binding.progres.isChecked = false

                binding.selesai.isEnabled = false

                status = "selesai"
            } else {
                binding.conSelesai.setBackgroundResource(R.drawable.edit_text1)
                binding.textSelesai.setTextColor(resources.getColor(R.color.textday_nonaktif))

                binding.selesai.isEnabled = true
            }
        }

        binding.progres.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.conProgres.setBackgroundResource(R.drawable.checkbox_day)
                binding.textProgres.setTextColor(resources.getColor(R.color.textday_aktif))

                binding.selesai.isChecked = false
                binding.semua.isChecked = false

                binding.progres.isEnabled = false

                status = "belum"
            } else {
                binding.conProgres.setBackgroundResource(R.drawable.edit_text1)
                binding.textProgres.setTextColor(resources.getColor(R.color.textday_nonaktif))

                binding.progres.isEnabled = true
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Pastikan aktivitas mengimplementasikan interface
        if (context is FilterActivityListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AddActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}

// AddActivityListener.kt
interface FilterActivityListener {
    fun OnFilter(status : String, senin : Boolean, selasa : Boolean, rabu : Boolean, kamis : Boolean, jumat : Boolean, sabtu : Boolean, minggu : Boolean) // Ganti String dengan tipe data yang ingin Anda kirim

    fun GetBack()
}
