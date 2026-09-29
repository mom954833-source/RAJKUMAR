package com.example.data.repository

import com.example.data.local.AutoLockDuration
import com.example.data.local.ProtectedAppDao
import com.example.data.local.ProtectedAppEntity
import com.example.data.local.SecurityLogDao
import com.example.data.local.SecurityLogEntity
import com.example.data.local.SecurityStorage
import kotlinx.coroutines.flow.Flow

class VaultRepository(
    private val protectedAppDao: ProtectedAppDao,
    private val securityLogDao: SecurityLogDao,
    val securityStorage: SecurityStorage
) {
    val allProtectedApps: Flow<List<ProtectedAppEntity>> = protectedAppDao.getAllProtectedApps()
    val protectedCount: Flow<Int> = protectedAppDao.getProtectedAppCount()
    val recentLogs: Flow<List<SecurityLogEntity>> = securityLogDao.getRecentLogs()

    suspend fun protectApp(packageName: String, appName: String, category: String = "General") {
        protectedAppDao.insertApp(
            ProtectedAppEntity(
                packageName = packageName,
                appName = appName,
                category = category
            )
        )
        logSecurityEvent("APP_PROTECTED", "Added $appName to private vault", true)
    }

    suspend fun protectApps(apps: List<ProtectedAppEntity>) {
        protectedAppDao.insertApps(apps)
        logSecurityEvent("BATCH_PROTECTION", "Protected ${apps.size} apps in private vault", true)
    }

    suspend fun unprotectApp(packageName: String, appName: String) {
        protectedAppDao.removeApp(packageName)
        logSecurityEvent("APP_REMOVED", "Removed $appName from private vault", true)
    }

    suspend fun recordAppLaunch(packageName: String, appName: String) {
        protectedAppDao.incrementLaunch(packageName)
        logSecurityEvent("APP_LAUNCHED", "Launched protected app: $appName", true)
    }

    suspend fun logSecurityEvent(type: String, details: String, isSuccess: Boolean = true) {
        securityLogDao.insertLog(
            SecurityLogEntity(
                eventType = type,
                description = details,
                isSuccess = isSuccess
            )
        )
    }

    suspend fun clearLogs() {
        securityLogDao.clearLogs()
    }

    suspend fun resetVault() {
        protectedAppDao.clearAll()
        securityLogDao.clearLogs()
        securityStorage.resetAll()
    }
}
