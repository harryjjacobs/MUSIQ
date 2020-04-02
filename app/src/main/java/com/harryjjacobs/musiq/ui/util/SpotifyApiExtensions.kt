package com.harryjjacobs.musiq.ui.util

import com.adamratzman.spotify.models.*
import com.harryjjacobs.musiq.model.PlayableItem
import com.harryjjacobs.musiq.spotify.SpotifyWebApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun List<SimpleTrack>.convertToTracks(api: SpotifyWebApi, callback: (tracks: List<Track?>) -> Unit) {
    convertSimpleTracksToFullTracks(api, this, callback);
}

fun List<PlaylistTrack>.convertToTracks(): List<Track?> {
    return convertPlaylistTracksToFullTracks(this);
}

fun List<SimpleTrack>.fetchAlbums(api: SpotifyWebApi, callback: (tracks: List<SimpleAlbum?>) -> Unit) {
    fetchAlbumsForTracks(api, this, callback);
}

fun<T : Any> AbstractPagingObject<T>.fetchNextPage(callback: (AbstractPagingObject<T>?) -> Unit) {
    GlobalScope.launch {
        val nextPage = this@fetchNextPage.getNext();
        callback(nextPage);
    }
}

fun List<CoreObject?>.toPlayables(api: SpotifyWebApi, callback: (playables: List<PlayableItem>) -> Unit) {
    GlobalScope.launch {
        val result = this@toPlayables.map { coreObject ->
            suspendCoroutine<PlayableItem> { cont ->
                when(coreObject) {
                    is SimplePlaylist -> {
                        PlayableItem.createFromCoreObject(
                            api,
                            coreObject
                        ) {
                            cont.resume(it);
                        };
                    }
                    is SimpleAlbum -> {
                        PlayableItem.createFromCoreObject(
                            api,
                            coreObject
                        ) {
                            cont.resume(it);
                        };
                    }
                }
            }
        }
        callback(result);
    }
}

private fun fetchAlbumsForTracks(api: SpotifyWebApi, simpleTracks: List<SimpleTrack>, callback: (simpleAlbums: List<SimpleAlbum?>) -> Unit) {
    GlobalScope.launch {
        val result = simpleTracks.map { simpleTrack ->
            api.service.tracks.getTrack(simpleTrack.uri.uri).complete()?.album
        }
        callback(result);
    }
}

private fun convertSimpleTracksToFullTracks(api: SpotifyWebApi, simpleTracks: List<SimpleTrack>, callback: (simpleAlbums: List<Track?>) -> Unit) {
    GlobalScope.launch {
        val result = simpleTracks.map { simpleTrack ->
            api.service.tracks.getTrack(simpleTrack.uri.uri).complete()
        }
        callback(result);
    }
}

private fun convertPlaylistTracksToFullTracks(simpleTracks: List<PlaylistTrack>): List<Track?> {
    return simpleTracks.map { playlist -> playlist.track }
}