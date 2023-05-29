package com.makelick.taskmate2.ui.creatingboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.makelick.taskmate2.R
import com.makelick.taskmate2.databinding.FragmentCreatingBoardBinding

class CreatingBoardFragment : Fragment() {

    private lateinit var binding: FragmentCreatingBoardBinding
    val creatingBoardViewModel: CreatingBoardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatingBoardBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coverList14.setOnCheckedChangeListener(::onRadiobuttonChecked)
        binding.coverList58.setOnCheckedChangeListener(::onRadiobuttonChecked)


    }

    private fun onRadiobuttonChecked(group: RadioGroup, checkedid: Int) {
        when (group) {
            binding.coverList14 -> {
                binding.coverList58.setOnCheckedChangeListener(null)
                binding.coverList58.clearCheck()
                binding.coverList58.setOnCheckedChangeListener(::onRadiobuttonChecked)
                creatingBoardViewModel.image = when (checkedid) {
                    0 -> R.drawable.board_cover_1
                    1 -> R.drawable.board_cover_2
                    2 -> R.drawable.board_cover_3
                    3 -> R.drawable.board_cover_4
                    else -> null
                }
            }
            binding.coverList58 -> {
                binding.coverList14.setOnCheckedChangeListener(null)
                binding.coverList14.clearCheck()
                binding.coverList14.setOnCheckedChangeListener(::onRadiobuttonChecked)
                creatingBoardViewModel.image = when (checkedid) {
                    0 -> R.drawable.board_cover_5
                    1 -> R.drawable.board_cover_6
                    2 -> R.drawable.board_cover_7
                    3 -> R.drawable.board_cover_8
                    else -> null
                }

            }
        }
    }
}