package com.example.rutinagym

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RutinaAdapter(
    private val lista: List<Ejercicio>,
    private val onLongClick: (Ejercicio) -> Unit
) : RecyclerView.Adapter<RutinaAdapter.EjercicioViewHolder>() {

    class EjercicioViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        val tvNombre: TextView = vista.findViewById(R.id.tvFilaNombre)
        val tvDetalles: TextView = vista.findViewById(R.id.tvFilaDetalles)
        val ivIcono: ImageView = vista.findViewById(R.id.ivFilaIcono)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_ejercicio, parent, false)
        return EjercicioViewHolder(vista)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val ejercicio = lista[position]

        holder.tvNombre.text = ejercicio.nombre
        val textoUbicacion = if (ejercicio.latitud != null && ejercicio.longitud != null) {
            " | GPS"
        } else {
            ""
        }
        holder.tvDetalles.text = "${ejercicio.repeticiones} reps | ${ejercicio.tipo}$textoUbicacion"

        val bitmapLocal = Fotos.cargar(ejercicio.fotoPath)
        if (bitmapLocal != null) {
            holder.ivIcono.setImageBitmap(bitmapLocal)
        } else {
            Glide.with(holder.itemView.context)
                .load(ejercicio.imagenUrl)
                .placeholder(R.drawable.gimnasio)
                .error(R.drawable.gimnasio1)
                .into(holder.ivIcono)
        }

        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, DetalleActivity::class.java).apply {
                putExtra("EXTRA_NOMBRE", ejercicio.nombre)
                putExtra("EXTRA_REPETICIONES", ejercicio.repeticiones)
                putExtra("EXTRA_TIPO", ejercicio.tipo)
                putExtra("EXTRA_IMAGEN_URL", ejercicio.imagenUrl)
                putExtra("EXTRA_FOTO_PATH", ejercicio.fotoPath)
                if (ejercicio.latitud != null && ejercicio.longitud != null) {
                    putExtra("EXTRA_LATITUD", ejercicio.latitud)
                    putExtra("EXTRA_LONGITUD", ejercicio.longitud)
                }
            }
            contexto.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(ejercicio)
            true
        }
    }

    override fun getItemCount(): Int = lista.size
}