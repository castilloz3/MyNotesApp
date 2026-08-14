package com.example.mynotes

import NoteEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mynotes.databinding.FragmentAddNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ButtonExpenseDialog : DialogFragment() {

    private var _binding: FragmentAddNotesBinding? = null
    private val binding get() = _binding!!

    private var noteId: Long = 0L
    private var isEditMode = false

    companion object {
        private const val ARG_ID = "arg_id"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_DESCRIPTION = "arg_description"
        private const val ARG_DATE = "arg_date"

        // Se usa para CREAR (sin argumentos)
        fun newInstance() = ButtonExpenseDialog()

        // Se usa para EDITAR (con los datos de la nota existente)
        fun newInstance(note: NoteEntity): ButtonExpenseDialog {
            val fragment = ButtonExpenseDialog()
            fragment.arguments = Bundle().apply {
                putLong(ARG_ID, note.id)
                putString(ARG_TITLE, note.title)
                putString(ARG_DESCRIPTION, note.description)
                putString(ARG_DATE, note.date)
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedDialog)

        // Leemos los argumentos si vinieron (modo edición)
        arguments?.let {
            noteId = it.getLong(ARG_ID, 0L)
            isEditMode = noteId != 0L
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Si es edición, precargamos los campos con los datos actuales
        if (isEditMode) {
            binding.etNameProduct.setText(arguments?.getString(ARG_TITLE))
            binding.etDescription.setText(arguments?.getString(ARG_DESCRIPTION))
            binding.btSave.text = "Actualizar"
        }

        binding.btCancel.setOnClickListener {
            dismiss()
        }

        binding.btSave.setOnClickListener {
            val titulo = binding.etNameProduct.text.toString().trim()
            val descripcion = binding.etDescription.text.toString().trim()

            if (titulo.isNotEmpty()) {
                val db = AppDatabase.getDatabase(requireContext())

                if (isEditMode) {
                    // Reutilizamos el id y la fecha originales
                    val notaActualizada = NoteEntity(
                        id = noteId,
                        title = titulo,
                        description = descripcion,
                        date = arguments?.getString(ARG_DATE) ?: "13 Aug"
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        db.noteDao().updateNote(notaActualizada)
                        withContext(Dispatchers.Main) { dismiss() }
                    }
                } else {
                    val nuevaNota = NoteEntity(
                        title = titulo,
                        description = descripcion,
                        date = "13 Aug"
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        db.noteDao().insertNote(nuevaNota)
                        withContext(Dispatchers.Main) { dismiss() }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(900, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}