package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DestinasiAdapter(
    private val destinasiList: List<Destinasi>
) : RecyclerView.Adapter<DestinasiAdapter.DestinasiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinasiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destinasi, parent, false)
        return DestinasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinasiViewHolder, position: Int) {
        holder.bind(destinasiList[position])
    }

    override fun getItemCount(): Int = destinasiList.size

    class DestinasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivGambar: ImageView = itemView.findViewById(R.id.ivGambar)
        private val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        private val tvLokasi: TextView = itemView.findViewById(R.id.tvLokasi)
        private val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)

        fun bind(destinasi: Destinasi) {
            tvNama.text = destinasi.name
            tvLokasi.text = destinasi.location
            tvHarga.text = "Rp ${destinasi.price}"

            Glide.with(itemView.context)
                .load(destinasi.image)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(ivGambar)
        }
    }
}