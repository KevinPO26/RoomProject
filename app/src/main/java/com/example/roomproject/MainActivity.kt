package com.example.roomproject

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.roomproject.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AdaptadorListener  {
    private lateinit var binding: ActivityMainBinding
    var listaEstudiante: MutableList<Estudiante> = mutableListOf()
    lateinit var room : DBPrueba
    lateinit var  estudiante: Estudiante
    lateinit var adatador: AdaptadorUsuarios
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rcestudent.layoutManager = LinearLayoutManager(this)
        room = Room.databaseBuilder(this, DBPrueba::class.java, "dbPruebas").build()
        obtenerEstudiante(room)
        with(binding){
            binding.floatingActionButton.setOnClickListener {
                if (etNombre.text.toString() == "" || etApellidos.text.toString() == "" || etCarrera.text.toString() == "" || etCorreo.text.toString() == ""){
                    Toast.makeText(this@MainActivity, "Todos los campos son requeridos", Toast.LENGTH_LONG).show()
                }else{

                    estudiante = Estudiante(
                        etNombre.text.toString().trim(),
                        etApellidos.text.toString().trim(),
                        etCarrera.text.toString().trim(),
                        etCorreo.text.toString().trim()
                    )

                    agregarEstudiante(room, estudiante)

                }
            }

        }

    }

    private fun agregarEstudiante(room: DBPrueba, estudiante: Estudiante) {
        lifecycleScope.launch {
            room.daoEstudiante().agregarUsuario(estudiante)
            obtenerEstudiante(room)
            limpiarCampos()
        }
    }

    private fun limpiarCampos() {
        with(binding){
            etNombre.setText("")
            etApellidos.setText("")
            etCarrera.setText("")
            etCorreo.setText("")
        }
    }
    fun actualizarEstudiante(room: DBPrueba, estudiante: Estudiante) {
        lifecycleScope.launch {
            room.daoEstudiante().actualizarEstudiante(estudiante.id, estudiante.nombre, estudiante.apellidos, estudiante.carrera, estudiante.correo)
            obtenerEstudiante(room)
            limpiarCampos()
        }
    }

    override fun onEditItemClick(estudiante: Estudiante) {

        this.estudiante = estudiante
        var id = this.estudiante.id
        binding.etNombre.setText(this.estudiante.nombre)
        binding.etApellidos.setText(this.estudiante.apellidos)
        binding.etCarrera.setText(this.estudiante.carrera)
        binding.etCorreo.setText(this.estudiante.correo)

    }

    override fun onDeleteItemClick(estudiante: Estudiante) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Eliminar Estudiante")
        dialog.setMessage("¿Desea eliminar este Estudiante0?")
        dialog.setPositiveButton("SI", DialogInterface.OnClickListener { _, _ ->
            with(binding) {
                lifecycleScope.launch {
                    room.daoEstudiante().borrarEstudiante(estudiante.id)
                    adatador.notifyDataSetChanged()
                    obtenerEstudiante(room)
                }
            }

        })
        dialog.setNegativeButton("NO", DialogInterface.OnClickListener { _, _ ->
            Toast.makeText(this, "Eliminación Cancelada", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        })
        dialog.show()

    }

    override fun onUpdateItem(estudiantes: Estudiante) {

            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Actualizar Estudiante")
            dialog.setMessage("¿Desea actualizar este Estudiante?")
            dialog.setPositiveButton("SI", DialogInterface.OnClickListener { _, _ ->
                with(binding) {
                    if (etNombre.text.toString() == "" || etApellidos.text.toString() == "" || etCarrera.text.toString() == "" || etCorreo.text.toString() == ""){
                        Toast.makeText(this@MainActivity, "Todos los campos son requeridos", Toast.LENGTH_LONG).show()
                    }else{

                        estudiante.nombre = etNombre.text.toString().trim()
                        estudiante.apellidos = etApellidos.text.toString().trim()
                        estudiante.carrera = etCarrera.text.toString().trim()
                        estudiante.correo = etCorreo.text.toString().trim()



                        actualizarEstudiante(room, estudiante)

                    }
                }

            })
            dialog.setNegativeButton("NO", DialogInterface.OnClickListener { _, _ ->
                Toast.makeText(this, "Edicion Cancelada", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            })
            dialog.show()


    }

    private fun obtenerEstudiante(room: DBPrueba) {
        lifecycleScope.launch {
            listaEstudiante = room.daoEstudiante().obtenerEstudiante()
            adatador = AdaptadorUsuarios(listaEstudiante, this@MainActivity)
            binding.rcestudent.adapter = adatador
        }
    }
}