package com.example.rutinagym

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File

class MainActivity : AppCompatActivity() {

    private val viewModel: RutinaViewModel by viewModels()

    private lateinit var ivFoto: ImageView
    private lateinit var tvUbicacion: TextView
    private lateinit var clienteUbicacion: FusedLocationProviderClient

    private var fotoPath: String? = null
    private var archivoFotoActual: File? = null
    private var latitud: Double? = null
    private var longitud: Double? = null

    private val pedirPermisoCamara = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) lanzarCamara()
        else Toast.makeText(this, "Sin permiso no puedo hacer la foto", Toast.LENGTH_SHORT).show()
    }

    private val hacerFoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exito ->
        val archivo = archivoFotoActual
        if (exito && archivo != null) {
            fotoPath = archivo.absolutePath
            ivFoto.setImageBitmap(Fotos.cargar(fotoPath))
        } else {
            archivo?.delete()
        }
    }

    private val elegirDeGaleria = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val ruta = Fotos.copiarDesdeUri(this, uri)
            if (ruta != null) {
                fotoPath = ruta
                ivFoto.setImageBitmap(Fotos.cargar(ruta))
            }
        }
    }

    private val pedirPermisosUbicacion = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        val fina = resultado[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val aproximada = resultado[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fina || aproximada) obtenerUbicacion()
        else Toast.makeText(this, "Sin permiso no puedo obtener la ubicación", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clienteUbicacion = LocationServices.getFusedLocationProviderClient(this)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etRepeticiones = findViewById<EditText>(R.id.etRepeticiones)
        val etTipo = findViewById<EditText>(R.id.etTipo)
        val btnCamara = findViewById<Button>(R.id.btnCamara)
        val btnGaleria = findViewById<Button>(R.id.btnGaleria)
        val btnUbicacion = findViewById<Button>(R.id.btnUbicacion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnVerLista = findViewById<Button>(R.id.btnVerLista)

        ivFoto = findViewById(R.id.ivFotoEjercicio)
        tvUbicacion = findViewById(R.id.tvUbicacion)

        btnCamara.setOnClickListener {
            pulsarBotonFoto()
        }

        btnGaleria.setOnClickListener {
            elegirDeGaleria.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        btnUbicacion.setOnClickListener {
            pulsarBotonUbicacion()
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val repeticionesStr = etRepeticiones.text.toString().trim()
            val tipo = etTipo.text.toString().trim()

            if (nombre.isEmpty() || repeticionesStr.isEmpty() || tipo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repeticiones = repeticionesStr.toIntOrNull()
            if (repeticiones == null || repeticiones <= 0) {
                Toast.makeText(this, "Introduce un número de repeticiones válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.guardarEjercicio(
                nombre = nombre,
                repeticiones = repeticiones,
                tipo = tipo,
                fotoPath = fotoPath,
                latitud = latitud,
                longitud = longitud
            ) {
                Toast.makeText(this, "Ejercicio guardado en tu rutina", Toast.LENGTH_SHORT).show()
                etNombre.text.clear()
                etRepeticiones.text.clear()
                etTipo.text.clear()
                fotoPath = null
                latitud = null
                longitud = null
                ivFoto.setImageDrawable(null)
                tvUbicacion.text = "Ubicación: sin registrar"
            }
        }

        btnVerLista.setOnClickListener {
            startActivity(Intent(this, RutinaActivity::class.java))
        }
    }

    private fun pulsarBotonFoto() {
        val permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permiso == PackageManager.PERMISSION_GRANTED) lanzarCamara()
        else pedirPermisoCamara.launch(Manifest.permission.CAMERA)
    }

    private fun lanzarCamara() {
        val archivo = Fotos.crearArchivo(this)
        archivoFotoActual = archivo
        hacerFoto.launch(Fotos.uriDe(this, archivo))
    }

    private fun tienePermisoUbicacion(): Boolean {
        val fina = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val aprox = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fina == PackageManager.PERMISSION_GRANTED || aprox == PackageManager.PERMISSION_GRANTED
    }

    private fun pulsarBotonUbicacion() {
        if (tienePermisoUbicacion()) {
            obtenerUbicacion()
        } else {
            pedirPermisosUbicacion.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        clienteUbicacion
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { ubicacion: Location? ->
                if (ubicacion != null) {
                    latitud = ubicacion.latitude
                    longitud = ubicacion.longitude
                    tvUbicacion.text = "Ubicación: %.4f, %.4f".format(ubicacion.latitude, ubicacion.longitude)
                } else {
                    Toast.makeText(this, "No se ha podido obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
    }
}