package com.example.passwordmanagerapk._Ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.passwordmanagerapk.data.PasswordViewModel
import com.example.passwordmanagerapk.roomDB.Password
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: PasswordViewModel) {
    val passwords by viewModel.allPasswords.observeAsState(emptyList())
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var showAddSheet by remember { mutableStateOf(false) }
    var selectedPassword: Password? by remember { mutableStateOf(null) }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            when {
                showAddSheet -> {
                    AddPasswordScreen(navController, viewModel) {
                        coroutineScope.launch {
                            try {
                                bottomSheetState.hide()
                            } catch (e: Exception) {
                                println("Something went wrong $e")
                            }
                        }
                    }
                }
                selectedPassword != null -> {
                    EditPasswordDetailsScreen(selectedPassword!!, navController, viewModel) {
                        coroutineScope.launch {
                            try {
                                bottomSheetState.hide()
                            } catch (e: Exception) {
                                println("Something went wrong $e")
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showAddSheet = true
                        selectedPassword = null // Ensure the selected password is reset
                        coroutineScope.launch {
                            try {
                                bottomSheetState.show()
                            } catch (e: Exception) {
                                println("Something went wrong $e")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFF2196F3),
                    modifier = Modifier
                        .height(76.dp)
                        .width(76.dp)
                        .padding(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Password", tint = Color.White)
                }
            },
            scaffoldState = scaffoldState,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Password Manager",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    LazyColumn {
                        items(passwords.size) { index ->
                            val password = passwords[index]
                            PasswordItem(password) {
                                selectedPassword = password
                                showAddSheet = false
                                coroutineScope.launch {
                                    try {
                                        bottomSheetState.show()
                                    } catch (e: Exception) {
                                        println("Something went wrong $e")                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun PasswordItem(password: Password, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFFFFFFF)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = password.accountType, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(text = "******", modifier = Modifier.padding(start = 8.dp, top = 4.dp).weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
