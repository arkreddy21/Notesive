package code.atarroid.notesive

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import code.atarroid.notesive.database.NoteEntry
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.databinding.FragmentEditEntryBinding
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass.
 * Use the [EditEntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEntryFragment : Fragment() {

    private lateinit var binding:FragmentEditEntryBinding

    private lateinit var newTitle:String
    private lateinit var newContent:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<FragmentEditEntryBinding>(inflater, R.layout.fragment_edit_entry, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao
        val id = EditEntryFragmentArgs.fromBundle(requireArguments()).folderId


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

    override fun onStop() {
        newTitle = binding.titleTxt.text.toString()
        newContent = binding.noteTxt.text.toString()
        Log.i("EditEntry", "title and content Called")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.i("EditEntry", "onDestroyView Called")
        doThis()
        super.onDestroyView()
    }

    private fun doThis() {
        Log.i("EditEntry", "doThis Called")
        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao
        val id = EditEntryFragmentArgs.fromBundle(requireArguments()).folderId
        if(newTitle!=""&&newContent!=""){
            ForDb.insDb(newTitle, newContent, id, dataSource)}
    }


}