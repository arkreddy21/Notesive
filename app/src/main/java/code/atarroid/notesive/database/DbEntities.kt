package code.atarroid.notesive.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "notes_table")
data class NoteEntry(
        @PrimaryKey(autoGenerate = true) var noteId: Long,
        var folder: String?,
        var tag: String?,
        var title: String?,
        var content: String?,
)

//@Entity(tableName = "tags_table")
//data class Tag(
//        @PrimaryKey(autoGenerate = true) var tagId: Long,
//        var folder: Folder,
//        var tag: String
//)
//
//@Entity(tableName = "folders_table")
//data class Folder(
//        @PrimaryKey(autoGenerate = true) var folderId: Long,
//        var folder: String
//)
//
//data class TagWithNotes(
//        @Embedded val tag: Tag,
//        @Relation(
//                parentColumn = "tagId",
//                entityColumn = "noteId"
//        )
//        val taggedNotes: List<NoteEntry>
//)
