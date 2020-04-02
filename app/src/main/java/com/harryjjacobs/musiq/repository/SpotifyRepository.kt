package com.harryjjacobs.musiq.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harryjjacobs.musiq.model.PlayableItem
import com.harryjjacobs.musiq.spotify.SpotifyWebApi
import com.harryjjacobs.musiq.spotify.SpotifyAppRemoteApi
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper
import com.harryjjacobs.musiq.ui.util.toPlayables
import com.harryjjacobs.musiq.ui.util.fetchAlbums
import com.spotify.protocol.types.PlayerState

/**
 * For accessing all spotify information and sending commands to the player
 */
class SpotifyRepository(private val api: SpotifyWebApi, private val player: SpotifyAppRemoteApi) {

    /**
     * Initialises the web api
     */
    fun initialiseApi(context: Context) = api.initialise(context);

    fun getPlayerState(): MutableLiveData<PlayerState> = player.playerState;

    fun getPlayerConnectionState(): MutableLiveData<SpotifyAppRemoteApi.ConnectionStatus> = player.connectionState;

    fun togglePause() = player.playingState { state ->
        when (state) {
            SpotifyAppRemoteApi.PlayingState.PAUSED -> player.resume()
            SpotifyAppRemoteApi.PlayingState.PLAYING -> player.pause()
            SpotifyAppRemoteApi.PlayingState.STOPPED -> {
                // Try to play the last
                api.service.player.getRecentlyPlayed().queue { recents ->
                    recents.items.forEachIndexed { i, history ->
                        if (i == 0) {
                            player.play(history.track.uri.uri)
                        }  else {
                            player.queue(history.track.uri.uri);
                        }
                    }
                }
            }
        }
    }

    fun playPlayableItem(item: PlayableItem) = player.play(item.uri)

    fun skip() = player.skip()

    fun resume() = player.resume()

    fun pause() = player.pause()

    fun getRecentlyPlayed(): LiveData<List<PlayableItem?>> {
        val recent = MutableLiveData<List<PlayableItem?>>();
        api.service.player.getRecentlyPlayed().queue { it ->
            it.items.map { playHistory ->
                playHistory.track
            }.let { simpleTracks ->
                simpleTracks.fetchAlbums(api) { albums ->
                    // Remove repeats
                    albums.distinctBy {
                        it?.uri?.uri
                    // Convert to playable
                    }.toPlayables(api) {
                        recent.postValue(it);
                    }
                };
            }
        }
        return recent;
    }

    fun getFeaturedPlaylists(): LiveData<List<PlayableItem?>> {
        val featured = MutableLiveData<List<PlayableItem?>>();
        api.service.browse.getFeaturedPlaylists().queue { playlists ->
            playlists.playlists.items.toPlayables(api) {
                featured.postValue(it);
            }
        };
        return featured;
    }

    fun connectPlayer(context: Context) {
        if (SpotifyAuthHelper.getToken(context) != null) {
            player.connect(context, handler = { });
        }
    }

    fun disconnectPlayer() {
        player.disconnect();
    }
}