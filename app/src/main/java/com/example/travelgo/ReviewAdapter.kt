package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(
    private val listReview: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaReview)
        val tvRating: TextView = itemView.findViewById(R.id.tvRatingReview)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalReview)
        val tvKomentar: TextView = itemView.findViewById(R.id.tvKomentarReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = listReview[position]

        holder.tvNama.text = review.userName
        holder.tvRating.text = "${"★".repeat(review.rating)} ${review.rating}.0"
        holder.tvTanggal.text = review.date
        holder.tvKomentar.text = review.comment
    }

    override fun getItemCount(): Int = listReview.size
}