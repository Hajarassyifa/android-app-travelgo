package com.example.travelgo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArtikelAdapter(private val listArtikel: List<Artikel>) :
    RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder>() {

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Biarkan ImageView dicari lewat ID bawaannya
        val ivArtikelImage: ImageView? = itemView.findViewById(R.id.ivArtikelImage)

        fun bind(artikel: Artikel) {
            // TRIK SAKTI: Jika pencarian ID selalu error, kita cari TextView secara otomatis
            // berdasarkan tipe komponennya yang ada di dalam layout item_artikel.xml
            val textViews = mutableListOf<TextView>()

            // Fungsi pembantu untuk mencari semua TextView di dalam layout secara rekursif
            fun findTextViews(view: View) {
                if (view is TextView) {
                    textViews.add(view)
                } else if (view is ViewGroup) {
                    for (i in 0 until view.childCount) {
                        findTextViews(view.getChildAt(i))
                    }
                }
            }

            // Mulai mencari semua TextView di dalam item layout
            findTextViews(itemView)

            // TextView pertama yang ditemukan otomatis dianggap sebagai Judul (Title)
            if (textViews.isNotEmpty()) {
                textViews[0].text = artikel.title
            }

            // TextView kedua yang ditemukan otomatis dianggap sebagai Tanggal (Date)
            if (textViews.size > 1) {
                textViews[1].text = artikel.date
            }

            // Handle URL Gambar dari Laragon/Hosting
            var imageUrl = artikel.image ?: ""
            if (imageUrl.contains("localhost")) {
                imageUrl = imageUrl.replace("localhost", "10.0.2.2")
            }

            if (ivArtikelImage != null && imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivArtikelImage)
            } else {
                ivArtikelImage?.setImageResource(R.drawable.placeholder_image)
            }

            // Aksi klik menuju halaman detail artikel
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailArtikelActivity::class.java).apply {
                    putExtra("ARTIKEL_ID", artikel.id)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artikel, parent, false)
        return ArtikelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        holder.bind(listArtikel[position])
    }

    override fun getItemCount(): Int = listArtikel.size
}