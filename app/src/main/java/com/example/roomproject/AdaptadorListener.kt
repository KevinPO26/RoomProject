package com.example.roomproject

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

interface AdaptadorListener {
    fun onEditItemClick(estudiante: Estudiante)
    fun onDeleteItemClick(estudiante: Estudiante)
    fun onUpdateItem(estudiantes: Estudiante)

}