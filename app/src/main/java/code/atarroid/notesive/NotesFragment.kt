package code.atarroid.notesive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.databinding.FragmentNotesBinding
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentNotesBinding>(inflater, R.layout.fragment_notes, container, false)

        val id: Long = NotesFragmentArgs.fromBundle(requireArguments()).folderId
        val name: String = NotesFragmentArgs.fromBundle(requireArguments()).folderName
        binding.phText.text = name

        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao

        val adapter = NoteRecAdapter()
        binding.notesRecView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            val notes = dataSource.getNotes(id)
            notes.observe(viewLifecycleOwner, {
                it?.let {
                    adapter.notes = it
                }
            })
        }

        binding.btnNewNote.setOnClickListener{view: View ->
            view.findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToEditEntryFragment(id))
        }

        return binding.root

    }

}