package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class GetStartedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref =
            getSharedPreferences("USER_SESSION", MODE_PRIVATE)

        val isLogin =
            sharedPref.getBoolean("IS_LOGIN", false)

        // Kalau sudah login → langsung ke MainActivity
        if (isLogin) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_get_started)

        val viewPager =
            findViewById<ViewPager2>(R.id.viewPager)

        val btnMulai =
            findViewById<Button>(R.id.btnMulai)

        val images = listOf(
            R.drawable.img_onboarding1,
            R.drawable.img_onboarding2,
            R.drawable.img_onboarding3
        )

        val adapter = OnboardingAdapter(images)
        viewPager.adapter = adapter

        btnMulai.setOnClickListener {

            val intent =
                Intent(this, LoginActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}