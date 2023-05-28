package com.makelick.taskmate2.ui.creatingboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.makelick.taskmate2.databinding.FragmentCreatingBoardBinding

class CreatingBoardFragment : Fragment() {

    private var _binding: FragmentCreatingBoardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val creatingBoardViewModel =
            ViewModelProvider(this).get(CreatingBoardViewModel::class.java)

        _binding = FragmentCreatingBoardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        creatingBoardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}