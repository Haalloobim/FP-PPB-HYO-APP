package com.app.hyo.presentation.login.components // Changed from .register.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.app.hyo.R
import com.app.hyo.presentation.Dimens.MediumPadding1
import com.app.hyo.presentation.Dimens.SmallPadding2
import com.app.hyo.presentation.common.HyoTextField
import com.app.hyo.ui.theme.HyoTheme

@Composable
fun LoginPage( // Changed from RegisterPage
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Login Akun", // Changed from "Buat Akun"
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.display_small),
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(MediumPadding1))
        HyoTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "Alamat Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = MediumPadding1)
        )

        Spacer(modifier = Modifier.height(SmallPadding2))
        HyoTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "Password",
            visualTransformation = PasswordVisualTransformation(),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = MediumPadding1)
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginPagePreview() { // Changed from RegistrationPreview
    HyoTheme {
        LoginPage( // Changed from RegisterPage
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        )
    }
}