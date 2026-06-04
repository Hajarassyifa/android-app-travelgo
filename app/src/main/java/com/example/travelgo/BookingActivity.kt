package com.example.travelgo

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class BookingActivity : AppCompatActivity() {

    private lateinit var etTanggalBerangkat: EditText
    private lateinit var etJumlahTiket: EditText
    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etSpecialRequests: EditText
    private lateinit var btnBooking: Button

    private var destinasiId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        destinasiId = intent.getIntExtra("DESTINASI_ID", 0)

        etTanggalBerangkat = findViewById(R.id.etTanggalBerangkat)
        etJumlahTiket = findViewById(R.id.etJumlahTiket)
        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etNoHp = findViewById(R.id.etNoHp)
        etSpecialRequests = findViewById(R.id.etSpecialRequests)
        btnBooking = findViewById(R.id.btnBooking)

        etTanggalBerangkat.setOnClickListener {
            showDatePicker()
        }

        btnBooking.setOnClickListener {
            createBooking()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val tanggal = "$year-${month + 1}-$dayOfMonth"
                etTanggalBerangkat.setText(tanggal)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun createBooking() {
        val tanggalBerangkat = etTanggalBerangkat.text.toString()
        val jumlahTiket = etJumlahTiket.text.toString().toIntOrNull() ?: 0
        val nama = etNama.text.toString()
        val email = etEmail.text.toString()
        val noHp = etNoHp.text.toString()
        val specialRequests = etSpecialRequests.text.toString()

        if (tanggalBerangkat.isEmpty()) {
            Toast.makeText(this, "Pilih tanggal berangkat", Toast.LENGTH_SHORT).show()
            return
        }
        if (jumlahTiket <= 0) {
            Toast.makeText(this, "Jumlah tiket minimal 1", Toast.LENGTH_SHORT).show()
            return
        }
        if (nama.isEmpty()) {
            Toast.makeText(this, "Masukkan nama", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Masukkan email", Toast.LENGTH_SHORT).show()
            return
        }
        if (noHp.isEmpty()) {
            Toast.makeText(this, "Masukkan nomor HP", Toast.LENGTH_SHORT).show()
            return
        }

        val request = BookingRequest(
            destinasi_id = destinasiId,
            tanggal_berangkat = tanggalBerangkat,
            jumlah_tiket = jumlahTiket,
            customer_name = nama,
            customer_email = email,
            customer_phone = noHp,
            special_requests = specialRequests.ifEmpty { null }
        )

        btnBooking.isEnabled = false
        btnBooking.text = "Memproses..."

        RetrofitClient.instance.createBooking(request)
            .enqueue(object : Callback<BookingCreateResponse> {
                override fun onResponse(
                    call: Call<BookingCreateResponse>,
                    response: Response<BookingCreateResponse>
                ) {
                    btnBooking.isEnabled = true
                    btnBooking.text = "Booking"

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.success == true) {
                            Toast.makeText(
                                this@BookingActivity,
                                "Booking berhasil!\nKode: ${body.data.booking_code}\nTotal: Rp ${body.data.total_harga}",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(this@BookingActivity, body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@BookingActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BookingCreateResponse>, t: Throwable) {
                    btnBooking.isEnabled = true
                    btnBooking.text = "Booking"
                    Toast.makeText(this@BookingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}