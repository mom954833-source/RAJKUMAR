package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtectedAppDao {
    @Query("SELECT * FROM protected_apps ORDER BY protectedAt DESC")
    fun getAllProtectedApps(): Flow<List<ProtectedAppEntity>>

    @Query("SELECT * FROM protected_apps WHERE packageName = :packageName LIMIT 1")
    suspend fun getApp(packageName: String): ProtectedAppEntity?

    @Query("SELECT COUNT(*) FROM protected_apps")
    fun getProtectedAppCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: ProtectedAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<ProtectedAppEntity>)

    @Query("DELETE FROM protected_apps WHERE packageName = :packageName")
    suspend fun removeApp(packageName: String)

    @Query("UPDATE protected_apps SET launchCount = launchCount + 1, lastUnlockedAt = :time WHERE packageName = :packageName")
    suspend fun incrementLaunch(packageName: String, time: Long = System.currentTimeMillis())

    @Query("DELETE FROM protected_apps")
    suspend fun clearAll()
}
