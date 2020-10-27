package code.atarroid.notesive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import code.atarroid.notesive.databinding.FragmentFavsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [FavsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFavsBinding>(inflater, R.layout.fragment_favs,container,false)

        binding.topAppBar1.setOnClickListener {
            findNavController().navigate(R.id.action_favsFragment_to_foldersFragment)
        }

        return binding.root
    }

}
