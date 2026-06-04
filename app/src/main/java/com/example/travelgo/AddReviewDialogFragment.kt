package com.example.travelgo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewDialogFragment : BottomSheetDialogFragment() {

    private var packageId: Int = -1
    private var onReviewSaved: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Menggunakan layout inflator yang aman jika file custom dialog belum ter-index
        return inflater.inflate(android.R.layout.list_content, container, false)
    }

    private fun createReview(rating: Int, comment: String) {
        val token = SessionManager.getToken(requireContext()) ?: return
        val body = mapOf(
            "rating" to rating.toString(),
            "comment" to comment
        )

        // Mengirim packageId langsung sebagai Int (Bukan .toString()) sesuai blueprint API
        ApiClient.apiService.createReview("Bearer $token", packageId, body)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                        onReviewSaved?.invoke()
                        dismiss()
                    }
                }
                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {}
            })
    }
}