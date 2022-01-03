package code.atarroid.notesive

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import code.atarroid.notesive.database.NoteDao
import code.atarroid.notesive.database.NoteEntry
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.database.Tag
import code.atarroid.notesive.databinding.FragmentEditEntryBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditEntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEntryFragment : Fragment() {

    private lateinit var binding: FragmentEditEntryBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var dataSource: NoteDao
    private lateinit var application: Application

    var id: Long = 0L
    var noteId: Long = 0L
    private lateinit var folderName: String

    private lateinit var newTitle: String
    private lateinit var newContent: String
    private var newTagId:Int = 0

    var delflag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<FragmentEditEntryBinding>(inflater, R.layout.fragment_edit_entry, container, false)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        application = requireNotNull(this.activity).application
        dataSource = NotesDatabase.getDatabase(application).noteDao
        id = EditEntryFragmentArgs.fromBundle(requireArguments()).folderId
        noteId = EditEntryFragmentArgs.fromBundle(requireArguments()).noteId
        folderName = EditEntryFragmentArgs.fromBundle(requireArguments()).folderName
        binding.topAppBarEdit.title = folderName

        binding.topAppBarEdit.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete -> {
                    Log.i("EditEntryFrag", "menu item click listener launched for edit entry")
                    Toast.makeText(application, "deleted note", Toast.LENGTH_SHORT).show()
                    delflag = 1
                    if (noteId == 0L) { }
                    else { ForDb.deleteNote(noteId, dataSource)}
                    //super.onDestroyView()
                    //findNavController().navigate(EditEntryFragmentDirections.actionEditEntryFragmentToNotesFragment(id, folderName))
                    activity?.onBackPressed()
                    true
                }
                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            Log.i("EditEntry", "lifecyclescope launched")
            if(EditEntryFragmentArgs.fromBundle(requireArguments()).noteId != 0L){
                val note = dataSource.getNote(noteId)
                if(note.parentTagId != -1){
                    newTagId = note.parentTagId
                }
                binding.titleTxt.setText(note.title)
                binding.noteTxt.setText(note.content)
            }
            val tags = dataSource.getTags(id)
            tags.observe(viewLifecycleOwner, { it?.let {addChips(it)} })
        }

        /*
        if(EditEntryFragmentArgs.fromBundle(requireArguments()).noteId != 0L){
            viewLifecycleOwner.lifecycleScope.launch{
                //val note = ForDb.getNote(noteId, dataSource)
                val note = dataSource.getNote(noteId)
                if(note.parentTagId != -1){binding.tagChipGroup.check(note.parentTagId); Log.i("EditEntry", "old tag tagged")}    //TODO: make this work
                binding.titleTxt.setText(note.title)
                binding.noteTxt.setText(note.content)
            }
        }*/

        /*
        val insertDb = { newTitle: String, newContent: String ->
            viewLifecycleOwner.lifecycleScope.launch {
                val newNote = NoteEntry(parentFolderId = id)
                newNote.title = newTitle; newNote.content = newContent
                dataSource.insertNote(newNote)
            }
        }

        binding.btnSave.setOnClickListener { view: View ->
            val newTitle = binding.titleTxt.text.toString()
            val newContent = binding.noteTxt.text.toString()
            //view.findNavController().navigate(R.id.action_editEntryFragment_to_notesFragment)
            insertDb(newTitle, newContent)
        }
         */

        binding.addTag.setOnClickListener {
            binding.bottomSheet.visibility = View.VISIBLE
            val state =
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                        BottomSheetBehavior.STATE_COLLAPSED
                    else
                        BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
            binding.edtNewTag.requestFocus()
        }

        binding.btnSaveTag.setOnClickListener { view: View ->
            addTag(binding.edtNewTag.text.toString())
        }

        /*
        val v: View = binding.scrollView
        binding.coord.setOnTouchListener(OnTouchListener { v, event ->
            v.onTouchEvent(event)
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(v, 0)
            true
        })
         */

        return binding.root
    }

    override fun onStop() {
        newTitle = binding.titleTxt.text.toString()
        newContent = binding.noteTxt.text.toString()
        newTagId = binding.tagChipGroup.checkedChipId
        Log.i("EditEntry", "title and content Called")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.i("EditEntry", "onDestroyView Called")
        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao

        if(delflag == 1){

        }
        else if(EditEntryFragmentArgs.fromBundle(requireArguments()).noteId == 0L){
            val id = EditEntryFragmentArgs.fromBundle(requireArguments()).folderId
            if (newTitle != "" && newContent != "") {
                ForDb.insDb(newTitle, newContent, newTagId, id, dataSource)
            }
        }
        else{
            val noteId = EditEntryFragmentArgs.fromBundle(requireArguments()).noteId
            if (newTitle != "" && newContent != "") {
                ForDb.updateNote(noteId, newTitle, newContent, newTagId, dataSource)
            }
        }

        super.onDestroyView()
    }

    private fun addChips(items: List<Tag>) {
        if (items.isNotEmpty()) {
            if (binding.tagChipGroup.childCount > 1) {
                binding.tagChipGroup.removeViews(0, binding.tagChipGroup.childCount - 1)
            }
            for (item in items) {
                val mChip = this.layoutInflater.inflate(R.layout.chip_item, null, false) as Chip
                mChip.text = item.tag
                mChip.id = item.tagId
                if(newTagId == mChip.id){ mChip.isChecked = true }
                binding.tagChipGroup.addView(mChip, binding.tagChipGroup.childCount - 1)
            }
        }
    }

    private fun addTag(text: String) {
        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao
        val nTag = Tag(parentFolderId = NotesFragmentArgs.fromBundle(requireArguments()).folderId, tag = text)
        ForDb.addTag(nTag, dataSource)
    }


}