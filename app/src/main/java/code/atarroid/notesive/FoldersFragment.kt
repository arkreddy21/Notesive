package code.atarroid.notesive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import code.atarroid.notesive.database.Folder
import code.atarroid.notesive.database.NotesDatabase
import code.atarroid.notesive.databinding.FragmentFoldersBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior


/**
 * A simple [Fragment] subclass.
 * Use the [FoldersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoldersFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    //private val application = requireNotNull(this.activity).application
    //private val dataSource = NotesDatabase.getDatabase(application).noteDao
    //private val folders = dataSource.getAllFolders()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFoldersBinding>(inflater, R.layout.fragment_folders,container,false)

        binding.topAppBar2.setOnClickListener {
            findNavController().navigate(R.id.action_foldersFragment_to_favsFragment)
        }

        // Initializing the BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        // Changing the BottomSheet State on ButtonClick
        binding.btnNewFolder.setOnClickListener {
            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        }

        val adapter = FolderRecAdapter()
        binding.foldersRecView.adapter = adapter

//        binding.btnSaveFolder.setOnClickListener{
//            val newText: String = binding.edtNewFolder.text.toString()
//            val newFolder = Folder(folder = newText)
//            dataSource.insertFolder(newFolder)
//            adapter.setFolders(folders)
//        }

        return binding.root
    }

}
