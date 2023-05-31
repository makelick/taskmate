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
import com.makelick.taskmate2.model.Issue
import com.makelick.taskmate2.model.Status
import com.makelick.taskmate2.ui.SharedViewModel

class BoardFragment : Fragment() {

    private lateinit var binding: FragmentBoardBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var issueList: List<Issue>

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

        sharedViewModel.setCurrentIssue(null)

        val issueAdapter = IssueAdapter(
            {
                sharedViewModel.setCurrentIssue(it)
                val action = BoardFragmentDirections.actionBoardFragmentToIssueFragment()
                findNavController().navigate(action)
            },
            {
                sharedViewModel.setCurrentIssue(it)
                sharedViewModel.deleteIssue(it)
                sharedViewModel.getFullBoard(sharedViewModel.currentBoard.value!!.id)
            }
        )


        sharedViewModel.currentBoard.observe(viewLifecycleOwner) { board ->
            if (board?.issues != null && board.issues.isNotEmpty()) {
                issueList = board.issues
                issueAdapter.submitList(issueList.filter { it.status == Status.BACKLOG })
            }
        }
        sharedViewModel.getFullBoard(sharedViewModel.currentBoard.value!!.id)

        binding.boardRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.boardRecyclerView.adapter = issueAdapter


        binding.addIssue.setOnClickListener {
            val action = BoardFragmentDirections.actionBoardFragmentToIssueFragment()
            findNavController().navigate(action)
        }

        binding.tabLayout.addOnTabSelectedListener(object : androidx.appcompat.widget.Toolbar.OnMenuItemClickListener,
            com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        issueAdapter.submitList(issueList.filter { it.status == Status.BACKLOG })
                    }
                    1 -> {
                        issueAdapter.submitList(issueList.filter { it.status == Status.TODO })
                    }
                    2 -> {
                        issueAdapter.submitList(issueList.filter { it.status == Status.IN_PROGRESS })
                    }
                    3 -> {
                        issueAdapter.submitList(issueList.filter { it.status == Status.DONE })
                    }
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        issueList = sharedViewModel.currentBoard.value?.issues ?: emptyList()
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