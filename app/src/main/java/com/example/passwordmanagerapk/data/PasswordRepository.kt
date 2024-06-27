package com.example.passwordmanagerapk.data

import com.example.passwordmanagerapk.roomDB.Password
import com.example.passwordmanagerapk.roomDB.PasswordDao
import kotlinx.coroutines.flow.Flow

class PasswordRepository(private val passwordDao: PasswordDao) {
    val allPasswords: Flow<List<Password>> = passwordDao.getAllPasswords()

    suspend fun insert(password: Password) {
        passwordDao.insert(password)
    }

    suspend fun update(password: Password) {
        passwordDao.update(password)
    }

    suspend fun delete(password: Password) {
        passwordDao.delete(password)
    }
}