package com.harryjjacobs.musiq.ui.sections.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.harryjjacobs.musiq.App

import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.ui.sections.home.HomeFragment
import com.harryjjacobs.musiq.ui.sections.home.HomeViewModel
import com.harryjjacobs.musiq.ui.sections.home.HomeViewModelFactory

class SearchFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_fragment, container, false)
        initViewModel()

        return root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(App.spotifyRepository)
        ).get(SearchViewModel::class.java).apply {
            arguments?.getInt(ARG_SECTION_NUMBER)?.let {
                setPageIndex(it);
            }
        }
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
