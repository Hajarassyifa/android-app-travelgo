package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtikelAdapter(
    private val listArtikel: ArrayList<Artikel>,
    private val onClick: (Artikel) -> Unit
) : RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder>() {

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArtikel: ImageView = itemView.findViewById(R.id.imgArtikel)
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudulArtikel)
        val tvKategori: TextView = itemView.findViewById(R.id.tvKategoriArtikel)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalArtikel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artikel, parent, false)

        return ArtikelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val artikel = listArtikel[position]

        holder.imgArtikel.setImageResource(artikel.gambar)
        holder.tvJudul.text = artikel.judul
        holder.tvKategori.text = "${artikel.kategori} • ${artikel.waktuBaca}"
        holder.tvTanggal.text = artikel.tanggal

        holder.itemView.setOnClickListener {
            onClick(artikel)
        }
    }

    override fun getItemCount(): Int = listArtikel.size
}