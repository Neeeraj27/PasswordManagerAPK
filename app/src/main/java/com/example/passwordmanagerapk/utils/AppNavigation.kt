package com.example.warehousetracebilityandroid.components



import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanagerapk._Ui.HomeScreen
import com.example.passwordmanagerapk.data.PasswordRepository
import com.example.passwordmanagerapk.data.PasswordViewModel
import com.example.passwordmanagerapk.data.PasswordViewModelFactory
import com.example.passwordmanagerapk.roomDB.PasswordDatabase
import com.example.warehousetracebilityandroid.utils.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@Composable
fun AppNavigation(
    navController: NavHostController,
)
{
    val context = LocalContext.current
    val navController = rememberNavController()

    val database = PasswordDatabase.getDatabase(context, CoroutineScope(Dispatchers.IO))
    val repository = PasswordRepository(database.passwordDao())
    val viewModel: PasswordViewModel = viewModel(factory = PasswordViewModelFactory(repository))



    NavHost(navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController ,viewModel)
        }
    }
}