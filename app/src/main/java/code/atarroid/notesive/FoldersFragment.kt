package code.atarroid.notesive

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import code.atarroid.notesive.database.Folder
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.databinding.FragmentFoldersBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [FoldersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoldersFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFoldersBinding>(inflater, R.layout.fragment_folders, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getDatabase(application).noteDao

        

        // Initializing the BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        // Changing the BottomSheet State on ButtonClick
        binding.btnNewFolder.setOnClickListener {
            binding.bottomSheet.visibility = View.VISIBLE
            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
            binding.edtNewFolder.requestFocus()
        }

        val adapter = FolderRecAdapter()
        binding.foldersRecView.adapter = adapter


        val insertDb = { newFolder: Folder ->
            viewLifecycleOwner.lifecycleScope.launch {
                dataSource.insertFolder(newFolder)
            }
        }

        binding.btnSaveFolder.setOnClickListener{
            if (!TextUtils.isEmpty(binding.edtNewFolder.text.toString())) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                binding.bottomSheet.visibility = View.GONE
                val newText: String = binding.edtNewFolder.text.toString()
                val newFolder = Folder(folder = newText)
                binding.edtNewFolder.text = null
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                insertDb(newFolder)
            }else
                Toast.makeText(application, "fill the folder name", Toast.LENGTH_SHORT).show()

        }

        viewLifecycleOwner.lifecycleScope.launch {
            val folders = dataSource.getAllFolders()
            folders.observe(viewLifecycleOwner, {
                it?.let {
                    adapter.folders = it
                }
            })
        }

        return binding.root
    }

}
