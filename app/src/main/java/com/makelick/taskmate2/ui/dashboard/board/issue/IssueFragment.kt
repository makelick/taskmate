package com.makelick.taskmate2.ui.dashboard.board.issue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makelick.taskmate2.databinding.FragmentCreatingIssueBinding


class IssueFragment : Fragment() {

    private lateinit var binding: FragmentCreatingIssueBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatingIssueBinding.inflate(inflater)
        return binding.root
    }
}