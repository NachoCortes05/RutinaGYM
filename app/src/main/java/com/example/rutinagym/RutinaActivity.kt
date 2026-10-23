package com.example.rutinagym

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rutinagym.R



class RutinaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutina)

        val rvRutina = findViewById<RecyclerView>(R.id.rvRutina)
        val btnVolver = findViewById<Button>(R.id.btnVolverFormulario)

        // Sin LayoutManager la lista no sabe cómo colocarse y queda en blanco
        rvRutina.layoutManager = LinearLayoutManager(this)

        // Asignamos el Adapter pasándole la lista global acumulada
        rvRutina.adapter = RutinaAdapter(RutinaAlmacen.lista)

        // Botón para cerrar esta pantalla y volver al formulario
        btnVolver.setOnClickListener {
            finish()
        }
    }
}