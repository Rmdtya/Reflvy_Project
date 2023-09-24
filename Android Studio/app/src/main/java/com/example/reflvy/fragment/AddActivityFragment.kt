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
import com.example.reflvy.R
import com.example.reflvy.SchedullingActivity
import com.example.reflvy.data.DataDaily
import com.example.reflvy.databinding.FragmentAddActivityBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AddActivityFragment : Fragment() {


    private lateinit var binding: FragmentAddActivityBinding
    private var listener: AddActivityListener? = null

    private var senin : Boolean = false
    private var selasa : Boolean = false
    private var rabu : Boolean = false
    private var kamis : Boolean = false
    private var jumat : Boolean = false
    private var sabtu : Boolean = false
    private var minggu : Boolean = false

    private var namaKegiatan : String = ""
    private var lamaKegiatan : Int = 0
    private var waktuMulai : String = ""
    private var waktuSelesai : String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        val rootView = binding.root

        AddTimeStart()
        AddTimeFinish()
        GetDay()

        binding.btnConfirm.setOnClickListener {

            if (binding.inpNama.text.isNotEmpty() && binding.inpLama.text.isNotEmpty() && binding.editTextHour1.text.isNotEmpty() && binding.editTextHour3.text.isNotEmpty()) {

                val nama = binding.inpNama.text.toString().trim()
                val tempLama = binding.inpLama.text.toString().trim()
                val lama = tempLama.toInt()
                val startTime = binding.editTextHour1.text.toString().trim() + binding.editTextHour2.text.toString().trim() + "." +
                        binding.editTextMinute1.text.toString().trim() + binding.editTextMinute2.text.toString().trim()
                val endTime = binding.editTextHour3.text.toString().trim() + binding.editTextHour4.text.toString().trim() + "." +
                        binding.editTextMinute3.text.toString().trim() + binding.editTextMinute4.text.toString().trim()

                val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
                val startDate = sdf.parse(startTime)
                val endDate = sdf.parse(endTime)

                val startMinutes = (startDate.hours * 60) + startDate.minutes
                val endMinutes = (endDate.hours * 60) + endDate.minutes
                val durationMinutes = endMinutes - startMinutes

                var overlap = false

                for (existingData in DataDaily.dataKegiatan) {
                    // Periksa hanya jika jadwal berada di hari yang sama
                    if (senin && existingData.senin ||
                        selasa && existingData.selasa ||
                        rabu && existingData.rabu ||
                        kamis && existingData.kamis ||
                        jumat && existingData.jumat ||
                        sabtu && existingData.sabtu ||
                        minggu && existingData.minggu) {

                        // Konversi waktu yang sudah ada ke menit
                        val existingStartMinutes = existingData.startMinute
                        val existingEndMinutes = existingData.endMinutes

                        // Periksa tumpang tindih
                        if ((startMinutes >= existingStartMinutes && startMinutes < existingEndMinutes) ||
                            (endMinutes > existingStartMinutes && endMinutes <= existingEndMinutes) ||
                            (startMinutes <= existingStartMinutes && endMinutes >= existingEndMinutes)) {
                            overlap = true
                            break
                        }
                    }
                }


                if(senin || selasa || rabu || kamis || jumat || sabtu || minggu){
                    if(!overlap){
                        if (durationMinutes <= 719){
                            listener?.onDataAdded(nama, lama, startTime, endTime, senin, selasa, rabu, kamis, jumat, sabtu, minggu)
                            val fragmentManager = requireActivity().supportFragmentManager
                            fragmentManager.popBackStack()
                        }else{
                            Toast.makeText(requireContext(), "Satu Kegiatan Maksimal 12 Jam", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                    Toast.makeText(requireContext(), "Sudah Ada Kegiatan pada jam ini", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Pilih Minimal 1 Hari", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            listener?.GetBack()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        // Set onClickListener to open TimePicker when clicked

        return rootView
    }

    private fun AddTimeStart(){

        val editTextHour1 = binding.editTextHour1
        val editTextHour2 = binding.editTextHour2
        val editTextMinute1 = binding.editTextMinute1
        val editTextMinute2 = binding.editTextMinute2

        editTextHour1.inputType = InputType.TYPE_NULL
        editTextHour2.inputType = InputType.TYPE_NULL
        editTextMinute1.inputType = InputType.TYPE_NULL
        editTextMinute2.inputType = InputType.TYPE_NULL

        val timePickerDialog = TimePickerDialog(requireContext(),
            R.style.BlueTimePickerDialog,
            { _, hourOfDay, minute ->
                val formattedHour = String.format("%02d", hourOfDay)
                val formattedMinute = String.format("%02d", minute)
                editTextHour1.setText(formattedHour[0].toString())
                editTextHour2.setText(formattedHour[1].toString())
                editTextMinute1.setText(formattedMinute[0].toString())
                editTextMinute2.setText(formattedMinute[1].toString())
            }, 0, 0, true)

        editTextHour1.setOnClickListener { timePickerDialog.show() }
        editTextHour2.setOnClickListener { timePickerDialog.show() }
        editTextMinute1.setOnClickListener { timePickerDialog.show() }
        editTextMinute2.setOnClickListener { timePickerDialog.show() }
    }

    private fun AddTimeFinish(){

        val editTextHour3 = binding.editTextHour3
        val editTextHour4 = binding.editTextHour4
        val editTextMinute3 = binding.editTextMinute3
        val editTextMinute4 = binding.editTextMinute4

        editTextHour3.inputType = InputType.TYPE_NULL
        editTextHour4.inputType = InputType.TYPE_NULL
        editTextMinute3.inputType = InputType.TYPE_NULL
        editTextMinute4.inputType = InputType.TYPE_NULL

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.BlueTimePickerDialog,
            { _, hourOfDay, minute ->
                val formattedHour = String.format("%02d", hourOfDay)
                val formattedMinute = String.format("%02d", minute)
                editTextHour3.setText(formattedHour[0].toString())
                editTextHour4.setText(formattedHour[1].toString())
                editTextMinute3.setText(formattedMinute[0].toString())
                editTextMinute4.setText(formattedMinute[1].toString())
            }, 0, 0, true
        )

        editTextHour3.setOnClickListener { timePickerDialog.show() }
        editTextHour4.setOnClickListener { timePickerDialog.show() }
        editTextMinute3.setOnClickListener { timePickerDialog.show() }
        editTextMinute4.setOnClickListener { timePickerDialog.show() }
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
}

// AddActivityListener.kt
interface AddActivityListener {
    fun onDataAdded(namaKegiatan : String, lama : Int, mulai : String, selesai : String, senin : Boolean, selasa : Boolean, rabu : Boolean, kamis : Boolean, jumat : Boolean, sabtu : Boolean, minggu : Boolean) // Ganti String dengan tipe data yang ingin Anda kirim

    fun GetBack()

    fun OnFilterSchedulling(status : String, senin : Boolean, selasa : Boolean, rabu : Boolean, kamis : Boolean, jumat : Boolean, sabtu : Boolean, minggu : Boolean) // Ganti String dengan tipe data yang ingin Anda kirim

    fun OnDataEdit(index : Int, namaKegiatan : String, lama : Int, mulai : String, selesai : String, senin : Boolean, selasa : Boolean, rabu : Boolean, kamis : Boolean, jumat : Boolean, sabtu : Boolean, minggu : Boolean)

    fun OnDelete(index : Int)
}
