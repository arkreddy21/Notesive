package code.atarroid.notesive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.database.Tag
import code.atarroid.notesive.databinding.FragmentNotesBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

//        viewLifecycleOwner.lifecycleScope.launch {
//            val tags = dataSource.getTags(id)
//            tags.observe(viewLifecycleOwner, {
//                it?.let {
//                    addChip(it, binding.tagChipGroup)
//                }
//            })
//        }


        binding.btnNewNote.setOnClickListener{ view: View ->
            view.findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToEditEntryFragment(id))
        }

        binding.addTag.setOnClickListener{}

        return binding.root
    }

    private fun addChip(pItem: List<Tag>, pChipGroup: ChipGroup) {
        val application = requireNotNull(this.activity).application
        val lChip = Chip(application)
        for(item in pItem) {
            lChip.text = item.tag
            pChipGroup.addView(lChip, pChipGroup.childCount-1)
        }
    }

}