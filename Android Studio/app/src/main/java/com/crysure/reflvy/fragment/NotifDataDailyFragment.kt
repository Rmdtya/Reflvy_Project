package com.crysure.reflvy.fragment

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crysure.reflvy.R
import com.crysure.reflvy.data.DataDaily
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.databinding.FragmentNotifDataDailyBinding

class NotifDataDailyFragment : Fragment() {
    private lateinit var binding: FragmentNotifDataDailyBinding
    private var listener: OnDailyNotifListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotifDataDailyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val index = arguments?.getInt("index")
        val totalIndex = arguments?.getInt("totalIndex")

        if(index != null){

            val dataIndex = NotifyChat.notifChat[index].index
            val data = DataDaily.dataKegiatan[dataIndex]

            val durasi : String = (data.endMinutes - data.startMinute).toString()

            binding.textKegiatan.text = data.namaKegiatan
            binding.textDurasi.text = "Durasi : " + durasi
            binding.textTime.text = "Waktu : " + data.waktuMulai + " - " + data.waktuSelesai
        }



        binding.btnYes.setOnClickListener {
            if (index != null) {
                listener?.ButtonAction(true, index)
            }
            closeFragmentWithAnimation()

            if (index != null) {
                if(index >= totalIndex!!){
                    listener?.BackFunction(true)
                }
            }
        }

        binding.btnNo.setOnClickListener {
            if (index != null) {
                listener?.ButtonAction(false, index)
            }
            closeFragmentWithAnimation()

            if (index != null) {
                if(index >= totalIndex!!){
                    listener?.BackFunction(true)
                }
            }
        }

//        binding.btnNo.setOnClickListener {
//
//            listener?.BackFunction(true)
//            val fragmentTransaction = parentFragmentManager.beginTransaction()
//            val fragmentSecond = StatusGameDragon()
//            fragmentTransaction.replace(R.id.fragment_container, fragmentSecond)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }

        return rootView
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Pastikan aktivitas mengimplementasikan interface
        if (context is OnDailyNotifListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AddActivityListener")
        }
    }

    private fun closeFragmentWithAnimation() {
        val slideUpAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.slide_up) as ObjectAnimator
        slideUpAnimator.setTarget(binding.root)

        // Menggunakan handler untuk menghapus fragment setelah animasi selesai
        val handler = Handler()
        handler.postDelayed({
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }, slideUpAnimator.duration)

        slideUpAnimator.start()
    }
}


interface OnDailyNotifListener {
    fun BackFunction(back : Boolean) // Ganti String dengan tipe data yang ingin Anda kirim

    fun ButtonAction(status : Boolean, index : Int)
}