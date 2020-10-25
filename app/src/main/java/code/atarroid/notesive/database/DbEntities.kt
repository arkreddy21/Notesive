package code.atarroid.notesive.database

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey


@Fts4
@Entity(tableName = "notes_table")
data class NoteEntry(
        @PrimaryKey(autoGenerate = true) var noteId: Long = 0L,
        var folder: String,
        var tag: String?,
        var title: String?,
        var content: String?,
)
