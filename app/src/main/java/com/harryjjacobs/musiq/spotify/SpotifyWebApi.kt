package com.harryjjacobs.musiq.spotify

import android.content.Context
import com.adamratzman.spotify.SpotifyApi.Companion.spotifyClientApi
import com.adamratzman.spotify.SpotifyClientApi
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper.CLIENT_ID
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper.CLIENT_SECRET
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper.REDIRECT_URI

class SpotifyWebApi private constructor() {

    lateinit var service: SpotifyClientApi;

    fun initialise(context: Context) {
        service = spotifyClientApi(
            CLIENT_ID,
            CLIENT_SECRET,
            REDIRECT_URI
        ) {
            authorization {
                tokenString = SpotifyAuthHelper.getToken(context);
            }
        }.build();
    }

    companion object {
        fun create(): SpotifyWebApi {
            return SpotifyWebApi();
        }
    }
}