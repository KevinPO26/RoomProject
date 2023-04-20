package com.example.roomproject
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Estudiante::class],
    version = 1
)
abstract class DBPrueba: RoomDatabase() {
    abstract fun daoEstudiante(): DaoEstudiante
}