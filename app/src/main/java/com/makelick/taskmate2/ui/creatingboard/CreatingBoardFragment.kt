package com.makelick.taskmate2.ui.creatingboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.makelick.taskmate2.databinding.FragmentCreatingBoardBinding
import com.makelick.taskmate2.ui.MainActivity

class CreatingBoardFragment : Fragment() {

    private lateinit var binding: FragmentCreatingBoardBinding
    private val creatingBoardViewModel: CreatingBoardViewModel by viewModels()

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
            creatingBoardViewModel.saveBoard(binding.boardName.text.toString(), activity as MainActivity)
        }

    }

    private fun onRadiobuttonChecked(group: RadioGroup, checkedid: Int) {
        when (group) {
            binding.coverList14 -> {
                binding.coverList58.setOnCheckedChangeListener(null)
                binding.coverList58.clearCheck()
                binding.coverList58.setOnCheckedChangeListener(::onRadiobuttonChecked)
                creatingBoardViewModel.imageUrl = when (checkedid) {
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
                creatingBoardViewModel.imageUrl = when (checkedid) {
                    0 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436137/willian-justen-de-vasconcellos-T_Qe4QlMIvQ-unsplash_mnlsfm.webp"
                    1 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436137/nikola-majksner-hXNGeAFOgT4-unsplash_ta97kg.webp"
                    2 -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436136/david-marcu-78A265wPiO4-unsplash_ifbuyf.webp"
                    else -> "https://res.cloudinary.com/dtay106eo/image/upload/v1682436136/jay-mantri-TFyi0QOx08c-unsplash_ntnbzd.webp"
                }

            }
        }
    }
}