package code.atarroid.notesive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.atarroid.notesive.database.Folder


class FolderRecAdapter: RecyclerView.Adapter<FolderRecAdapter.ViewHolder>() {

//    var one = Folder(folder="one"); var two = Folder(folder="two")
//    private var folders = listOf<Folder>(one, two)

    var folders =  listOf<Folder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.foldName.text = folders[position].folder
    }

    override fun getItemCount(): Int = folders.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foldName: TextView = itemView.findViewById(R.id.foldName)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.folder_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

}
