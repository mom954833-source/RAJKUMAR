package com.example.data.model

import android.graphics.drawable.Drawable

data class InstalledAppInfo(
    val packageName: String,
    val appName: String,
    val category: String,
    val iconDrawable: Drawable? = null,
    val isProtected: Boolean = false,
    val isSelected: Boolean = false
)
