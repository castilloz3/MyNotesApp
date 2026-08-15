package com.example.mynotes

import NoteAdapter
import NoteEntity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {

    // 1. Instanciar nuestro binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mostrarFechaActual()


        noteAdapter = NoteAdapter(
            onDeleteClick = { note -> eliminarNota(note) },
            onItemClick = { note ->
                val dialog = ButtonExpenseDialog.newInstance(note)
                dialog.show(supportFragmentManager, "VentanaDialog")
            }
        )

        binding.btAdd.setOnClickListener {
            val dialog = ButtonExpenseDialog.newInstance() // sin datos = modo crear
            dialog.show(supportFragmentManager, "VentanaDialog")
        }
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }


        observarNotas()
        observarCantidadNotas()
    }

    private fun observarNotas() {
        // llamamos a nuestro singleton
        val db = AppDatabase.getDatabase(applicationContext)

        // Usamos una corrutina para recolectar los datos del Flow
        CoroutineScope(Dispatchers.IO).launch {
            db.noteDao().getAllNotes().collectLatest { listaDeNotas ->
                // Actualizamos el adaptador en el hilo principal
                runOnUiThread {
                    noteAdapter.submitList(listaDeNotas)
                }
            }
        }
    }
    private fun eliminarNota(note: NoteEntity) {
        val db = AppDatabase.getDatabase(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            db.noteDao().deleteNote(note)
            // el Flow de getAllNotes() emitirá la lista actualizada automáticamente
        }
    }

    private fun mostrarFechaActual() {
        val fechaActual = Calendar.getInstance().time

        // 1. Día (número) y nombre del mes -> ej: "15 August"
        val formatoDiaMes = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
        val diaYMes = formatoDiaMes.format(fechaActual)

        // 2. Nombre del día -> ej: "Saturday"
        val formatoNombreDia = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val nombreDia = formatoNombreDia.format(fechaActual)

        binding.tvTitle.text = diaYMes
        binding.tvDate.text = nombreDia
    }

    private fun observarCantidadNotas() {
        val db = AppDatabase.getDatabase(applicationContext)

        CoroutineScope(Dispatchers.IO).launch {
            db.noteDao().getNotesCount().collectLatest { cantidad ->
                runOnUiThread {
                    binding.tvTotal.text = "$cantidad Task"

                }
            }
        }
    }
}