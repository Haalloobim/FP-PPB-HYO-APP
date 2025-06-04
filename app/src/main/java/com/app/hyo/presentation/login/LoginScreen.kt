package com.app.hyo.presentation.login // Changed from .register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme // Assuming MaterialTheme is used generally
import androidx.compose.material3.Text // Assuming Text is used generally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource // Assuming colorResource is used generally
import androidx.compose.ui.text.font.FontWeight // Assuming FontWeight is used generally
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.hyo.presentation.Dimens.MediumPadding2
import com.app.hyo.presentation.Dimens.SmallPadding2
import com.app.hyo.presentation.login.components.LoginPage // Changed from .register.components.RegisterPage
import com.app.hyo.R
import com.app.hyo.presentation.common.HyoButton // Changed from HyoRegisterButton - You might need to create this or reuse a generic button
import com.app.hyo.presentation.common.HyoTextButton

@Composable
fun LoginScreen( // Changed from RegisterScreen
    onLoginClick: () -> Unit, // Changed from onRegisterClick
    onRegisterNavigateClick: () -> Unit // Changed from onLoginClick, assuming navigation to register
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SmallPadding2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginPage( // Changed from RegisterPage
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it }
        )
        Spacer(modifier = Modifier.height(MediumPadding2))
        HyoButton( // Changed from HyoRegisterButton
            text = "Login", // Changed from "Register"
            onClick = {
                // Here you would typically call something like:
                // viewModel.onEvent(LoginEvent.LoginUser(email, password))
                // For now, just invoking the passed lambda
                onLoginClick()
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
        onLoginClick = {},
        onRegisterNavigateClick = {}
    )
}