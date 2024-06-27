package com.example.passwordmanagerapk.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.passwordmanagerapk.roomDB.Password
import kotlinx.coroutines.launch


class PasswordViewModel(private val repository: PasswordRepository) : ViewModel() {
    val allPasswords: LiveData<List<Password>> = repository.allPasswords.asLiveData()

    fun addPassword(password: Password) = viewModelScope.launch {
        repository.insert(password)
    }

    fun updatePassword(password: Password) = viewModelScope.launch {
        repository.update(password)
    }

    fun deletePassword(password: Password) = viewModelScope.launch {
        repository.delete(password)
    }
}

class PasswordViewModelFactory(private val repository: PasswordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}