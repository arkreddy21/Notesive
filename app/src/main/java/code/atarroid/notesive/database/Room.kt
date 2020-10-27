package code.atarroid.notesive.database

import android.content.Context
import androidx.room.*


@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteEntry)

    @Update
    suspend fun update(note: NoteEntry)
}

@Database(entities = [NoteEntry::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao
}

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
