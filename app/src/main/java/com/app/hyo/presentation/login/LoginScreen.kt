package com.app.hyo.presentation.login // Changed from .register

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
import com.app.hyo.presentation.common.HyoButton
import com.app.hyo.presentation.common.HyoTextButton
import com.app.hyo.presentation.login.components.LoginPage

@Composable
fun LoginScreen( // Changed from RegisterScreen
    onLoginSuccessNavigation: () -> Unit, // For navigating after successful login
    onRegisterNavigateClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()// Changed from onLoginClick, assuming navigation to register
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.loginEvents.collect { event ->
            when (event) {
                is LoginEventState.Success -> {
                    Toast.makeText(context, "Login Successful! Welcome ${event.userName}", Toast.LENGTH_SHORT).show()
                    onLoginSuccessNavigation() // Navigate to dashboard
                }
                is LoginEventState.Error -> {
                    Toast.makeText(context, "Error: ${event.message}", Toast.LENGTH_LONG).show()
                }
                is LoginEventState.Loading -> {
                    // Optionally show a loading indicator
                    Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show()
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
        LoginPage(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it }
        )
        Spacer(modifier = Modifier.height(MediumPadding2))
        HyoButton( // Assuming TumbasButton is your generic button
            text = "Login",
            onClick = {
                viewModel.onEvent(LoginEvent.LoginUser(email, password))
            }
        )

        Spacer(modifier = Modifier.height(SmallPadding2))
        HyoTextButton(
            text = "Belum Punya Akun? Register", // Changed from "Sudah Punya Akun? Login"
            onClick = onRegisterNavigateClick // Changed from onLoginClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() { // Changed from RegisterScreenPreview
    LoginScreen( // Changed from RegisterScreen
        onLoginSuccessNavigation = {},
        onRegisterNavigateClick = {}
    )
}