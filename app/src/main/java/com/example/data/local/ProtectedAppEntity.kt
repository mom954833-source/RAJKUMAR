package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "protected_apps")
data class ProtectedAppEntity(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val isProtected: Boolean = true,
    val protectedAt: Long = System.currentTimeMillis(),
    val launchCount: Int = 0,
    val lastUnlockedAt: Long = 0L,
    val category: String = "General"
)
