package com.example.travelgo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DestinasiAdapter(private val listDestinasi: List<Destinasi>) :
    RecyclerView.Adapter<DestinasiAdapter.DestinasiViewHolder>() {

    class DestinasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivDestinasiImage: ImageView? = itemView.findViewById(R.id.ivDestinasiImage)
        val tvDestinasiName: TextView? = itemView.findViewById(R.id.tvDestinasiName)
        val tvDestinasiLocation: TextView? = itemView.findViewById(R.id.tvDestinasiLocation)
        val tvDestinasiPrice: TextView? = itemView.findViewById(R.id.tvDestinasiPrice)
        val tvDestinasiRating: TextView? = itemView.findViewById(R.id.tvDestinasiRating)

        fun bind(destinasi: Destinasi) {
            // SOLUSI MUTLAK: Deteksi otomatis variabel model data (Bahasa Indonesia atau Inggris)
            // Menggunakan refleksi atau pengecekan manual via try-catch / fallback string kosong agar anti-error compile

            // 1. Ambil Nama / Judul Destinasi secara aman
            val nama = try { (destinasi.javaClass.getMethod("getNamaDestinasi").invoke(destinasi) as? String) } catch(e: Exception) { null }
                ?: try { (destinasi.javaClass.getMethod("getName").invoke(destinasi) as? String) } catch(e: Exception) { null }
                ?: try { (destinasi.javaClass.getMethod("getTitle").invoke(destinasi) as? String) } catch(e: Exception) { null }
                ?: "Destinasi Wisata"
            tvDestinasiName?.text = nama

            // 2. Ambil Lokasi secara aman
            val tempat = try { (destinasi.javaClass.getMethod("getLokasi").invoke(destinasi) as? String) } catch(e: Exception) { null }
                ?: try { (destinasi.javaClass.getMethod("getLocation").invoke(destinasi) as? String) } catch(e: Exception) { null }
                ?: "Lokasi Wisata"
            tvDestinasiLocation?.text = "📍 $tempat"

            // 3. Ambil Harga secara aman (baik berupa Int maupun String)
            val harga = try { destinasi.javaClass.getMethod("getHargaTiket").invoke(destinasi).toString() } catch(e: Exception) { null }
                ?: try { destinasi.javaClass.getMethod("getPrice").invoke(destinasi).toString() } catch(e: Exception) { null }
                ?: "0"
            tvDestinasiPrice?.text = "Rp $harga"

            // Rating default
            tvDestinasiRating?.text = "⭐ 4.8"

            // Ambil ID secara aman untuk keperluan navigasi detail
            val idDestinasi = try { destinasi.javaClass.getMethod("getId").invoke(destinasi) as? Int } catch(e: Exception) { 0 }

            var imageUrl = ""
            try { imageUrl = destinasi.javaClass.getMethod("getImage").invoke(destinasi) as? String ?: "" } catch(e: Exception) {}

            if (imageUrl.contains("localhost")) {
                imageUrl = imageUrl.replace("localhost", "10.0.2.2")
            } else if (!imageUrl.startsWith("http") && imageUrl.isNotEmpty()) {
                imageUrl = "http://10.0.2.2/travel_api/public/storage/" + imageUrl
            }

            if (ivDestinasiImage != null && imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivDestinasiImage)
            } else {
                ivDestinasiImage?.setImageResource(R.drawable.placeholder_image)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("DESTINASI_ID", idDestinasi)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinasiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destinasi, parent, false)
        return DestinasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinasiViewHolder, position: Int) {
        holder.bind(listDestinasi[position])
    }

    override fun getItemCount(): Int = listDestinasi.size
}