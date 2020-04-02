package com.harryjjacobs.musiq.ui

import android.app.Application
import androidx.lifecycle.*
import com.harryjjacobs.musiq.App
import com.harryjjacobs.musiq.repository.SpotifyRepository
import com.harryjjacobs.musiq.spotify.SpotifyAppRemoteApi

class MainViewModel(application: Application, private val spotify: SpotifyRepository): AndroidViewModel(application) {
    init {
        spotify.initialiseApi(getApplication())
    }

    val connectionState: LiveData<SpotifyAppRemoteApi.ConnectionStatus> =
        Transformations.map(spotify.getPlayerConnectionState()) { it }

    val playerState = spotify.getPlayerState()

    fun togglePause() = spotify.togglePause()
    fun skip() = spotify.skip()

    fun openSpotifyRemote() = spotify.connectPlayer(getApplication())
    fun closeSpotifyRemote() = spotify.disconnectPlayer()

}