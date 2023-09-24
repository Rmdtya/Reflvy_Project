package com.example.reflvy.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.reflvy.R
import com.example.reflvy.databinding.FragmentAddActivityBinding
import com.example.reflvy.databinding.FragmentStartingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StartingFragment : Fragment() {
    private lateinit var binding: FragmentStartingBinding
    private var listener: HintListener? = null

    private val linearContainerKeterangan = mutableListOf<View>()
    private val constraintHint = mutableListOf<View>()

    private var index : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStartingBinding.inflate(inflater, container, false)
        val rootView = binding.root


        linearContainerKeterangan.add(binding.keterangan1)
        linearContainerKeterangan.add(binding.keterangan2)
        linearContainerKeterangan.add(binding.keterangan3)
        linearContainerKeterangan.add(binding.keterangan4)
//        linearContainerKeterangan.add(binding.keterangan5)
//        linearContainerKeterangan.add(binding.keterangan6)


        constraintHint.add(binding.containerHint1)
        constraintHint.add(binding.containerHint2)
        constraintHint.add(binding.containerHint3)
        constraintHint.add(binding.containerHint4)
//        constraintHint.add(binding.containerHint5)
//        constraintHint.add(binding.containerHint6)

        showCurrentLayout()

        binding.btnNext.setOnClickListener {
            // Ganti ke tampilan berikutnya jika mungkin
            if (index < linearContainerKeterangan.size - 1) {
                index++
                showCurrentLayout()
            } else if(index >= linearContainerKeterangan.size - 1){
                listener?.GetEnd()
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            }
        }

        binding.btnPrev.setOnClickListener {
            // Ganti ke tampilan berikutnya jika mungkin
            if (index > 0) {
                index--
                showCurrentLayout()
            }
        }

        return rootView
    }

    private fun showCurrentLayout() {
        // Sembunyikan semua linear layout
        linearContainerKeterangan.forEach { it.visibility = View.GONE }
        constraintHint.forEach { it.visibility = View.INVISIBLE }

        // Tampilkan linear layout dengan indeks saat ini
        linearContainerKeterangan[index].visibility = View.VISIBLE
        constraintHint[index].visibility = View.VISIBLE

        if(index == 3){
            binding.scrollContainer.smoothScrollTo(0, 3000)
            listener?.GetScroll()
        }else if(index < 3){
            binding.scrollContainer.fullScroll(View.FOCUS_UP)
            listener?.GetBackScroll()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Pastikan aktivitas mengimplementasikan interface
        if (context is HintListener) {
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


interface HintListener{

    fun GetScroll()
    fun GetBackScroll()

    fun GetEnd()
}

