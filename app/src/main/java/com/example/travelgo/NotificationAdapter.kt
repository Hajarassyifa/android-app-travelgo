package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val items: MutableList<Notification>,
    private val onClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView   = view.findViewById(R.id.tvTitle)
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val tvTime: TextView    = view.findViewById(R.id.tvTime)
        val dotUnread: View     = view.findViewById(R.id.dotUnread)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notif = items[position]
        holder.tvTitle.text   = notif.title
        holder.tvMessage.text = notif.message
        holder.tvTime.text    = formatTime(notif.created_at)
        holder.dotUnread.visibility = if (!notif.is_read) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener { onClick(notif) }
    }

    override fun getItemCount() = items.size

    private fun formatTime(dateStr: String?): String {
        if (dateStr.isNullOrEmpty()) return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(dateStr) ?: return dateStr
            val diff = Date().time - date.time
            val minutes = diff / 60000
            val hours   = minutes / 60
            val days    = hours / 24
            when {
                minutes < 1  -> "Baru saja"
                minutes < 60 -> "$minutes menit lalu"
                hours < 24   -> "$hours jam lalu"
                days < 7     -> "$days hari lalu"
                else         -> SimpleDateFormat("dd MMM yyyy", Locale("id")).format(date)
            }
        } catch (e: Exception) { dateStr }
    }
}