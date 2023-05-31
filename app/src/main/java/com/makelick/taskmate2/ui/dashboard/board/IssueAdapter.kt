package com.makelick.taskmate2.ui.dashboard.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makelick.taskmate2.databinding.IssueItemBinding
import com.makelick.taskmate2.model.Issue

class IssueAdapter(
    private val clickListener: ((Issue) -> Unit),
    private val deleteClickListener: ((Issue) -> Unit)
) : ListAdapter<Issue, IssueAdapter.IssueViewHolder>(
    IssueDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        return IssueViewHolder(IssueItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        val issue = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(issue)
        }
        holder.binding.deleteIssue.setOnClickListener {
            deleteClickListener(issue)
        }
        holder.bind(issue)
    }

    class IssueViewHolder(val binding: IssueItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(issue: Issue) {
            binding.apply {
                issueName.text = issue.title
                issueDescription.text = issue.description
            }
        }
    }

    class IssueDiffCallback : DiffUtil.ItemCallback<Issue>() {
        override fun areItemsTheSame(oldItem: Issue, newItem: Issue): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Issue, newItem: Issue): Boolean {
            return oldItem == newItem
        }
    }
}