package com.makelick.taskmate2.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makelick.taskmate2.R
import com.makelick.taskmate2.databinding.FragmentDashboardBinding
import com.makelick.taskmate2.model.Board

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

        val listAdapter = BoardAdapter()
        listAdapter.submitList(listOf(
            Board("test", R.drawable.board_cover_1),
            Board("test2", R.drawable.board_cover_2),
            Board("test3", R.drawable.board_cover_3),
            Board("test4", R.drawable.board_cover_4),
            Board("test5", R.drawable.board_cover_5),
            Board("test6", R.drawable.board_cover_6),
            Board("test7", R.drawable.board_cover_7),
            Board("test8", R.drawable.board_cover_8)
        ))


        binding.list.apply {
            layoutManager =  LinearLayoutManager(context)
            adapter = listAdapter
        }

        return binding.root
    }
}