package com.example.passwordmanagerapk._Ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.passwordmanagerapk.data.PasswordViewModel
import com.example.passwordmanagerapk.roomDB.Password
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditPasswordDetailsScreen(password: Password, navController: NavHostController, viewModel: PasswordViewModel, onClose: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var accountType by remember { mutableStateOf(password.accountType) }
    var username by remember { mutableStateOf(password.username) }
    var passwordText by remember { mutableStateOf(password.password) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var passwordLengthError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Reset the states when the composable is first launched
    LaunchedEffect(password) {
        isEditing = false
        isPasswordVisible = false
        accountType = password.accountType
        username = password.username
        passwordText = password.password
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = if (isEditing) "Edit Account" else "Account Details",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = accountType,
            onValueChange = { accountType = it },
            label = { Text("Account Type") },
            readOnly = !isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username/Email") },
            readOnly = !isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = passwordText,
            onValueChange = {
                passwordText = it
                passwordLengthError = false
            },
            label = { Text("Password") },
            readOnly = !isEditing,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (isEditing) {
                    val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        Icon(imageVector = image, contentDescription = if (isPasswordVisible) "Hide password" else "Show password")
                    }
                }
            },
            isError = passwordLengthError,
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordLengthError) {
            Text(text = "Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isEditing) {
            Button(
                onClick = {
                    if (passwordText.length < 6) {
                        passwordLengthError = true
                    } else {
                        coroutineScope.launch {
                            try {
                                viewModel.updatePassword(
                                    password.copy(
                                        accountType = accountType,
                                        username = username,
                                        password = passwordText
                                    )
                                )
                                isEditing = false
                                onClose()
                                isPasswordVisible = false
                            } catch (e: Exception) {
                                println("Something went wrong $e")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
            ) {
                Text("Save Changes", color = Color.White)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text("Edit", color = Color.White)
                }
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                viewModel.deletePassword(password)
                                onClose()
                            } catch (e: Exception) {
                                println("Something went wrong $e")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Delete", color = Color.White)
                }
            }
        }
    }
}
