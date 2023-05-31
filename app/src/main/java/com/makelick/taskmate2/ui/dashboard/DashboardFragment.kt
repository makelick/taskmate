package com.makelick.taskmate2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.makelick.taskmate2.databinding.FragmentDashboardBinding
import com.makelick.taskmate2.ui.MainActivity
import com.makelick.taskmate2.ui.SharedViewModel
import com.makelick.taskmate2.ui.SharedViewModelFactory

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.setCurrentBoard(null)

        val listAdapter = DashboardAdapter { board ->
            sharedViewModel.setCurrentBoard(board)
            val action = DashboardFragmentDirections.actionDashboardFragmentToBoardFragment()
            findNavController().navigate(action)
        }

        sharedViewModel.boardsList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getBoards()
    }
}