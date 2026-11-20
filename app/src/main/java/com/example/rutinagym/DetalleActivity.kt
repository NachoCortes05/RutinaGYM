package com.example.rutinagym

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetalleActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val tvNombre = findViewById<TextView>(R.id.tvDetalleNombre)
        val tvRepeticiones = findViewById<TextView>(R.id.tvDetalleRepeticiones)
        val tvTipo = findViewById<TextView>(R.id.tvDetalleTipo)
        val tvUbicacion = findViewById<TextView>(R.id.tvDetalleUbicacion)
        val ivTipo = findViewById<ImageView>(R.id.ivTipoEjercicio)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Sin nombre"
        val repeticiones = intent.getIntExtra("EXTRA_REPETICIONES", 0)
        val tipo = intent.getStringExtra("EXTRA_TIPO") ?: "General"
        val imagenUrl = intent.getStringExtra("EXTRA_IMAGEN_URL")
        val fotoPath = intent.getStringExtra("EXTRA_FOTO_PATH")

        tvNombre.text = nombre
        tvRepeticiones.text = "Objetivo: $repeticiones repeticiones"
        tvTipo.text = "Categoría: $tipo"

        if (intent.hasExtra("EXTRA_LATITUD") && intent.hasExtra("EXTRA_LONGITUD")) {
            val lat = intent.getDoubleExtra("EXTRA_LATITUD", 0.0)
            val lon = intent.getDoubleExtra("EXTRA_LONGITUD", 0.0)
            tvUbicacion.visibility = View.VISIBLE
            tvUbicacion.text = "Gimnasio: %.4f, %.4f".format(lat, lon)
        } else {
            tvUbicacion.visibility = View.GONE
        }

        val bitmapLocal = Fotos.cargar(fotoPath)
        if (bitmapLocal != null) {
            ivTipo.setImageBitmap(bitmapLocal)
        } else {
            Glide.with(this)
                .load(imagenUrl)
                .placeholder(R.drawable.gimnasio)
                .error(R.drawable.gimnasio1)
                .into(ivTipo)
        }

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.strongman3)
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}