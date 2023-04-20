package com.example.roomproject

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DaoEstudiante {
    @Query("SELECT * FROM estudiante")
    suspend fun obtenerEstudiante(): MutableList<Estudiante>

    @Insert
    suspend fun agregarUsuario(estudiante: Estudiante)

    @Query("UPDATE estudiante SET nombre =:nombre, apellidos =:apellidos, carrera =:carrera, correo =:correo  WHERE id =:id")
    suspend fun actualizarEstudiante(id: Int, nombre: String, apellidos:String, carrera: String, correo: String)


    @Query("DELETE FROM estudiante WHERE id=:id")
    suspend fun borrarEstudiante(id: Int)
}