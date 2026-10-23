package com.example.rutinagym

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RutinaAdapter(
    private val lista: List<Ejercicio>
) : RecyclerView.Adapter<RutinaAdapter.EjercicioViewHolder>() {

    // 1. ViewHolder que referencia las vistas de fila_ejercicio.xml
    class EjercicioViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        val tvNombre: TextView = vista.findViewById(R.id.tvFilaNombre)
        val tvDetalles: TextView = vista.findViewById(R.id.tvFilaDetalles)
        val ivIcono: ImageView = vista.findViewById(R.id.ivFilaIcono)
    }

    // 2. Inflado de la vista de la fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_ejercicio, parent, false)
        return EjercicioViewHolder(vista)
    }

    // 3. Rellena los datos de la fila y gestiona el clic para navegar a Detalle
    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val ejercicio = lista[position]

        holder.tvNombre.text = ejercicio.nombre
        holder.tvDetalles.text = "${ejercicio.repeticiones} reps | ${ejercicio.tipo}"

        // Pinta la imagen correspondiente al ejercicio en la miniatura
        holder.ivIcono.setImageResource(ejercicio.imagenResId)

        // Navegación con extras al hacer clic en la fila
        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, DetalleActivity::class.java)

            // Empaquetado de extras (String, Int y el ID del drawable)
            intent.putExtra("EXTRA_NOMBRE", ejercicio.nombre)
            intent.putExtra("EXTRA_REPETICIONES", ejercicio.repeticiones)
            intent.putExtra("EXTRA_TIPO", ejercicio.tipo)
            intent.putExtra("EXTRA_IMAGEN", ejercicio.imagenResId)

            contexto.startActivity(intent)
        }
    }

    // 4. Cantidad total de elementos
    override fun getItemCount(): Int = lista.size
}