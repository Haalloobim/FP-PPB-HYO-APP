package com.app.hyo.presentation.onboarding

import androidx.annotation.DrawableRes

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "Selamat Datang di HYO Application",
        description = "HYO adalah aplikasi untuk platform pembelajaran yang membantu kerabat sobat tuna rungu di Indonesia",
        image = com.app.hyo.R.drawable.on2
    ),
    Page(
        title = "Deteksi Bahasa Isyarat Indonesia",
        description = "Fitur deteksi bahasa isyarat Indonesia yang membantu kerabat sobat tuna rungu berkomunikasi dengan lebih mudah.",
        image = com.app.hyo.R.drawable.on3
    ),
    Page(
        title = "Dapatkan Edukasi dan Tips",
        description = "Akses informasi dan tips untuk meningkatkan pemahaman tentang dunia tuna rungu.",
        image = com.app.hyo.R.drawable.on4
    )
)