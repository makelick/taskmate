package com.makelick.taskmate2.ui.dashboard.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.makelick.taskmate2.R
import com.makelick.taskmate2.databinding.FragmentBoardBinding
import com.makelick.taskmate2.model.Board
import com.makelick.taskmate2.ui.SharedViewModel

class BoardFragment : Fragment() {

    private lateinit var binding: FragmentBoardBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var board : Board

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        board = sharedViewModel.currentBoard.value!!
        sharedViewModel.setCurrentIssue(null)

        val issueAdapter = IssueAdapter {
            sharedViewModel.setCurrentIssue(it)
            val action = BoardFragmentDirections.actionBoardFragmentToIssueFragment()
            findNavController().navigate(action)
        }

        sharedViewModel.currentBoard.observe(viewLifecycleOwner) { board ->
            if (board?.issues != null && board.issues.isNotEmpty()) {
                issueAdapter.submitList(board.issues)
            }
        }
        sharedViewModel.getFullBoard(board.id)

        binding.boardRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = issueAdapter
        }

        binding.addIssue.setOnClickListener {
            val action = BoardFragmentDirections.actionBoardFragmentToIssueFragment()
            findNavController().navigate(action)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.board_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_board -> {
                val action = BoardFragmentDirections.actionBoardFragmentToCreatingBoardFragment()
                findNavController().navigate(action)
                true
            }
            R.id.delete_board -> {
                sharedViewModel.deleteBoard()
                val action = BoardFragmentDirections.actionBoardFragmentToDashboardFragment()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}