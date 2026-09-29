package com.example.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.data.model.InstalledAppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppDiscoveryRepository(private val context: Context) {

    suspend fun getInstalledLauncherApps(): List<InstalledAppInfo> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val discovered = mutableListOf<InstalledAppInfo>()
        val seenPackages = mutableSetOf<String>()

        try {
            val activities = pm.queryIntentActivities(launcherIntent, 0)
            for (resolveInfo in activities) {
                val pkgName = resolveInfo.activityInfo.packageName
                if (pkgName == context.packageName || seenPackages.contains(pkgName)) continue
                seenPackages.add(pkgName)

                val label = resolveInfo.loadLabel(pm).toString()
                val icon = try {
                    resolveInfo.loadIcon(pm)
                } catch (e: Exception) {
                    null
                }

                val category = guessCategory(pkgName, label)
                discovered.add(
                    InstalledAppInfo(
                        packageName = pkgName,
                        appName = label,
                        category = category,
                        iconDrawable = icon
                    )
                )
            }
        } catch (e: Exception) {
            // Graceful fallback
        }

        // Add common privacy-sensitive apps if not found on device (so user can test vault features)
        val defaultPrivacyApps = listOf(
            InstalledAppInfo("com.whatsapp", "WhatsApp", "Messaging"),
            InstalledAppInfo("com.instagram.android", "Instagram", "Social"),
            InstalledAppInfo("org.telegram.messenger", "Telegram", "Messaging"),
            InstalledAppInfo("com.facebook.katana", "Facebook", "Social"),
            InstalledAppInfo("com.facebook.orca", "Messenger", "Messaging"),
            InstalledAppInfo("com.snapchat.android", "Snapchat", "Social"),
            InstalledAppInfo("com.google.android.apps.photos", "Google Photos", "Media"),
            InstalledAppInfo("com.google.android.gm", "Gmail", "Communication"),
            InstalledAppInfo("com.twitter.android", "X (Twitter)", "Social"),
            InstalledAppInfo("com.google.android.keep", "Google Keep Notes", "Productivity")
        )

        for (defaultApp in defaultPrivacyApps) {
            if (!seenPackages.contains(defaultApp.packageName)) {
                discovered.add(defaultApp)
            }
        }

        discovered.sortedBy { it.appName.lowercase() }
    }

    private fun guessCategory(packageName: String, label: String): String {
        val lowerPkg = packageName.lowercase()
        val lowerLabel = label.lowercase()
        return when {
            lowerPkg.contains("whatsapp") || lowerPkg.contains("telegram") || lowerPkg.contains("signal") ||
                    lowerPkg.contains("messaging") || lowerLabel.contains("message") -> "Messaging"
            lowerPkg.contains("instagram") || lowerPkg.contains("facebook") || lowerPkg.contains("snapchat") ||
                    lowerPkg.contains("twitter") || lowerPkg.contains("tiktok") -> "Social"
            lowerPkg.contains("bank") || lowerPkg.contains("pay") || lowerPkg.contains("wallet") -> "Finance"
            lowerPkg.contains("gallery") || lowerPkg.contains("photo") || lowerPkg.contains("camera") -> "Media"
            else -> "General"
        }
    }

    fun launchApp(packageName: String): Boolean {
        return try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
