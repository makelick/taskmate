package com.makelick.taskmate2.ui.creatingboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.makelick.taskmate2.databinding.FragmentCreatingBoardBinding
import com.makelick.taskmate2.ui.SharedViewModel

class CreatingBoardFragment : Fragment() {

    private lateinit var binding: FragmentCreatingBoardBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var imageUrl: String =
        "https://res.cloudinary.com/dtay106eo/image/upload/v1682436194/oc-gonzalez-xg8z_KhSorQ-unsplash_gfhgto.webp"

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

        binding.saveAction.setOnClickListener {
            sharedViewModel.createBoard(binding.boardName.text.toString(), imageUrl)
            navigateToBoard()
        }

        if (sharedViewModel.currentBoard.value != null) {
            binding.boardName.setText(sharedViewModel.currentBoard.value!!.name)
            imageUrl = sharedViewModel.currentBoard.value!!.imageUrl

            binding.saveAction.setOnClickListener {
                sharedViewModel.updateBoard(
                    sharedViewModel.currentBoard.value!!.id,
                    binding.boardName.text.toString(),
                    imageUrl
                )
                navigateToBoard()
            }
        }

    }

    private fun onRadiobuttonChecked(group: RadioGroup, checkedid: Int) {
        when (group) {
            binding.coverList14 -> {
                binding.coverList58.setOnCheckedChangeListener(null)
                binding.coverList58.clearCheck()
                binding.coverList58.setOnCheckedChangeListener(::onRadiobuttonChecked)
                imageUrl = when (checkedid) {
                    0 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436194/oc-gonzalez-xg8z_KhSorQ-unsplash_gfhgto.webp"
                    1 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436169/jason-ortego-buF62ewDLcQ-unsplash_ysofvo.webp"
                    2 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436153/eberhard-grossgasteiger-y2azHvupCVo-unsplash_ffng0b.webp"
                    else -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436149/fabian-quintero-UWQP2mh5YJI-unsplash_qtsxfp.webp"
                }
            }

            binding.coverList58 -> {
                binding.coverList14.setOnCheckedChangeListener(null)
                binding.coverList14.clearCheck()
                binding.coverList14.setOnCheckedChangeListener(::onRadiobuttonChecked)
                imageUrl = when (checkedid) {
                    0 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436137/willian-justen-de-vasconcellos-T_Qe4QlMIvQ-unsplash_mnlsfm.webp"
                    1 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436137/nikola-majksner-hXNGeAFOgT4-unsplash_ta97kg.webp"
                    2 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436136/david-marcu-78A265wPiO4-unsplash_ifbuyf.webp"
                    else -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436136/jay-mantri-TFyi0QOx08c-unsplash_ntnbzd.webp"
                }

            }
        }
    }

    private fun navigateToBoard() {
        val action =
            CreatingBoardFragmentDirections.actionCreatingBoardFragmentToDashboardFragment()
        findNavController().navigate(action)
    }
}