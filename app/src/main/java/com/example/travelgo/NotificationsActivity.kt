package com.example.travelgo

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : AppCompatActivity() {

    private lateinit var rvNotifications: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var notificationAdapter: NotificationAdapter
    // Ganti baris penampung list lama menjadi seperti ini:
    private val notificationList = mutableListOf<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        rvNotifications = findViewById(R.id.rvNotifications)
        progressBar = findViewById(R.id.progressBar)

        rvNotifications.layoutManager = LinearLayoutManager(this)
        notificationAdapter = NotificationAdapter(notificationList) { notification ->
            markAsRead(notification.id)
        }
        rvNotifications.adapter = notificationAdapter

        loadNotifications()
    }

    private fun loadNotifications() {
        progressBar.visibility = View.VISIBLE
        val token = SessionManager.getToken(this) ?: return

        ApiClient.apiService.getNotifications("Bearer $token")
            .enqueue(object : Callback<NotificationListResponse> {
                override fun onResponse(call: Call<NotificationListResponse>, response: Response<NotificationListResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body()?.status == true) {
                        notificationList.clear()
                        response.body()?.data?.let { notificationList.addAll(it) }
                        notificationAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<NotificationListResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@NotificationsActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun markAsRead(id: Int) {
        val token = SessionManager.getToken(this) ?: return
        ApiClient.apiService.markNotificationRead("Bearer $token", id)
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful) {
                        loadNotifications()
                    }
                }
                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {}
            })
    }
}