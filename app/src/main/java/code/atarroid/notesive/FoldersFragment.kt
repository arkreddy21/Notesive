package code.atarroid.notesive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import code.atarroid.notesive.databinding.FragmentFoldersBinding


/**
 * A simple [Fragment] subclass.
 * Use the [FoldersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoldersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFoldersBinding>(inflater, R.layout.fragment_folders,container,false)

        binding.topAppBar2.setOnClickListener {
            findNavController().navigate(R.id.action_foldersFragment_to_favsFragment)
        }

        return binding.root
    }

}
