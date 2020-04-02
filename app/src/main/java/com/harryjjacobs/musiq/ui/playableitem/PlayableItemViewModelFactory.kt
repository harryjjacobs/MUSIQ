package com.harryjjacobs.musiq.ui.playableitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harryjjacobs.musiq.repository.SpotifyRepository

class PlayableItemModelFactory(
    private val spotifyRepository: SpotifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SpotifyRepository::class.java)
            .newInstance(spotifyRepository)
    }
}