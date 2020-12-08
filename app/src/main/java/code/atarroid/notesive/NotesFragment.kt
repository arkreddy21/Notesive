package code.atarroid.notesive

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<FragmentNotesBinding>(inflater, R.layout.fragment_notes, container, false)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        val id: Long = NotesFragmentArgs.fromBundle(requireArguments()).folderId
        val name: String = NotesFragmentArgs.fromBundle(requireArguments()).folderName
        binding.topAppBar.title = name

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

//        val oldTags = dataSource.getTags(id)
//        oldTags.value?.let { addChips(it) }

        viewLifecycleOwner.lifecycleScope.launch {
            Log.i("NotesFrag", "lifecyclescope launched")
            val tags = dataSource.getTags(id)
            tags.observe(viewLifecycleOwner, {
                it?.let { addChips(it) }
            })
        }

        binding.btnNewNote.setOnClickListener{ view: View ->
            view.findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToEditEntryFragment(id))
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
        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao
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