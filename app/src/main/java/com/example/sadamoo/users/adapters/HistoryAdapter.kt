package com.example.sadamoo.users.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sadamoo.databinding.ItemHistoryBinding
import com.example.sadamoo.users.models.HistoryItem
import com.example.sadamoo.users.models.HistoryType
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private var historyList: List<HistoryItem>,
    private val onItemClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryItem) {
            binding.apply {
                tvTitle.text = item.title
                tvSubtitle.text = item.subtitle
                tvDate.text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(item.date)
                tvStatus.text = item.status

                // Set icon based on type
                when (item.type) {
                    HistoryType.SCAN -> {
                        ivIcon.setImageResource(com.example.sadamoo.R.drawable.ic_scan)
                        ivIcon.setColorFilter(itemView.context.getColor(android.R.color.holo_blue_bright))
                    }
                    HistoryType.CONSULTATION -> {
                        ivIcon.setImageResource(com.example.sadamoo.R.drawable.ic_chat)
                        ivIcon.setColorFilter(itemView.context.getColor(android.R.color.holo_green_light))
                    }
                }

                // Set status color
                val statusColor = when (item.status) {
                    "Selesai" -> android.R.color.holo_green_dark
                    "Berlangsung" -> android.R.color.holo_orange_dark
                    "Menunggu" -> android.R.color.holo_blue_dark
                    else -> android.R.color.darker_gray
                }
                tvStatus.setTextColor(itemView.context.getColor(statusColor))

                // Set severity indicator for scan results
                if (item.type == HistoryType.SCAN && item.severity != null) {
                    tvSeverity.text = item.severity
                    tvSeverity.visibility = android.view.View.VISIBLE

                    val severityColor = when (item.severity) {
                        "Ringan" -> android.R.color.holo_green_light
                        "Sedang" -> android.R.color.holo_orange_light
                        "Berat" -> android.R.color.holo_red_light
                        else -> android.R.color.darker_gray
                    }
                    tvSeverity.setTextColor(itemView.context.getColor(severityColor))
                } else {
                    tvSeverity.visibility = android.view.View.GONE
                }

                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    fun updateData(newList: List<HistoryItem>) {
        historyList = newList
        notifyDataSetChanged()
    }
}
