package code.atarroid.notesive

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import code.atarroid.notesive.database.NoteDao
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.database.Tag
import code.atarroid.notesive.databinding.FragmentNotesBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {

    private lateinit var binding:FragmentNotesBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var dataSource: NoteDao
    private lateinit var application: Application

    var id: Long = 0L
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<FragmentNotesBinding>(inflater, R.layout.fragment_notes, container, false)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        id = NotesFragmentArgs.fromBundle(requireArguments()).folderId
        name = NotesFragmentArgs.fromBundle(requireArguments()).folderName
        binding.topAppBarNotes.title = name

        application = requireNotNull(this.activity).application
        dataSource = NotesDatabase.getDatabase(application).noteDao

        //binding.topAppBarNotes.inflateMenu(R.menu.contextual_app_bar)
        binding.topAppBarNotes.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete -> {
                    Log.i("NotesFrag", "menu item click listener launched")
                    Toast.makeText(application, "deleted folder", Toast.LENGTH_SHORT).show()
                    ForDb.deleteFolder(id, dataSource)
                    //findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToFoldersFragment())
                    activity?.onBackPressed()
                    true
                }
                else -> false
            }
        }


        val adapter = NoteRecAdapter(dataSource)
        binding.notesRecView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            val notes = dataSource.getNotes(id)
            notes.observe(viewLifecycleOwner, { it.let { adapter.notes = it } })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            Log.i("NotesFrag", "lifecyclescope launched")
            val tags = dataSource.getTags(id)
            tags.observe(viewLifecycleOwner, { it?.let { addChips(it) } })
        }

        binding.btnNewNote.setOnClickListener{ view: View ->
            view.findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToEditEntryFragment(id, name))
        }

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

        binding.btnSaveTag.setOnClickListener{view: View ->
            addTag(binding.edtNewTag.text.toString())
        }

        return binding.root
    }


    private fun addTag(text:String){
        val nTag = Tag(parentFolderId = NotesFragmentArgs.fromBundle(requireArguments()).folderId, tag = text)
        ForDb.addTag(nTag, dataSource)
    }

    private fun addChips(items: List<Tag>) {
        if(items.isNotEmpty()) {
            if(binding.tagChipGroup.childCount > 3){
                binding.tagChipGroup.removeViews(2,binding.tagChipGroup.childCount-3)
            }
            for (item in items) {
                val mChip = this.layoutInflater.inflate(R.layout.chip_item, null, false) as Chip
                mChip.text = item.tag
                mChip.id = item.tagId
                binding.tagChipGroup.addView(mChip, binding.tagChipGroup.childCount-1)
            }
        }
    }

    private fun addnewChip(items: List<Tag>) {
        if(items.isNotEmpty()){
            val mChip = this.layoutInflater.inflate(R.layout.chip_item, null, false) as Chip
            mChip.text = items[items.size-1].tag
            //val paddingDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
            //mChip.setPadding(paddingDp, 0, paddingDp, 0)
            //mChip.setOnCheckedChangeListener { compoundButton, b -> }
            binding.tagChipGroup.addView(mChip, binding.tagChipGroup.childCount-1)
        }
    }

}
