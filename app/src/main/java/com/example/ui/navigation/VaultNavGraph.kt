package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AddAppScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LockScreen
import com.example.ui.screens.PrivacyScreen
import com.example.ui.screens.PrivateAppsScreen
import com.example.ui.screens.SecurityLogsScreen
import com.example.ui.screens.SecuritySettingsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SetupScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TermsScreen
import com.example.ui.screens.WebsitePreviewScreen
import com.example.ui.viewmodel.VaultViewModel

@Composable
fun VaultNavGraph(
    viewModel: VaultViewModel,
    navController: NavHostController = rememberNavController(),
    onBiometricPromptRequest: () -> Unit
) {
    val isVaultSetup by viewModel.isVaultSetup.collectAsStateWithLifecycle()
    val isUnlocked by viewModel.isUnlocked.collectAsStateWithLifecycle()
    val authMode by viewModel.authMode.collectAsStateWithLifecycle()
    val pinInput by viewModel.pinInput.collectAsStateWithLifecycle()
    val passwordInput by viewModel.passwordInput.collectAsStateWithLifecycle()
    val authError by viewModel.authError.collectAsStateWithLifecycle()
    val biometricEnabled by viewModel.biometricEnabled.collectAsStateWithLifecycle()

    val protectedApps by viewModel.protectedApps.collectAsStateWithLifecycle()
    val protectedCount by viewModel.protectedCount.collectAsStateWithLifecycle()
    val securityLogs by viewModel.securityLogs.collectAsStateWithLifecycle()

    val deviceApps by viewModel.deviceApps.collectAsStateWithLifecycle()
    val selectedPackages by viewModel.selectedPackages.collectAsStateWithLifecycle()
    val searchQuery by viewModel.appSearchQuery.collectAsStateWithLifecycle()
    val categoryFilter by viewModel.appCategoryFilter.collectAsStateWithLifecycle()
    val isLoadingApps by viewModel.isLoadingApps.collectAsStateWithLifecycle()

    val autoLockDuration by viewModel.autoLockDuration.collectAsStateWithLifecycle()
    val lockOnScreenOff by viewModel.lockOnScreenOff.collectAsStateWithLifecycle()
    val lockOnLeaveForeground by viewModel.lockOnLeaveForeground.collectAsStateWithLifecycle()
    val securityNotifications by viewModel.securityNotifications.collectAsStateWithLifecycle()

    // Automatic routing when lock/unlock state changes
    LaunchedEffect(isUnlocked, isVaultSetup) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (currentRoute != NavRoutes.SPLASH) {
            if (!isVaultSetup) {
                navController.navigate(NavRoutes.SETUP) {
                    popUpTo(0) { inclusive = true }
                }
            } else if (!isUnlocked) {
                navController.navigate(NavRoutes.LOCK) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        // Splash Screen
        composable(NavRoutes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    val destination = if (isVaultSetup) {
                        if (isUnlocked) NavRoutes.DASHBOARD else NavRoutes.LOCK
                    } else {
                        NavRoutes.SETUP
                    }
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Setup Screen (First run)
        composable(NavRoutes.SETUP) {
            SetupScreen(
                onSetupComplete = { pin, pwd, enableBio ->
                    viewModel.setupVault(pin, pwd, enableBio)
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.SETUP) { inclusive = true }
                    }
                }
            )
        }

        // Lock Screen
        composable(NavRoutes.LOCK) {
            LaunchedEffect(isUnlocked) {
                if (isUnlocked) {
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.LOCK) { inclusive = true }
                    }
                }
            }

            LockScreen(
                authMode = authMode,
                pinInput = pinInput,
                passwordInput = passwordInput,
                authError = authError,
                biometricEnabled = biometricEnabled,
                onAuthModeChange = { viewModel.setAuthMode(it) },
                onPinDigit = { viewModel.onPinDigit(it) },
                onPinDelete = { viewModel.onPinDelete() },
                onPasswordChange = { viewModel.onPasswordChange(it) },
                onPasswordSubmit = { viewModel.verifyAndUnlockPassword() },
                onBiometricClick = onBiometricPromptRequest
            )
        }

        // Main Dashboard
        composable(NavRoutes.DASHBOARD) {
            DashboardScreen(
                protectedCount = protectedCount,
                recentProtectedApps = protectedApps,
                onNavigateMyApps = { navController.navigate(NavRoutes.PRIVATE_APPS) },
                onNavigateAddApp = {
                    viewModel.loadDeviceApps()
                    navController.navigate(NavRoutes.ADD_APP)
                },
                onNavigateSecurity = { navController.navigate(NavRoutes.SECURITY_SETTINGS) },
                onNavigateSettings = { navController.navigate(NavRoutes.SETTINGS) },
                onNavigateWebsite = { navController.navigate(NavRoutes.WEBSITE) },
                onNavigateLogs = { navController.navigate(NavRoutes.SECURITY_LOGS) },
                onLockVault = {
                    viewModel.lockVault()
                    navController.navigate(NavRoutes.LOCK) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
                    }
                },
                onLaunchApp = { pkg, name ->
                    viewModel.launchAppFromVault(pkg, name)
                }
            )
        }

        // My Private Apps
        composable(NavRoutes.PRIVATE_APPS) {
            BackHandler {
                navController.popBackStack()
            }
            PrivateAppsScreen(
                protectedApps = protectedApps,
                onBack = { navController.popBackStack() },
                onNavigateAddApp = {
                    viewModel.loadDeviceApps()
                    navController.navigate(NavRoutes.ADD_APP)
                },
                onOpenApp = { pkg, name ->
                    viewModel.launchAppFromVault(pkg, name)
                },
                onRemoveApp = { pkg, name ->
                    viewModel.unprotectApp(pkg, name)
                }
            )
        }

        // Add Private App
        composable(NavRoutes.ADD_APP) {
            BackHandler {
                navController.popBackStack()
            }
            val alreadyProtected = protectedApps.map { it.packageName }.toSet()

            AddAppScreen(
                deviceApps = deviceApps,
                selectedPackages = selectedPackages,
                alreadyProtectedPackages = alreadyProtected,
                searchQuery = searchQuery,
                categoryFilter = categoryFilter,
                isLoading = isLoadingApps,
                onBack = { navController.popBackStack() },
                onSearchChange = { viewModel.setSearchQuery(it) },
                onCategoryChange = { viewModel.setCategoryFilter(it) },
                onToggleSelect = { viewModel.toggleAppSelection(it) },
                onSaveToVault = {
                    viewModel.saveSelectedAppsToVault()
                    navController.popBackStack()
                }
            )
        }

        // Security Settings
        composable(NavRoutes.SECURITY_SETTINGS) {
            BackHandler {
                navController.popBackStack()
            }
            SecuritySettingsScreen(
                autoLockDuration = autoLockDuration,
                biometricEnabled = biometricEnabled,
                lockOnScreenOff = lockOnScreenOff,
                lockOnLeaveForeground = lockOnLeaveForeground,
                securityNotifications = securityNotifications,
                onBack = { navController.popBackStack() },
                onUpdateAutoLock = { viewModel.updateAutoLock(it) },
                onUpdateBiometric = { viewModel.updateBiometric(it) },
                onUpdateLockScreenOff = { viewModel.updateLockOnScreenOff(it) },
                onUpdateLockLeaveForeground = { viewModel.updateLockOnLeaveForeground(it) },
                onUpdateSecurityNotifications = { viewModel.updateSecurityNotifications(it) },
                onChangePin = { cur, newPin -> viewModel.changePin(cur, newPin) },
                onChangePassword = { cur, newPwd -> viewModel.changePassword(cur, newPwd) },
                onNavigateLogs = { navController.navigate(NavRoutes.SECURITY_LOGS) },
                onResetVault = {
                    viewModel.resetVault()
                    navController.navigate(NavRoutes.SETUP) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Settings Screen
        composable(NavRoutes.SETTINGS) {
            BackHandler {
                navController.popBackStack()
            }
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateSecurity = { navController.navigate(NavRoutes.SECURITY_SETTINGS) },
                onNavigateSecurityLogs = { navController.navigate(NavRoutes.SECURITY_LOGS) },
                onNavigateAbout = { navController.navigate(NavRoutes.ABOUT) },
                onNavigatePrivacy = { navController.navigate(NavRoutes.PRIVACY) },
                onNavigateTerms = { navController.navigate(NavRoutes.TERMS) },
                onNavigateWebsite = { navController.navigate(NavRoutes.WEBSITE) }
            )
        }

        // Security Logs Screen (Room Database Audit Trail)
        composable(NavRoutes.SECURITY_LOGS) {
            BackHandler {
                navController.popBackStack()
            }
            SecurityLogsScreen(
                logs = securityLogs,
                onBack = { navController.popBackStack() },
                onClearLogs = { viewModel.clearSecurityLogs() }
            )
        }

        // About Screen
        composable(NavRoutes.ABOUT) {
            BackHandler {
                navController.popBackStack()
            }
            AboutScreen(onBack = { navController.popBackStack() })
        }

        // Privacy Policy Screen
        composable(NavRoutes.PRIVACY) {
            BackHandler {
                navController.popBackStack()
            }
            PrivacyScreen(onBack = { navController.popBackStack() })
        }

        // Terms of Service Screen
        composable(NavRoutes.TERMS) {
            BackHandler {
                navController.popBackStack()
            }
            TermsScreen(onBack = { navController.popBackStack() })
        }

        // Website Preview Screen
        composable(NavRoutes.WEBSITE) {
            BackHandler {
                navController.popBackStack()
            }
            WebsitePreviewScreen(onBack = { navController.popBackStack() })
        }
    }
}
