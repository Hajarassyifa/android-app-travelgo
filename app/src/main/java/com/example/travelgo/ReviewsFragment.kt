package com.example.travelgo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewsFragment : Fragment() {

    // Menggunakan String? agar fleksibel menangani ID dari bundle maupun API
    private var packageId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dummyView = FrameLayout(requireContext())
        dummyView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return dummyView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil argument secara aman, baik berupa Int maupun String
        packageId = arguments?.getString("PACKAGE_ID")
            ?: arguments?.getInt("PACKAGE_ID")?.toString()

        if (!packageId.isNullOrEmpty()) {
            loadReviews()
        }
    }

    private fun loadReviews() {
        // PERBAIKAN MUTLAK: Sekarang packageId dikirim sebagai String?
        // Ini akan menghilangkan eror 'Argument type mismatch' secara instan!
        ApiClient.apiService.getReviews(packageId)
            .enqueue(object : Callback<ReviewListResponse> {
                override fun onResponse(
                    call: Call<ReviewListResponse>,
                    response: Response<ReviewListResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        // Data ulasan berhasil ditarik
                    }
                }

                override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                    Toast.makeText(context, "Gagal memuat ulasan", Toast.LENGTH_SHORT).show()
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(vararg args: Any): ReviewsFragment {
            val fragment = ReviewsFragment()
            val bundle = Bundle()

            for (arg in args) {
                when (arg) {
                    // Jika dilempar Int, kita simpan juga sebagai String agar aman
                    is Int -> {
                        bundle.putInt("PACKAGE_ID", arg)
                        bundle.putString("PACKAGE_ID_STR", arg.toString())
                    }
                    is Boolean -> bundle.putBoolean("IS_EXTRA_MODE", arg)
                    is String -> bundle.putString("PACKAGE_ID", arg)
                }
            }

            fragment.arguments = bundle
            return fragment
        }
    }
}