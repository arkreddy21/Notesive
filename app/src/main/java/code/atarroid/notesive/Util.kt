package code.atarroid.notesive

import android.util.Log
import code.atarroid.notesive.database.NoteDao
import code.atarroid.notesive.database.NoteEntry
import code.atarroid.notesive.database.Tag
import kotlinx.coroutines.*

object ForDb {

    fun insDb(newTitle: String, newContent: String, newTag: Int, id:Long, dataSource:NoteDao) {
        Log.i("Util", "new function Called")
        GlobalScope.launch {
            val newNote = NoteEntry(parentFolderId = id)
            newNote.title = newTitle; newNote.content = newContent; newNote.parentTagId = newTag
            if(newTag != -1){newNote.parentTagName = getTagText(newTag, dataSource)}
            newNote.parentFolderName = getFolderName(id, dataSource)
            dataSource.insertNote(newNote)
            Log.i("util", "new note added")
        }
    }

    fun updateNote(noteId :Long, newTitle: String, newContent: String, newTag: Int, dataSource:NoteDao) {
        Log.i("Util", "new function Called")
        GlobalScope.launch {
            val updatedNote: NoteEntry = dataSource.getNote(noteId)
            updatedNote.title = newTitle; updatedNote.content = newContent; updatedNote.parentTagId = newTag
            if(newTag != -1){
                updatedNote.parentTagName = getTagText(newTag, dataSource)
            }else if(newTag == -1){ updatedNote.parentTagName = "untagged" }
            dataSource.updateNote(updatedNote)
            Log.i("util", "new note added")
        }
    }

    fun addTag(nTag:Tag, dataSource: NoteDao){
        GlobalScope.launch {
            dataSource.insertTag(nTag)
            Log.i("util", "new tag added")
        }
    }

    suspend fun getTagText(id:Int, dataSource: NoteDao): String{
        var tagText:String = "no tag"
        GlobalScope.async {
            Log.i("fun0", "GS 0 launched")
            tagText = dataSource.getTag(id).tag
        }.await()
        return tagText
    }

    suspend fun getFolderName(id: Long, dataSource: NoteDao): String{
        var folderText:String = "not yet"
        GlobalScope.async {
            Log.i("fun0", "GS 0 launched")
            folderText = dataSource.getFolder(id).folder
        }.await()
        return folderText
    }

    suspend fun getNote(id: Long, dataSource: NoteDao): NoteEntry{
        lateinit var note: NoteEntry
        GlobalScope.async{
            note= dataSource.getNote(id)
        }.await()
        return note
    }

    fun deleteFolder(id: Long, dataSource: NoteDao) {
        GlobalScope.launch {
            dataSource.delFolder(id)
            dataSource.delTags(id)
            dataSource.delNotes(id)
        }
    }

    fun deleteNote(id: Long, dataSource: NoteDao) {
        GlobalScope.launch {
            dataSource.delNote(id)
        }
    }


}
