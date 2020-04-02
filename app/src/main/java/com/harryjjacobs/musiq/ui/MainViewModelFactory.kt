package com.harryjjacobs.musiq.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harryjjacobs.musiq.repository.SpotifyRepository

class MainViewModelFactory(
    private val application: Application,
    private val spotifyRepository: SpotifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, SpotifyRepository::class.java)
            .newInstance(application, spotifyRepository)
    }
}