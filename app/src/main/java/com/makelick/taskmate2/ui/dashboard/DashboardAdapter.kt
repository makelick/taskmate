package com.makelick.taskmate2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makelick.taskmate2.databinding.BoardItemBinding
import com.makelick.taskmate2.model.Board

class DashboardAdapter(private val clickListener: ((Board) -> Unit)) : ListAdapter<Board, DashboardAdapter.BoardViewHolder>(BoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(BoardItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(board)
        }
        holder.bind(board)
    }

    class BoardViewHolder(private val binding: BoardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Board) {
            binding.apply {
                boardName.text = board.name
                boardCover.setImageResource(board.image)

            }
        }
    }

    class BoardDiffCallback : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }
    }
}