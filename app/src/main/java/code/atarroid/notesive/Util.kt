package code.atarroid.notesive

import android.util.Log
import androidx.lifecycle.viewModelScope
import code.atarroid.notesive.database.NoteDao
import code.atarroid.notesive.database.NoteEntry
import code.atarroid.notesive.database.Tag
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ForDb {
    fun insDb(newTitle: String, newContent: String, id:Long, dataSource:NoteDao) {
        Log.i("Util", "new function Called")
        GlobalScope.launch {
            val newNote = NoteEntry(parentFolderId = id)
            newNote.title = newTitle; newNote.content = newContent
            dataSource.insertNote(newNote)
            Log.i("util", "new note added")
        }
    }

    fun addTag(nTag:Tag, dataSource: NoteDao){
        GlobalScope.launch {
            dataSource.insertTag(nTag)
            Log.i("util", "new tag added")
        }
    }
}
