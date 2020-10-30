package code.atarroid.notesive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import code.atarroid.notesive.database.NoteEntry
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.databinding.FragmentEditEntryBinding
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [EditEntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEntryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentEditEntryBinding>(inflater, R.layout.fragment_edit_entry, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao

        val id: Long = EditEntryFragmentArgs.fromBundle(requireArguments()).folderId

        val insertDb = {newTitle: String, newContent: String ->
            viewLifecycleOwner.lifecycleScope.launch {
                val newNote = NoteEntry(parentFolderId = id)
                newNote.title = newTitle; newNote.content = newContent
                dataSource.insertNote(newNote)
            }
        }

        binding.btnSave.setOnClickListener{view: View ->
            val newTitle = binding.titleTxt.text.toString()
            val newContent = binding.noteTxt.text.toString()
            //view.findNavController().navigate(R.id.action_editEntryFragment_to_notesFragment)
            insertDb(newTitle, newContent)
        }


        return binding.root
    }



}