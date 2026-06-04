package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArtikelAdapter(
    private val artikelList: List<Artikel>
) : RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artikel, parent, false)
        return ArtikelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        holder.bind(artikelList[position])
    }

    override fun getItemCount(): Int = artikelList.size

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.ivArtikelImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvArtikelTitle)
        private val tvExcerpt: TextView = itemView.findViewById(R.id.tvArtikelExcerpt)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvArtikelCategory)
        private val tvDate: TextView = itemView.findViewById(R.id.tvArtikelDate)

        fun bind(artikel: Artikel) {
            tvTitle.text = artikel.title
            tvExcerpt.text = artikel.excerpt ?: ""
            tvCategory.text = artikel.category
            tvDate.text = artikel.published_at?.substring(0, 10) ?: ""

            Glide.with(itemView.context)
                .load(artikel.image)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(ivImage)
        }
    }
}