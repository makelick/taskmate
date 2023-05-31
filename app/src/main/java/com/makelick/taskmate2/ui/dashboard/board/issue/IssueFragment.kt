package com.makelick.taskmate2.ui.dashboard.board.issue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.makelick.taskmate2.databinding.FragmentCreatingIssueBinding
import com.makelick.taskmate2.model.Status
import com.makelick.taskmate2.ui.SharedViewModel


class IssueFragment : Fragment() {

    private lateinit var binding: FragmentCreatingIssueBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatingIssueBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveAction.setOnClickListener{
            val status = when (binding.issueStatus.selectedItem.toString()) {
                "Backlog" -> Status.BACKLOG
                "Todo" -> Status.TODO
                "In progress" -> Status.IN_PROGRESS
                "Done" -> Status.DONE
                else -> Status.BACKLOG
            }
            sharedViewModel.createIssue(
                binding.issueName.text.toString(),
                binding.issueDescription.text.toString(),
                status)
            val action = IssueFragmentDirections.actionIssueFragmentToBoardFragment()
            findNavController().navigate(action)
        }

        if (sharedViewModel.currentIssue.value != null) {
            binding.issueName.setText(sharedViewModel.currentIssue.value?.title)
            binding.issueDescription.setText(sharedViewModel.currentIssue.value?.description)
            binding.issueStatus.setSelection(
                when (sharedViewModel.currentIssue.value?.status) {
                    Status.BACKLOG -> 0
                    Status.TODO -> 1
                    Status.IN_PROGRESS -> 2
                    Status.DONE -> 3
                    else -> 0
                }
            )
            binding.saveAction.setOnClickListener {
                val status = when (binding.issueStatus.selectedItem.toString()) {
                    "Backlog" -> Status.BACKLOG
                    "Todo" -> Status.TODO
                    "In progress" -> Status.IN_PROGRESS
                    "Done" -> Status.DONE
                    else -> Status.BACKLOG
                }
                sharedViewModel.updateIssue(
                    binding.issueName.text.toString(),
                    binding.issueDescription.text.toString(),
                    status)
                val action = IssueFragmentDirections.actionIssueFragmentToBoardFragment()
                findNavController().navigate(action)
            }
        }
    }
}