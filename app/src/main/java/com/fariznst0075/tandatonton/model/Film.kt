package com.fariznst0075.tandatonton.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film")
data class Film(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val jenis: String,
    val status: String,
    val tanggal: String,
)