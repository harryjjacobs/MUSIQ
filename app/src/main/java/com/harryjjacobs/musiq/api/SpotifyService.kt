package com.harryjjacobs.musiq.api

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track


class SpotifyService private constructor(private val connectionParams: ConnectionParams) {

    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    enum class PlayingState {
        PAUSED, PLAYING, STOPPED
    }

    fun connect(context: Context, handler: (connected: Boolean) -> Unit) {
        // Check if already connected
        if (mSpotifyAppRemote?.isConnected == true) {
            handler(true)
            return
        }
        // Connect to the remote spotify app
        SpotifyAppRemote.connect(context, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("SpotifyService", "Connected! Yay!")
                    // Now we can start interacting with App Remote
                    handler(true);
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("SpotifyService", throwable.message, throwable)
                    // Something went wrong when attempting to connect!
                    handler(false);
                }
            })
    }

    fun disconnect() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    fun play(uri: String) {
        mSpotifyAppRemote?.playerApi?.play(uri)
    }

    fun resume() {
        mSpotifyAppRemote?.playerApi?.resume()
    }

    fun pause() {
        mSpotifyAppRemote?.playerApi?.pause()
    }

    fun getCurrentTrack(handler: (track: Track) -> Unit) {
        mSpotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
            handler(result.track)
        }
    }

    fun getCurrentTrackImage(handler: (Bitmap) -> Unit)  {
        getCurrentTrack { track ->
            fetchImage(track.imageUri) { bitmap ->
                handler(bitmap)
            }
        }
    }

    fun fetchImage(imageUri: ImageUri, handler: (Bitmap) -> Unit)  {
        mSpotifyAppRemote?.imagesApi?.getImage(imageUri)?.setResultCallback {
            handler(it)
        }
    }

    fun playingState(handler: (PlayingState) -> Unit) {
        mSpotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
            when {
                result.track.uri == null -> {
                    handler(PlayingState.STOPPED)
                }
                result.isPaused -> {
                    handler(PlayingState.PAUSED)
                }
                else -> {
                    handler(PlayingState.PLAYING)
                }
            }
        }
    }

    fun subscribeToChanges(handler: (PlayerState) -> Unit) {
        mSpotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            handler(it)
        }
    }

    companion object {
        private const val CLIENT_ID = "808fb42b646648008d475bb0c60cde58"
        private const val REDIRECT_URI = "com.harryjjacobs.musiq://callback"
        fun create(): SpotifyService {
            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()
            return SpotifyService(connectionParams);
        }
    }
}