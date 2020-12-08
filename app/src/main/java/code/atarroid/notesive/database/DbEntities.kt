package code.atarroid.notesive.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "notes_table")
data class NoteEntry(
        @PrimaryKey(autoGenerate = true) var noteId: Long = 0L,
        var parentTagId: Long = 0L,
        var parentFolderId: Long,
        var title: String? = "",
        var content: String? = "",
)

@Entity(tableName = "tags_table")
data class Tag(
        @PrimaryKey(autoGenerate = true) var tagId: Long = 0L,
        var parentFolderId: Long,
        var tag: String
)

@Entity(tableName = "folders_table")
data class Folder(
        @PrimaryKey(autoGenerate = true) var folderId: Long = 0L,
        var folder: String
)

data class TagWithNotes(
        @Embedded val tag: Tag,
        @Relation(
                parentColumn = "tagId",
                entityColumn = "parentTagId"
        )
        val taggedNotes: List<NoteEntry>
)

data class FolderWithTags(
        @Embedded val folder: Folder,
        @Relation(
                parentColumn = "folderId",
                entityColumn = "parentFolderId"
        )
        val folderedTags: List<Tag>
)
