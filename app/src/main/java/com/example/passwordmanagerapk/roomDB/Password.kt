package com.example.passwordmanagerapk.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "password_table")
data class Password(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val accountType: String,
    val username: String,
    val password: String
)
