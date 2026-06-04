package com.example.travelgo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTicketActivity : AppCompatActivity() {

    private lateinit var rvBookings: RecyclerView
    private lateinit var bookingAdapter: BookingAdapter
    private val bookingList = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ticket)

        rvBookings = findViewById(R.id.rvBookings)
        rvBookings.layoutManager = LinearLayoutManager(this)
        bookingAdapter = BookingAdapter(bookingList)
        rvBookings.adapter = bookingAdapter

        getMyBookings()
    }

    private fun getMyBookings() {
        RetrofitClient.instance.getBookings()
            .enqueue(object : Callback<BookingListResponse> {
                override fun onResponse(
                    call: Call<BookingListResponse>,
                    response: Response<BookingListResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.success == true) {
                            bookingList.clear()
                            bookingList.addAll(body.data)
                            bookingAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(this@MyTicketActivity, body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MyTicketActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BookingListResponse>, t: Throwable) {
                    Toast.makeText(this@MyTicketActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}