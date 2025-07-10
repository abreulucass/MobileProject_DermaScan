package br.com.abreulucas.mobileproject2.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultas")
data class Consulta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timeStamp: String,
    val classname: String,
    val result: String,
    val confidence: Double,
    val imageUri: String
)