package com.example.travelgo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.travelgo.databinding.ActivityReviewsBinding
import com.google.android.material.tabs.TabLayoutMediator

class ReviewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewsBinding

    // Optional: pass packageId via intent if coming from destination detail
    private var packageId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        packageId = intent.getIntExtra("package_id", -1)

        setupViewPager()
        binding.btnBack.setOnClickListener { finish() }

        // Show Add Review button only if packageId is provided
        if (packageId != -1) {
            binding.btnAddReview.visibility = View.VISIBLE
            binding.btnAddReview.setOnClickListener { showAddReviewDialog() }
        }
    }

    private fun setupViewPager() {
        val tabs = if (packageId != -1) {
            listOf("Semua Ulasan", "Ulasan Saya")
        } else {
            listOf("Ulasan Saya")
        }

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = tabs.size
            override fun createFragment(position: Int): Fragment {
                return when {
                    tabs[position] == "Semua Ulasan" -> ReviewsFragment.newInstance(packageId, false)
                    else -> ReviewsFragment.newInstance(packageId, true)
                }
            }
        }

        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun showAddReviewDialog() {
        val dialog = AddReviewDialogFragment.newInstance(packageId)
        dialog.show(supportFragmentManager, "AddReview")
    }
}