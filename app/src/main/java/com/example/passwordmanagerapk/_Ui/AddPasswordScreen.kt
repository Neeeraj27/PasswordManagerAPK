package com.example.passwordmanagerapk._Ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.passwordmanagerapk.data.PasswordViewModel
import com.example.passwordmanagerapk.roomDB.Password
import kotlinx.coroutines.launch


@Composable
fun AddPasswordScreen(navController: NavHostController, viewModel: PasswordViewModel, onClose: () -> Unit) {
    var accountType by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var accountTypeError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordLengthError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Add New Account", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = accountType,
            onValueChange = {
                accountType = it
                accountTypeError = false
            },
            label = { Text("Account Name") },
            isError = accountTypeError,
            modifier = Modifier.fillMaxWidth()
        )
        if (accountTypeError) {
            Text(text = "Account Name is required", color = Color.Red, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = false
            },
            label = { Text("Username/Email") },
            isError = usernameError,
            modifier = Modifier.fillMaxWidth()
        )
        if (usernameError) {
            Text(text = "Username/Email is required", color = Color.Red, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = passwordText,
            onValueChange = {
                passwordText = it
                passwordError = false
                passwordLengthError = false
            },
            label = { Text("Password") },
            isError = passwordError || passwordLengthError,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Icon(imageVector = image, contentDescription = if (isPasswordVisible) "Hide password" else "Show password")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError) {
            Text(text = "Password is required", color = Color.Red, fontSize = 12.sp)
        }
        if (passwordLengthError) {
            Text(text = "Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                var hasError = false
                if (accountType.isEmpty()) {
                    accountTypeError = true
                    hasError = true
                }
                if (username.isEmpty()) {
                    usernameError = true
                    hasError = true
                }
                if (passwordText.isEmpty()) {
                    passwordError = true
                    hasError = true
                } else if (passwordText.length < 6) {
                    passwordLengthError = true
                    hasError = true
                }
                if (!hasError) {
                    coroutineScope.launch {
                        try {
                            viewModel.addPassword(
                                Password(
                                    accountType = accountType,
                                    username = username,
                                    password = passwordText
                                )
                            )
                            onClose()
                        } catch (e: Exception) {
                            println("Something went wrong $e")
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text("Add New Account", color = Color.White)
        }
    }
}
