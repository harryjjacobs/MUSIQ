package com.harryjjacobs.musiq.ui.sections.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harryjjacobs.musiq.App
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.model.PlayableItemList
import com.harryjjacobs.musiq.ui.lists.ListFactory
import com.harryjjacobs.musiq.ui.sections.search.SearchFragment

/**
 * The fragment containing the home view
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val listsContainer = root.findViewById<LinearLayout>(R.id.lists_container)
        loadingIndicator = root.findViewById(R.id.loading_indicator)
        initViewModel()

        // Show that we are loading data
        showLoadingIndicator()

        // Fetch lists to display
        viewModel.getPlayableItemLists().forEach {
            addList(it, listsContainer)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(App.spotifyRepository)
        ).get(HomeViewModel::class.java).apply {
            arguments?.getInt(ARG_SECTION_NUMBER)?.let {
                setPageIndex(it);
            }
        }
    }

    private fun addList(
        playableItemList: PlayableItemList,
        container: LinearLayout
    ) {
        // Container for the list
        val featuredPlayablesContainer =
            ListFactory.createListContainer(requireContext())
        // Add the container in advance in case we want to TODO(show placeholder icons)
        container.addView(featuredPlayablesContainer)
        // Listen for state change indicating that the playable items have loaded
        playableItemList.playableItems.observe(viewLifecycleOwner, Observer { playables ->
            // Finished loading at least a bit of data
            hideLoadingIndicator();
            // Create and put the list in it's playables container
            ListFactory.createHorizontalPlayablesList(
                context = requireContext(),
                playables = playables,
                container = featuredPlayablesContainer,
                title = playableItemList.listName
            )
        })
    }

    private fun showLoadingIndicator() {
        loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        loadingIndicator.visibility = View.GONE
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