package com.harryjjacobs.musiq

import android.app.Application
import com.harryjjacobs.musiq.repository.SpotifyRepository
import com.harryjjacobs.musiq.spotify.SpotifyAppRemoteApi
import com.harryjjacobs.musiq.spotify.SpotifyWebApi

class App : Application() {
    companion object {
        val spotifyRepository: SpotifyRepository = SpotifyRepository(
            SpotifyWebApi.create(),
            SpotifyAppRemoteApi.create()
        );
    }
}