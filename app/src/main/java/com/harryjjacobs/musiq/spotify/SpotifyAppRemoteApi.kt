package com.harryjjacobs.musiq.spotify

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper.CLIENT_ID
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper.REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track


class SpotifyAppRemoteApi private constructor(private val connectionParams: ConnectionParams) {

    enum class PlayingState {
        PAUSED, PLAYING, STOPPED
    }

    enum class ConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }

    private var spotifyAppRemote: SpotifyAppRemote? = null

    val connectionState: MutableLiveData<ConnectionStatus> =
        MutableLiveData<ConnectionStatus>(ConnectionStatus.DISCONNECTED);

    val playerState = MutableLiveData<PlayerState>();

    fun connect(context: Context, handler: (connected: Boolean) -> Unit) {
        // Check if already connected
        if (spotifyAppRemote?.isConnected == true) {
            handler(true)
            return
        }
        // Connect to the remote spotify app
        SpotifyAppRemote.connect(context, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@SpotifyAppRemoteApi.spotifyAppRemote = spotifyAppRemote
                    Log.d("SpotifyAppRemoteService", "Connected!")
                    // Update live connection state data
                    connectionState.value = ConnectionStatus.CONNECTED;
                    // Start listening for state changes
                    subscribeToPlayerChanges { state -> playerState.postValue(state) };
                    // Now we can start interacting with App Remote
                    handler(true);
                }
                override fun onFailure(throwable: Throwable) {
                    Log.e("SpotifyAppRemoteService", throwable.message, throwable)
                    // Update live connection state data
                    connectionState.value = ConnectionStatus.DISCONNECTED;
                    // Something went wrong when attempting to connect!
                    handler(false);
                }
            });
        // Update connection state to show that we are attempting to connect to remote spotify app
        connectionState.value = ConnectionStatus.CONNECTING;
    }

    fun disconnect() {
        SpotifyAppRemote.disconnect(spotifyAppRemote);
        connectionState.value = ConnectionStatus.DISCONNECTED;
    }

    fun play(uri: String) {
        assertAppRemoteConnected()?.playerApi?.play(uri)
    }

    fun resume() {
        assertAppRemoteConnected()?.playerApi?.resume()
    }

    fun pause() {
        assertAppRemoteConnected()?.playerApi?.pause()
    }

    fun queue(track: String) {
        assertAppRemoteConnected()?.playerApi?.queue(track)
    }

    fun skip() {
        assertAppRemoteConnected()?.playerApi?.skipNext()
    }

    fun getCurrentTrack(handler: (track: Track) -> Unit) {
        assertAppRemoteConnected()?.playerApi?.playerState?.setResultCallback { result ->
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
        assertAppRemoteConnected()?.imagesApi?.getImage(imageUri)?.setResultCallback {
            handler(it)
        }
    }

    fun playingState(handler: (PlayingState) -> Unit) {
        assertAppRemoteConnected()?.playerApi?.playerState?.setResultCallback { result ->
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

    fun subscribeToPlayerChanges(handler: (PlayerState) -> Unit) {
        assertAppRemoteConnected()?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            handler(it)
        }
    }

    private fun assertAppRemoteConnected(): SpotifyAppRemote? {
        spotifyAppRemote?.let {
            if (it.isConnected) {
                connectionState.postValue(ConnectionStatus.CONNECTED)
                return it
            }
        }
        Log.e("SpotifyAppRemoteService", "Spotify Disconnected");
        connectionState.value = ConnectionStatus.DISCONNECTED
        return null
    }

    companion object {
        fun create(): SpotifyAppRemoteApi {
            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .build()
            return SpotifyAppRemoteApi(connectionParams)
        }
    }
}