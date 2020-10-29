package code.atarroid.notesive.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDao {
    @Insert(entity = NoteEntry::class)
    fun insertNote(note: NoteEntry)

    @Insert(entity = Folder::class)
    fun insertFolder(folder: Folder)

    @Update
    fun update(note: NoteEntry)

    @Query("SELECT * FROM folders_table")
    fun getAllFolders(): List<Folder>
}

@Database(entities = [NoteEntry::class, Tag::class, Folder::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        private lateinit var INSTANCE: NotesDatabase
        fun getDatabase(context: Context): NotesDatabase {
            synchronized(NotesDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, NotesDatabase::class.java, "notes")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }

    }

}

