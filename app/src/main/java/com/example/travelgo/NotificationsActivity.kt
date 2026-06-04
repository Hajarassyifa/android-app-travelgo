package com.example.travelgo

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : AppCompatActivity() {

    private lateinit var rvNotifications: RecyclerView
    private lateinit var btnBack: ImageButton
    private lateinit var btnMarkAllRead: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var layoutUnreadBadge: LinearLayout
    private lateinit var tvUnreadCount: TextView

    private lateinit var adapter: NotificationAdapter
    private val notifications = mutableListOf<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        initViews()
        setupRecyclerView()
        setupClickListeners()
        loadNotifications()
    }

    private fun initViews() {
        rvNotifications   = findViewById(R.id.rvNotifications)
        btnBack           = findViewById(R.id.btnBack)
        btnMarkAllRead    = findViewById(R.id.btnMarkAllRead)
        progressBar       = findViewById(R.id.progressBar)
        layoutEmpty       = findViewById(R.id.layoutEmpty)
        layoutUnreadBadge = findViewById(R.id.layoutUnreadBadge)
        tvUnreadCount     = findViewById(R.id.tvUnreadCount)
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter(notifications) { notification ->
            if (!notification.is_read) markAsRead(notification.id)
        }
        rvNotifications.layoutManager = LinearLayoutManager(this)
        rvNotifications.adapter = adapter
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }
        btnMarkAllRead.setOnClickListener { markAllRead() }
    }

    private fun loadNotifications() {
        val token = SessionManager.getToken(this) ?: return
        showLoading(true)

        ApiClient.apiService.getNotifications("Bearer $token")
            .enqueue(object : Callback<NotificationListResponse> {
                override fun onResponse(call: Call<NotificationListResponse>, response: Response<NotificationListResponse>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val data = response.body()?.data ?: emptyList()
                        notifications.clear()
                        notifications.addAll(data)
                        adapter.notifyDataSetChanged()

                        val unreadCount = data.count { !it.is_read }
                        if (unreadCount > 0) {
                            layoutUnreadBadge.visibility = View.VISIBLE
                            tvUnreadCount.text = "$unreadCount notifikasi belum dibaca"
                        } else {
                            layoutUnreadBadge.visibility = View.GONE
                        }

                        layoutEmpty.visibility      = if (data.isEmpty()) View.VISIBLE else View.GONE
                        rvNotifications.visibility  = if (data.isEmpty()) View.GONE else View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<NotificationListResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@NotificationsActivity, "Gagal memuat notifikasi", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun markAsRead(id: Int) {
        val token = SessionManager.getToken(this) ?: return
        ApiClient.apiService.markNotificationRead("Bearer $token", id)
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful) {
                        val idx = notifications.indexOfFirst { it.id == id }
                        if (idx != -1) {
                            notifications[idx] = notifications[idx].copy(is_read = true)
                            adapter.notifyItemChanged(idx)
                            updateUnreadBadge()
                        }
                    }
                }
                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {}
            })
    }

    private fun markAllRead() {
        val token = SessionManager.getToken(this) ?: return
        ApiClient.apiService.markAllNotificationsRead("Bearer $token")
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful) {
                        notifications.replaceAll { it.copy(is_read = true) }
                        adapter.notifyDataSetChanged()
                        layoutUnreadBadge.visibility = View.GONE
                        Toast.makeText(this@NotificationsActivity, "Semua notifikasi telah dibaca", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {}
            })
    }

    private fun updateUnreadBadge() {
        val unreadCount = notifications.count { !it.is_read }
        if (unreadCount > 0) {
            tvUnreadCount.text = "$unreadCount notifikasi belum dibaca"
        } else {
            layoutUnreadBadge.visibility = View.GONE
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}