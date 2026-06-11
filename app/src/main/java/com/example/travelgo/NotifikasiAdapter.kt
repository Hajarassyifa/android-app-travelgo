package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotifikasiAdapter(
    private val listNotifikasi: List<Notifikasi>
) : RecyclerView.Adapter<NotifikasiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView =
            itemView.findViewById(R.id.tvTitleNotif)

        val tvMessage: TextView =
            itemView.findViewById(R.id.tvMessageNotif)

        val tvDate: TextView =
            itemView.findViewById(R.id.tvDateNotif)

        val dotUnread: View =
            itemView.findViewById(R.id.dotUnread)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_notifikasi,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val notif = listNotifikasi[position]

        holder.tvTitle.text = notif.title
        holder.tvMessage.text = notif.message
        holder.tvDate.text = notif.createdAt

        holder.dotUnread.visibility =
            if (!notif.isRead)
                View.VISIBLE
            else
                View.GONE
    }

    override fun getItemCount(): Int {
        return listNotifikasi.size
    }
}