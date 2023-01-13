package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalUser (
    @PrimaryKey val uid : String,
    @ColumnInfo(name = "display_name") val displayName : String?,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "desc") val desc : String? = "",
    @ColumnInfo(name = "image_url") val imageUrl : String? = ""
)
