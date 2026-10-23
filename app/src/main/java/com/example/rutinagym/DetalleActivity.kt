package com.example.rutinagym

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class DetalleActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        // 1. Enlazar vistas
        val tvNombre = findViewById<TextView>(R.id.tvDetalleNombre)
        val tvRepeticiones = findViewById<TextView>(R.id.tvDetalleRepeticiones)
        val tvTipo = findViewById<TextView>(R.id.tvDetalleTipo)
        val ivTipo = findViewById<ImageView>(R.id.ivTipoEjercicio)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // 2. Recoger los extras del Intent con valores por defecto
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Sin nombre"
        val repeticiones = intent.getIntExtra("EXTRA_REPETICIONES", 0)
        val tipo = intent.getStringExtra("EXTRA_TIPO") ?: "General"
        val imagenId = intent.getIntExtra("EXTRA_IMAGEN", android.R.drawable.ic_menu_agenda)

        // 3. Pintar los datos y la imagen en pantalla
        tvNombre.text = nombre
        tvRepeticiones.text = "Objetivo: $repeticiones repeticiones"
        tvTipo.text = "Categoría: $tipo"
        ivTipo.setImageResource(imagenId)

        // 4. Sonido con MediaPlayer
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.strongman3)
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 5. Botón para volver atrás
        btnVolver.setOnClickListener {
            finish()
        }
    }

    // 6. Liberación de la memoria del reproductor al salir de la pantalla
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}