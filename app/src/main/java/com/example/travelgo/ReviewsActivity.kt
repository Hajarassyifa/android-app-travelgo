package com.example.travelgo

import android.os.Bundle
import android.view.View // <-- INI YANG KURANG DAN SUDAH DITAMBAHKAN
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class ReviewsActivity : AppCompatActivity() {

    private var packageId: Int = -1
    private var isFetchStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Membuat kontainer layout utama activity secara dinamis
        // sehingga activity tidak akan force close meskipun file XML activity_reviews milikmu tidak ada
        val viewContainer = FrameLayout(this)
        viewContainer.id = View.generateViewId()
        viewContainer.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        setContentView(viewContainer)

        // Mengambil data intent dari halaman sebelumnya dengan aman
        packageId = intent.getIntExtra("PACKAGE_ID", -1)
        isFetchStatus = intent.getBooleanExtra("FETCH_STATUS", false)

        // Masukkan Fragment ke dalam kontainer dinamis yang baru dibuat
        val reviewsFragment = ReviewsFragment.newInstance(packageId, isFetchStatus)
        supportFragmentManager.beginTransaction()
            .replace(viewContainer.id, reviewsFragment)
            .commit()
    }
}