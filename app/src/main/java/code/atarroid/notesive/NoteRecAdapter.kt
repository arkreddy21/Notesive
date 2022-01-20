package code.atarroid.notesive

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import code.atarroid.notesive.database.NoteDao
import code.atarroid.notesive.database.NoteEntry


class NoteRecAdapter(private val dataSource: NoteDao): RecyclerView.Adapter<NoteRecAdapter.ViewHolder>() {

    //private lateinit var mOnClickListener: ListItemClickListener

    var notes =  listOf<NoteEntry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = notes[position].title
        holder.txtContent.text = notes[position].content
        holder.txtTag.text = notes[position].parentTagName
        holder.itemView.setOnClickListener { view: View ->
            val action = NotesFragmentDirections.actionNotesFragmentToEditEntryFragment(notes[position].parentFolderId, notes[position].parentFolderName)
            action.noteId = notes[position].noteId
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = notes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        var txtContent: TextView = itemView.findViewById(R.id.txtContent)
        var txtTag: TextView = itemView.findViewById(R.id.txtTag)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.note_item, parent, false)
                return ViewHolder(view)
            }
        }
    }


}


internal interface ListItemClickListener {
    fun onListItemClick(position: Int)
}
