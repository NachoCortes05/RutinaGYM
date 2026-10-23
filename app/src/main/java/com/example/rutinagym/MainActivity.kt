package com.example.rutinagym

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Enlazar vistas del XML
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etRepeticiones = findViewById<EditText>(R.id.etRepeticiones)
        val etTipo = findViewById<EditText>(R.id.etTipo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnVerLista = findViewById<Button>(R.id.btnVerLista)

        // 2. Guardar ejercicio con validación
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val repeticionesStr = etRepeticiones.text.toString().trim()
            val tipo = etTipo.text.toString().trim()

            // Validación de campos vacíos
            if (nombre.isEmpty() || repeticionesStr.isEmpty() || tipo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val repeticiones = repeticionesStr.toInt()

                // Asignación de foto según la categoría introducida
                val imagenSeleccionada = if (tipo.equals("Cardio", ignoreCase = true)) {
                    R.drawable.gimnasio
                } else {
                    R.drawable.gimnasio1
                }

                // Guardar en el almacén singleton
                RutinaAlmacen.lista.add(Ejercicio(nombre, repeticiones, tipo, imagenSeleccionada))

                Toast.makeText(this, "Ejercicio guardado en la rutina", Toast.LENGTH_SHORT).show()

                // Limpiar campos
                etNombre.text.clear()
                etRepeticiones.text.clear()
                etTipo.text.clear()
            }
        }

        // 3. Navegar a la pantalla de la lista
        btnVerLista.setOnClickListener {
            val intent = Intent(this, RutinaActivity::class.java)
            startActivity(intent)
        }
    }
}