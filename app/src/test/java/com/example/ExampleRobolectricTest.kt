package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.SecurityLogEntity
import com.example.data.local.SecurityStorage
import com.example.data.local.VaultDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("VaultHide", appName)
  }

  @Test
  fun `verify pin security hashing and verification`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val securityStorage = SecurityStorage(context)
    securityStorage.setupVault(pin = "1234", password = "masterpassword", enableBiometric = true)

    assertTrue(securityStorage.isVaultSetup())
    assertTrue(securityStorage.verifyPin("1234"))
    assertFalse(securityStorage.verifyPin("9999"))
  }

  @Test
  fun `verify Room database security logs recording and query`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = VaultDatabase.getInstance(context)
    val dao = db.securityLogDao()

    dao.clearLogs()

    dao.insertLog(
      SecurityLogEntity(
        eventType = "VAULT_UNLOCKED",
        description = "Unlocked using master PIN",
        isSuccess = true
      )
    )
    dao.insertLog(
      SecurityLogEntity(
        eventType = "FAILED_ATTEMPT",
        description = "Failed PIN attempt",
        isSuccess = false
      )
    )

    val logs = dao.getRecentLogs().first()
    assertEquals(2, logs.size)
    assertTrue(logs.any { it.eventType == "VAULT_UNLOCKED" && it.isSuccess })
    assertTrue(logs.any { it.eventType == "FAILED_ATTEMPT" && !it.isSuccess })
  }
}
