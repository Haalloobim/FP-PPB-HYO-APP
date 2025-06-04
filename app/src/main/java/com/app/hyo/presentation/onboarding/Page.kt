package com.app.hyo.presentation.onboarding

import androidx.annotation.DrawableRes

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "Selamat Datang di Hyo",
        description = "Hyo adalah aplikasi untuk memantau tumbuh kembang anak dan mendeteksi risiko stunting sejak dini.",
        image = com.app.hyo.R.drawable.on1
    ),
    Page(
        title = "Pantau Perkembangan Anak",
        description = "Catat tinggi badan, berat badan, dan perkembangan anak secara rutin untuk mengetahui status gizinya.",
        image = com.app.hyo.R.drawable.on2
    ),
    Page(
        title = "Dapatkan Edukasi dan Tips",
        description = "Akses informasi dan saran dari ahli gizi untuk mendukung pertumbuhan anak yang optimal.",
        image = com.app.hyo.R.drawable.on3
    )
)
