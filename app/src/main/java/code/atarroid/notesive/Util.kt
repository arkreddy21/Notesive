package code.atarroid.notesive

import android.util.Log
import androidx.lifecycle.viewModelScope
import code.atarroid.notesive.database.NoteDao
import code.atarroid.notesive.database.NoteEntry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ForDb {
    fun insDb(newTitle: String, newContent: String, id:Long, dataSource:NoteDao) {
        Log.i("Util", "new function Called")
        GlobalScope.launch {
            val newNote = NoteEntry(parentFolderId = id)
            newNote.title = newTitle; newNote.content = newContent
            dataSource.insertNote(newNote)
            Log.i("ViewModel", "new note added")
        }
    }
}
