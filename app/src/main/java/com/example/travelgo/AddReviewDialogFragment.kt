package com.example.travelgo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewDialogFragment : BottomSheetDialogFragment() {

    private lateinit var ratingBar: RatingBar
    private lateinit var etComment: EditText
    private lateinit var btnSubmitReview: MaterialButton
    private var packageId: Int = 0

    companion object {
        fun newInstance(packageId: Int): AddReviewDialogFragment {
            val fragment = AddReviewDialogFragment()
            val args = Bundle()
            args.putInt("PACKAGE_ID", packageId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageId = arguments?.getInt("PACKAGE_ID") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_review, container, false)

        ratingBar = view.findViewById(R.id.ratingBar)
        etComment = view.findViewById(R.id.etComment)
        btnSubmitReview = view.findViewById(R.id.btnSubmitReview)

        btnSubmitReview.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = etComment.text.toString().trim()

            if (comment.isEmpty()) {
                Toast.makeText(requireContext(), "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            kirimReviewKeServer(rating, comment)
        }

        return view
    }

    private fun kirimReviewKeServer(rating: Int, comment: String) {
        val token = SessionManager.getToken(requireContext())

        // Memasukkan argumen ke dalam objek ReviewRequest sesuai rancangan ApiService yang baru
        val reviewRequest = ReviewRequest(packageId, rating, comment)

        btnSubmitReview.isEnabled = false
        btnSubmitReview.text = "Mengirim..."

        ApiClient.apiService.createReview("Bearer $token", reviewRequest)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    btnSubmitReview.isEnabled = true
                    btnSubmitReview.text = "Kirim Ulasan"

                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Gagal mengirim ulasan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    btnSubmitReview.isEnabled = true
                    btnSubmitReview.text = "Kirim Ulasan"
                    Toast.makeText(requireContext(), "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}