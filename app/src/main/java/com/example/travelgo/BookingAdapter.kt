package com.example.travelgo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class BookingAdapter(
    private val bookingList: List<Booking>
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookingList[position])
    }

    override fun getItemCount(): Int = bookingList.size

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBookingCode: TextView = itemView.findViewById(R.id.tvBookingCode)
        private val tvDestinasiName: TextView = itemView.findViewById(R.id.tvDestinasiName)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        private val tvJumlahTiket: TextView = itemView.findViewById(R.id.tvJumlahTiket)
        private val tvTotalHarga: TextView = itemView.findViewById(R.id.tvTotalHarga)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(booking: Booking) {
            tvBookingCode.text = booking.booking_code
            tvDestinasiName.text = booking.destinasi?.name ?: "-"
            tvTanggal.text = booking.tanggal_berangkat
            tvJumlahTiket.text = "${booking.jumlah_tiket} tiket"

            // PERBAIKAN: Ganti Locale constructor dengan forLanguageTag
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
            tvTotalHarga.text = formatRupiah.format(booking.total_harga)

            tvStatus.text = booking.status
            when (booking.status) {
                "pending" -> tvStatus.setBackgroundColor(0xFFFF9800.toInt())
                "paid" -> tvStatus.setBackgroundColor(0xFF4CAF50.toInt())
                "cancelled" -> tvStatus.setBackgroundColor(0xFFF44336.toInt())
                "completed" -> tvStatus.setBackgroundColor(0xFF2196F3.toInt())
            }
        }
    }
}