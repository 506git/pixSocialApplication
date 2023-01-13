package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.LocalUser

@Database(entities = [LocalUser::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}