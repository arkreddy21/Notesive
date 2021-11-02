package code.atarroid.notesive

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import code.atarroid.notesive.database.Folder


class FolderRecAdapter(val activity:Activity): RecyclerView.Adapter<FolderRecAdapter.ViewHolder>() {

    private lateinit var context: Context

    var folders =  listOf<Folder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.foldName.text = folders[position].folder
        holder.folderItem.setOnClickListener { view: View ->
            val action = FoldersFragmentDirections.actionFoldersFragmentToNotesFragment(folders[position].folderId, folders[position].folder)
            view.findNavController().navigate(action)
        }
        /*
        holder.folderItem.setOnLongClickListener {view:View ->
            when (actionMode) {
                null -> {
                    actionMode = activity.startActionMode(actionModeCallback)
                    view.isSelected = true
                    view.setBackgroundColor(Color.BLUE)
                    true
                }
                else -> false
            }
        }*/
    }

    override fun getItemCount(): Int = folders.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foldName: TextView = itemView.findViewById(R.id.foldName)
        var folderItem: RelativeLayout = itemView.findViewById(R.id.folderItem)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.folder_item, parent, false)
                return ViewHolder(view)
            }
        }
    }


    /*
    var actionMode: ActionMode? = null
    var actionModeCallback = object : ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.contextual_app_bar, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.delete -> {
                    //TODO: Implement delete
                    Toast.makeText(context, "will be deleted soon", Toast.LENGTH_SHORT).show()
                    mode?.finish()  // Action picked, so close the CAB
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }
    }*/


}
