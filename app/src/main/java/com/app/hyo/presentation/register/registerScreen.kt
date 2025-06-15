package com.app.hyo.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hyo.presentation.Dimens.MediumPadding2
import com.app.hyo.presentation.Dimens.SmallPadding2
import com.app.hyo.presentation.common.HyoRegisterButton
import com.app.hyo.presentation.common.HyoTextButton
import com.app.hyo.presentation.register.components.RegisterPage

@Composable
fun RegisterScreen(
    onRegisterSuccessNavigation: () -> Unit, // For navigating after successful registration
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var telepon by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // Keep for UI, validation should be in VM or here

    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.registrationEvents.collect { event ->
            when (event) {
                is RegistrationEventState.Success -> {
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    onRegisterSuccessNavigation() // Navigate to login or dashboard
                }
                is RegistrationEventState.Error -> {
                    Toast.makeText(context, "Error: ${event.message}", Toast.LENGTH_LONG).show()
                }
                is RegistrationEventState.Loading -> {
                    // Optionally show a loading indicator
                    Toast.makeText(context, "Registering...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SmallPadding2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterPage(
            name = name,
            onNameChange = { name = it },
            telepon = telepon,
            onTeleponChange = { telepon = it },
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            confirmPassword = confirmPassword,
            onConfirmPasswordChange = { confirmPassword = it }
        )
        Spacer(modifier = Modifier.height(MediumPadding2))

        HyoRegisterButton( // Assuming this is a Composable from your common components
            text = "Register",
            onClick = {
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                    return@HyoRegisterButton
                }
                viewModel.onEvent(RegisterEvent.RegisterUser(email, password), name, telepon)
            }
        )

        Spacer(modifier = Modifier.height(SmallPadding2))
        HyoTextButton(
            text = "Sudah Punya Akun? Login",
            onClick = onLoginClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegisterSuccessNavigation = {},
        onLoginClick = {}
    )
}