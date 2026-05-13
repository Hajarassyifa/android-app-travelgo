package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtikelAdapter(private val listArtikel: List<Artikel>) :
    RecyclerView.Adapter<ArtikelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgArtikel)
        val judul: TextView = view.findViewById(R.id.tvJudulArtikel)
        val info: TextView = view.findViewById(R.id.tvInfoArtikel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artikel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listArtikel[position]
        holder.judul.text = item.judul
        holder.info.text = "${item.kategori}  •  ${item.waktuBaca}"
        holder.img.setImageResource(item.gambar)
    }

    override fun getItemCount(): Int = listArtikel.size
}
