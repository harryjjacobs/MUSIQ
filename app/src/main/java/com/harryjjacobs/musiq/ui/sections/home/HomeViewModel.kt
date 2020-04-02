package com.harryjjacobs.musiq.ui.sections.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harryjjacobs.musiq.model.PlayableItemList
import com.harryjjacobs.musiq.repository.SpotifyRepository
import com.harryjjacobs.musiq.ui.sections.SectionViewModel

class HomeViewModel(private val spotifyRepository: SpotifyRepository) : SectionViewModel() {

    fun getPlayableItemLists() = listOf(
        PlayableItemList("Featured", spotifyRepository.getFeaturedPlaylists()),
        PlayableItemList("Recently played", spotifyRepository.getRecentlyPlayed())
    );
}