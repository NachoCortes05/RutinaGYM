package com.example.rutinagym

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ejercicios",
    indices = [Index(value = ["apiId"], unique = true)]
)
data class Ejercicio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val apiId: String? = null,
    val nombre: String,
    val repeticiones: Int,
    val tipo: String,
    val imagenUrl: String? = null,
    val fotoPath: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
)