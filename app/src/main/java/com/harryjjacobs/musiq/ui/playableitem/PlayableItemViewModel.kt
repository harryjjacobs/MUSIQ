package com.harryjjacobs.musiq.ui.playableitem

import androidx.lifecycle.ViewModel
import com.harryjjacobs.musiq.model.PlayableItem
import com.harryjjacobs.musiq.repository.SpotifyRepository

class PlayableItemViewModel(val spotifyRepository: SpotifyRepository): ViewModel() {

    private lateinit var item: PlayableItem
    fun setItem(item: PlayableItem) {
        this.item = item
    }

    fun getImageUrls(): List<String>? = item.images?.map { it.url }

    fun play() {
        spotifyRepository.playPlayableItem(item);
    }
}