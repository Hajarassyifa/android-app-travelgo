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
        // ID dicocokkan 100% dengan item_artikel.xml aslimu
        val ivArtikelImage: ImageView? = itemView.findViewById(R.id.ivArtikelImage)
        val tvTitle: TextView? = itemView.findViewById(R.id.tvTitle)

        fun bind(artikel: Artikel) {
            tvTitle?.text = artikel.title

            // Ambil data gambar dari database phpMyAdmin
            var imageUrl = artikel.image ?: ""

            // Logika konversi alamat IP khusus emulator
            if (imageUrl.contains("localhost")) {
                imageUrl = imageUrl.replace("localhost", "10.0.2.2")
            } else if (!imageUrl.startsWith("http") && imageUrl.isNotEmpty()) {
                // Jika di database hanya tertulis nama file (misal: tips.jpg), arahkan ke folder penyimpanan Laragon
                imageUrl = "http://10.0.2.2/travel_api/public/storage/" + imageUrl
            }

            // Muat gambar asli lewat Glide
            if (ivArtikelImage != null && imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivArtikelImage)
            } else {
                ivArtikelImage?.setImageResource(R.drawable.placeholder_image)
            }

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