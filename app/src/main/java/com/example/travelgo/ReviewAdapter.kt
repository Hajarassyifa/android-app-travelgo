package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(
    private val items: List<Review>,
    private val showActions: Boolean = false,
    private val onEdit: (Review) -> Unit = {},
    private val onDelete: (Review) -> Unit = {}
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivDestinasiImage: ShapeableImageView = view.findViewById(R.id.ivDestinasiImage)
        val tvDestinasiName: TextView            = view.findViewById(R.id.tvDestinasiName)
        val tvDestinasiLocation: TextView        = view.findViewById(R.id.tvDestinasiLocation)
        val ratingBar: RatingBar                 = view.findViewById(R.id.ratingBar)
        val tvRating: TextView                   = view.findViewById(R.id.tvRating)
        val tvDate: TextView                     = view.findViewById(R.id.tvDate)
        val tvComment: TextView                  = view.findViewById(R.id.tvComment)
        val btnEditReview: ImageButton           = view.findViewById(R.id.btnEditReview)
        val btnDeleteReview: ImageButton         = view.findViewById(R.id.btnDeleteReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = items[position]

        holder.tvDestinasiName.text     = review.destinasi?.name ?: review.user?.name ?: "Pengguna"
        holder.tvDestinasiLocation.text = review.user?.name ?: ""
        holder.ratingBar.rating         = review.rating.toFloat()
        holder.tvRating.text            = review.rating.toString()
        holder.tvComment.text           = review.comment ?: ""
        holder.tvDate.text              = formatDate(review.created_at)

        val photoUrl = review.user?.photo
        if (!photoUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(photoUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivDestinasiImage)
        } else {
            holder.ivDestinasiImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        if (showActions) {
            holder.btnEditReview.visibility   = View.VISIBLE
            holder.btnDeleteReview.visibility = View.VISIBLE
            holder.btnEditReview.setOnClickListener   { onEdit(review) }
            holder.btnDeleteReview.setOnClickListener { onDelete(review) }
        } else {
            holder.btnEditReview.visibility   = View.GONE
            holder.btnDeleteReview.visibility = View.GONE
        }
    }

    override fun getItemCount() = items.size

    private fun formatDate(dateStr: String?): String {
        if (dateStr.isNullOrEmpty()) return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(dateStr) ?: return dateStr
            SimpleDateFormat("dd MMM yyyy", Locale("id")).format(date)
        } catch (e: Exception) {
            try {
                val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = sdf2.parse(dateStr) ?: return dateStr
                SimpleDateFormat("dd MMM yyyy", Locale("id")).format(date)
            } catch (e2: Exception) { dateStr }
        }
    }
}