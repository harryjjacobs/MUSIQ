package com.harryjjacobs.musiq.ui.sections.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harryjjacobs.musiq.repository.SpotifyRepository

class HomeViewModelFactory(
    private val spotifyRepository: SpotifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SpotifyRepository::class.java)
            .newInstance(spotifyRepository)
    }
}