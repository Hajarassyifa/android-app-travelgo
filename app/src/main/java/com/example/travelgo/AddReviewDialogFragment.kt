package com.example.travelgo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.travelgo.databinding.DialogAddReviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewDialogFragment : BottomSheetDialogFragment() {

    private var _binding: DialogAddReviewBinding? = null
    private val binding get() = _binding!!

    private var packageId: Int = -1
    private var existingReview: Review? = null
    var onReviewSaved: (() -> Unit)? = null

    companion object {
        fun newInstance(packageId: Int, review: Review? = null) =
            AddReviewDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt("package_id", packageId)
                    review?.let {
                        putInt("review_id", it.id)
                        putInt("rating", it.rating)
                        putString("comment", it.comment ?: "")
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageId = arguments?.getInt("package_id", -1) ?: -1
        val reviewId = arguments?.getInt("review_id", -1) ?: -1
        if (reviewId != -1) {
            existingReview = Review(
                id = reviewId,
                rating = arguments?.getInt("rating", 5) ?: 5,
                comment = arguments?.getString("comment"),
                user = null,
                destinasi = null,
                created_at = null
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogAddReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isEdit = existingReview != null
        binding.tvTitle.text = if (isEdit) "Edit Ulasan" else "Tulis Ulasan"
        binding.btnSubmit.text = if (isEdit) "Simpan Perubahan" else "Kirim Ulasan"

        existingReview?.let {
            binding.ratingBar.rating = it.rating.toFloat()
            binding.etComment.setText(it.comment ?: "")
        }

        binding.btnSubmit.setOnClickListener {
            val rating = binding.ratingBar.rating.toInt()
            val comment = binding.etComment.text.toString().trim()

            if (rating == 0) {
                Toast.makeText(context, "Berikan rating terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (comment.isEmpty()) {
                binding.tilComment.error = "Komentar tidak boleh kosong"
                return@setOnClickListener
            }
            binding.tilComment.error = null

            if (isEdit) updateReview(existingReview!!.id, rating, comment)
            else createReview(rating, comment)
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }

    private fun createReview(rating: Int, comment: String) {
        val token = SessionManager.getToken(requireContext()) ?: return
        setLoading(true)

        val body = mapOf(
            "rating" to rating.toString(),
            "comment" to comment
        )

        ApiClient.apiService.createReview("Bearer $token", packageId, body)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    setLoading(false)
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                        onReviewSaved?.invoke()
                        dismiss()
                    } else {
                        Toast.makeText(context, "Gagal mengirim ulasan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    setLoading(false)
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateReview(id: Int, rating: Int, comment: String) {
        val token = SessionManager.getToken(requireContext()) ?: return
        setLoading(true)

        val body = mapOf(
            "rating" to rating.toString(),
            "comment" to comment
        )

        ApiClient.apiService.updateReview("Bearer $token", id, body)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    setLoading(false)
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Ulasan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        onReviewSaved?.invoke()
                        dismiss()
                    } else {
                        Toast.makeText(context, "Gagal memperbarui ulasan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    setLoading(false)
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setLoading(loading: Boolean) {
        binding.btnSubmit.isEnabled = !loading
        binding.btnSubmit.text = if (loading) "Memproses..." else
            if (existingReview != null) "Simpan Perubahan" else "Kirim Ulasan"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}