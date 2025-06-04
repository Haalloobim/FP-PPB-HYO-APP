package com.app.hyo.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize // Changed
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment // Changed
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.hyo.presentation.Dimens.MediumPadding2
import com.app.hyo.presentation.Dimens.SmallPadding2
import com.app.hyo.presentation.register.components.RegisterPage
import com.app.hyo.presentation.common.HyoRegisterButton
import com.app.hyo.presentation.common.HyoTextButton

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var telepon by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
        HyoRegisterButton(
            text = "Register",
            onClick = onRegisterClick
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
        onRegisterClick = {},
        onLoginClick = {}
    )
}