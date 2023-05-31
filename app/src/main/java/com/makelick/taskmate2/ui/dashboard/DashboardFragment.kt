package com.makelick.taskmate2.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.makelick.taskmate2.databinding.FragmentDashboardBinding
import com.makelick.taskmate2.network.TaskmateApi
import com.makelick.taskmate2.ui.MainActivity
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        val listAdapter = DashboardAdapter {
            val action = DashboardFragmentDirections.actionDashboardFragmentToBoardFragment()
            findNavController().navigate(action)
        }

        lifecycleScope.launch {
            val token = (activity as MainActivity).token
            val boards = TaskmateApi.retrofitService.getBoards("Bearer_$token")
            listAdapter.submitList(boards)
        }


        binding.list.apply {
            layoutManager =  LinearLayoutManager(context)
            adapter = listAdapter
        }

        return binding.root
    }
}