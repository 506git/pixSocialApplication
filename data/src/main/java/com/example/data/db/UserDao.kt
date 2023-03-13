package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.data.model.LocalUser

@Dao
interface UserDao {
    @Query("SELECT * FROM local_user")
    fun getAll() : LocalUser
//
//    @Query("SELECT * FROM localUser WHERE ")
//    fun getUserImage() : String

    @Insert(onConflict = REPLACE)
    fun insertUser(users: LocalUser)

    @Delete
    fun delete(localUser: LocalUser)

}