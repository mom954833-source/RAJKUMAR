package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlowingShieldIcon
import com.example.ui.components.PinDotsIndicator
import com.example.ui.components.PinPad
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import com.example.ui.theme.VaultSurface
import com.example.ui.viewmodel.AuthMode

@Composable
fun LockScreen(
    authMode: AuthMode,
    pinInput: String,
    passwordInput: String,
    authError: String?,
    biometricEnabled: Boolean,
    onAuthModeChange: (AuthMode) -> Unit,
    onPinDigit: (String) -> Unit,
    onPinDelete: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordSubmit: () -> Unit,
    onBiometricClick: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("lock_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlowingShieldIcon(size = 72.dp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔐 VaultHide",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                modifier = Modifier.testTag("lock_screen_title")
            )

            Text(
                text = "Private App Vault",
                color = ElectricBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Text explicitly requested in Hindi
            Text(
                text = "अपनी Private Apps को सुरक्षित रखें",
                color = GoldenYellow,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("lock_screen_hindi_subtitle")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Auth Mode Toggle (PIN vs Password)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFF141B2D))
                    .border(1.dp, Color(0xFF263353), RoundedCornerShape(30.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (authMode == AuthMode.PIN) ElectricBlue else Color.Transparent)
                        .clickable { onAuthModeChange(AuthMode.PIN) }
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .testTag("toggle_pin_mode"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "PIN",
                        color = if (authMode == AuthMode.PIN) Color.White else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (authMode == AuthMode.PASSWORD) CyberPurple else Color.Transparent)
                        .clickable { onAuthModeChange(AuthMode.PASSWORD) }
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .testTag("toggle_password_mode"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Password",
                        color = if (authMode == AuthMode.PASSWORD) Color.White else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(visible = authError != null) {
                Text(
                    text = authError ?: "",
                    color = CrimsonDanger,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("auth_error_message")
                )
            }

            if (authMode == AuthMode.PIN) {
                PinDotsIndicator(
                    pinLength = pinInput.length,
                    maxDigits = 4,
                    hasError = authError != null
                )

                PinPad(
                    onDigitClick = onPinDigit,
                    onDeleteClick = onPinDelete,
                    onBiometricClick = onBiometricClick,
                    showBiometric = biometricEnabled
                )
            } else {
                // Password Mode
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = onPasswordChange,
                        label = { Text("Enter Master Password") },
                        leadingIcon = { Icon(Icons.Default.Password, null, tint = CyberPurple) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    "Toggle password visibility"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { onPasswordSubmit() }),
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberPurple,
                            unfocusedBorderColor = Color(0xFF263353),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input_field")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onPasswordSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("unlock_vault_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberPurple)
                    ) {
                        Text(
                            text = "Unlock Vault",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Unlock Vault & Use Fingerprint
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (authMode == AuthMode.PIN) {
                    Button(
                        onClick = {
                            if (pinInput.length == 4) {
                                onPinDigit("")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("unlock_vault_pin_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        enabled = pinInput.length == 4
                    ) {
                        Text(
                            text = "Unlock Vault",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }

                if (biometricEnabled) {
                    OutlinedButton(
                        onClick = onBiometricClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("use_fingerprint_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = GoldenYellow
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldenYellow.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "Use Fingerprint",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            FounderBadge(detailed = false)

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
