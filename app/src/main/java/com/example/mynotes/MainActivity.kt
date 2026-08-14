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


class MainActivity : AppCompatActivity() {

    // 1. Instanciar nuestro binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }

    private fun observarNotas() {
        // CAMBIO: En lugar de crear un Room.databaseBuilder aquí, llamamos a nuestro singleton
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
            // No necesitas actualizar el adapter manualmente:
            // el Flow de getAllNotes() emitirá la lista actualizada automáticamente
        }
    }
}