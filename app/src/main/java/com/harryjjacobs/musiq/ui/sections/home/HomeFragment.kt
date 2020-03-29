package com.harryjjacobs.musiq.ui.sections.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.model.Playlist
import com.harryjjacobs.musiq.ui.lists.ListFactory
import com.harryjjacobs.musiq.ui.sections.playlist.PlaylistActivity


/**
 * The fragment containing the home view
 */
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java).apply {
            setPageIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val list1 = ListFactory.createHorizontalPlaylistList(
            context = requireContext(),
            playlists = listOf(Playlist("Acid Jazz"), Playlist("Songs to dab to"), Playlist("Cheesy Hits")),
            title = "Jump back in..."
        );
        val list2 = ListFactory.createHorizontalPlaylistList(
            requireContext(),
            playlists = listOf(Playlist("Shrek 2 Soundtrack"), Playlist("Dark Side of the Moon"), Playlist("Butt"), Playlist("Harry's Playlist")),
            title = "Recommended..."
        );

        with(root.findViewById<LinearLayout>(R.id.home_layout)) {
            addView(list1);
            addView(list2);
        }

        /*val textView: TextView = root.findViewById(R.id.section_label)
        homeViewModel.text.observe(viewLifecycleOwner, Observer<String> {
            textView.text = it
        })*/

        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}