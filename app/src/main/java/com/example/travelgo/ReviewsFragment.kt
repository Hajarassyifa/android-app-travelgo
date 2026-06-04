package com.example.travelgo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelgo.databinding.FragmentReviewsListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private val reviews = mutableListOf<Review>()

    private var packageId: Int = -1
    private var isMyReviews: Boolean = false

    companion object {
        fun newInstance(packageId: Int, isMyReviews: Boolean) = ReviewsFragment().apply {
            arguments = Bundle().apply {
                putInt("package_id", packageId)
                putBoolean("is_my_reviews", isMyReviews)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageId = arguments?.getInt("package_id", -1) ?: -1
        isMyReviews = arguments?.getBoolean("is_my_reviews", false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadReviews()

        if (!isMyReviews && packageId != -1) {
            binding.cardRatingSummary.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        adapter = ReviewAdapter(
            reviews,
            showActions = isMyReviews,
            onEdit = { review -> showEditDialog(review) },
            onDelete = { review -> confirmDelete(review) }
        )
        binding.rvReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReviews.adapter = adapter
    }

    private fun loadReviews() {
        showLoading(true)
        val token = SessionManager.getToken(requireContext())

        if (isMyReviews) {
            if (token == null) { showLoading(false); return }
            ApiClient.apiService.getMyReviews("Bearer $token")
                .enqueue(object : Callback<ReviewListResponse> {
                    override fun onResponse(call: Call<ReviewListResponse>, response: Response<ReviewListResponse>) {
                        showLoading(false)
                        handleResponse(response.body()?.data)
                    }
                    override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(context, "Gagal memuat ulasan", Toast.LENGTH_SHORT).show()
                    }
                })
        } else if (packageId != -1) {
            ApiClient.apiService.getReviews(packageId)
                .enqueue(object : Callback<ReviewListResponse> {
                    override fun onResponse(call: Call<ReviewListResponse>, response: Response<ReviewListResponse>) {
                        showLoading(false)
                        handleResponse(response.body()?.data)
                        response.body()?.data?.let { updateRatingSummary(it) }
                    }
                    override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(context, "Gagal memuat ulasan", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun handleResponse(data: List<Review>?) {
        val list = data ?: emptyList()
        reviews.clear()
        reviews.addAll(list)
        adapter.notifyDataSetChanged()

        if (list.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.tvEmptyMessage.text = if (isMyReviews) "Kamu belum membuat ulasan" else "Belum ada ulasan"
            binding.rvReviews.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvReviews.visibility = View.VISIBLE
        }
    }

    private fun updateRatingSummary(data: List<Review>) {
        if (data.isEmpty()) return
        val avg = data.map { it.rating }.average()
        binding.tvAvgRating.text = String.format("%.1f", avg)
        binding.ratingBarAvg.rating = avg.toFloat()
        binding.tvTotalReviews.text = "${data.size} ulasan"

        val counts = IntArray(6)
        data.forEach { counts[it.rating]++ }
        val max = data.size.coerceAtLeast(1)

        binding.pb5.progress = counts[5] * 100 / max; binding.tv5Count.text = counts[5].toString()
        binding.pb4.progress = counts[4] * 100 / max; binding.tv4Count.text = counts[4].toString()
        binding.pb3.progress = counts[3] * 100 / max; binding.tv3Count.text = counts[3].toString()
        binding.pb2.progress = counts[2] * 100 / max; binding.tv2Count.text = counts[2].toString()
        binding.pb1.progress = counts[1] * 100 / max; binding.tv1Count.text = counts[1].toString()
    }

    private fun showEditDialog(review: Review) {
        val dialog = AddReviewDialogFragment.newInstance(packageId, review)
        dialog.show(parentFragmentManager, "EditReview")
        dialog.onReviewSaved = { loadReviews() }
    }

    private fun confirmDelete(review: Review) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Ulasan")
            .setMessage("Yakin ingin menghapus ulasan ini?")
            .setPositiveButton("Hapus") { _, _ -> deleteReview(review.id) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteReview(id: Int) {
        val token = SessionManager.getToken(requireContext()) ?: return
        ApiClient.apiService.deleteReview("Bearer $token", id)
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Ulasan dihapus", Toast.LENGTH_SHORT).show()
                        loadReviews()
                    }
                }
                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {}
            })
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}