import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Traer todas las notas
    @Query("SELECT * FROM notes_table ORDER BY id DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    // Total de notas
    @Query("SELECT COUNT(*) FROM notes_table")
    fun getNotesCount(): Flow<Int>

    // Insertar una nota nueva
    @Insert
    suspend fun insertNote(note: NoteEntity)

    // Borrar una nota
    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // Actualizar nota
    @Update
    suspend fun updateNote(note: NoteEntity)
}