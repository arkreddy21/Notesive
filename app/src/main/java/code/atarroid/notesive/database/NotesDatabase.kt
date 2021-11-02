package code.atarroid.notesive.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDao {
    @Insert(entity = NoteEntry::class)
    suspend fun insertNote(note: NoteEntry)

    @Update(entity = NoteEntry::class)
    suspend fun updateNote(note: NoteEntry)

    @Insert(entity = Tag::class)
    suspend fun insertTag(tag: Tag)

    @Insert(entity = Folder::class)
    suspend fun insertFolder(folder: Folder)

    @Query("SELECT * FROM folders_table")
    fun getAllFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM folders_table WHERE folderId = :key")
    fun getFolder(key: Long): Folder

    @Query("SELECT * FROM tags_table WHERE parentFolderId = :key")
    fun getTags(key: Long): LiveData<List<Tag>>

    @Query("SELECT * FROM tags_table WHERE tagId = :id")
    suspend fun getTag(id: Int): Tag

    @Query("SELECT * FROM notes_table WHERE parentTagId = :key")
    fun getTaggedNotes(key: Long): List<NoteEntry>

    @Query("SELECT * FROM notes_table WHERE parentFolderId = :key")
    fun getNotes(key: Long): LiveData<List<NoteEntry>>

    @Query("SELECT * FROM notes_table WHERE noteId = :noteId")
    suspend fun getNote(noteId: Long): NoteEntry

    @Query("DELETE FROM folders_table WHERE folderId = :id")
    suspend fun delFolder(id: Long)

    @Query("DELETE FROM notes_table WHERE parentFolderId = :id")
    suspend fun delNotes(id: Long)

    @Query("DELETE FROM tags_table WHERE parentFolderId = :id")
    suspend fun delTags(id: Long)
}


@Database(entities = [NoteEntry::class, Tag::class, Folder::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, NotesDatabase::class.java, "notes")
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}

