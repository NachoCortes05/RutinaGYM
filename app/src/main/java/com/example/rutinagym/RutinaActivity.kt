package com.example.rutinagym

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RutinaActivity : AppCompatActivity() {

    private val viewModel: RutinaViewModel by viewModels()

    private lateinit var rvRutina: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var etBuscarCategoria: EditText
    private lateinit var adapter: RutinaAdapter
    private val listaMostrada = mutableListOf<Ejercicio>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutina)

        rvRutina = findViewById(R.id.rvRutina)
        progressBar = findViewById(R.id.progressBar)
        etBuscarCategoria = findViewById(R.id.etBuscarCategoria)
        val btnVolver = findViewById<Button>(R.id.btnVolverFormulario)

        rvRutina.layoutManager = LinearLayoutManager(this)
        adapter = RutinaAdapter(listaMostrada) { ejercicio ->
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar ejercicio")
                .setMessage("¿Quieres borrar '${ejercicio.nombre}' de tu rutina?")
                .setPositiveButton("Borrar") { _, _ ->
                    viewModel.borrarEjercicio(ejercicio)
                    Toast.makeText(this, "Ejercicio eliminado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        rvRutina.adapter = adapter

        btnVolver.setOnClickListener {
            finish()
        }

        etBuscarCategoria.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filtrarPorCategoria(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is EstadoRutina.Cargando -> {
                    progressBar.visibility = View.VISIBLE
                }
                is EstadoRutina.Exito -> {
                    progressBar.visibility = View.GONE
                    actualizarLista(estado.lista)
                }
                is EstadoRutina.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, estado.mensaje, Toast.LENGTH_LONG).show()
                    actualizarLista(estado.listaLocal)
                }
            }
        }

        viewModel.cargarEjercicios()
    }

    private fun actualizarLista(nuevaLista: List<Ejercicio>) {
        listaMostrada.clear()
        listaMostrada.addAll(nuevaLista)
        adapter.notifyDataSetChanged()
    }
}