package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProtectedAppEntity::class, SecurityLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VaultDatabase : RoomDatabase() {
    abstract fun protectedAppDao(): ProtectedAppDao
    abstract fun securityLogDao(): SecurityLogDao

    companion object {
        @Volatile
        private var INSTANCE: VaultDatabase? = null

        fun getInstance(context: Context): VaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VaultDatabase::class.java,
                    "vaulthide_vault.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
