package com.example.travelgo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DestinasiAdapter(private val listDestinasi: List<Destinasi>) :
    RecyclerView.Adapter<DestinasiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // SESUAIKAN ID INI DENGAN XML (item_destinasi.xml)
        val imgWisata: ImageView = view.findViewById(R.id.imgDestinasi)
        val tvNama: TextView = view.findViewById(R.id.txtNama)
        val tvLokasi: TextView = view.findViewById(R.id.txtLokasi)
        val tvHarga: TextView = view.findViewById(R.id.txtHarga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destinasi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDestinasi[position]

        holder.tvNama.text = data.nama
        holder.tvLokasi.text = data.lokasi
        holder.tvHarga.text = data.harga
        // Gunakan .image sesuai dengan data class Destinasi terbaru
        holder.imgWisata.setImageResource(data.image)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("NAMA_DESTINASI", data.nama)
            intent.putExtra("LOKASI_DESTINASI", data.lokasi)
            intent.putExtra("HARGA_DESTINASI", data.harga)
            intent.putExtra("GAMBAR_DESTINASI", data.image)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listDestinasi.size

    companion object {
        fun notifyDataSetChanged() {
            TODO("Not yet implemented")
        }
    }
}
