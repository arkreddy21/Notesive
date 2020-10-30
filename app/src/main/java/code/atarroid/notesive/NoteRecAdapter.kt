package code.atarroid.notesive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.atarroid.notesive.database.NoteEntry

class NoteRecAdapter: RecyclerView.Adapter<NoteRecAdapter.ViewHolder>() {

    var notes =  listOf<NoteEntry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = notes[position].content
        holder.txtContent.text = notes[position].title
    }

    override fun getItemCount(): Int = notes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        var txtContent: TextView = itemView.findViewById(R.id.txtContent)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.note_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

}