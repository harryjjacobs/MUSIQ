package com.harryjjacobs.musiq.model

import com.adamratzman.spotify.models.*
import com.harryjjacobs.musiq.spotify.SpotifyWebApi
import com.harryjjacobs.musiq.ui.util.convertToTracks
import com.harryjjacobs.musiq.ui.util.fetchNextPage
import kotlinx.serialization.Serializable

/**
 * Represents an item with a spotify uri that can be played.
 * Usually either an album or a playlist.
 */
@Serializable
class PlayableItem private constructor(
    val type: Type,
    val name: String,
    val authors: List<String>,
    val uri: String,
    val href: String,
    val images: List<SpotifyImage>? = null
) {
    private var simpleTracks: PagingObject<SimpleTrack>? = null;
    private var playlistTracks: PagingObject<PlaylistTrack>? = null;
    var allTracks: MutableList<Track?> = mutableListOf();

    enum class Type {
        SIMPLE_PLAYLIST,
        SIMPLE_ALBUM
    }

    fun canFetchMoreTracks(): Boolean {
        return when (type) {
            Type.SIMPLE_PLAYLIST -> {
                (simpleTracks == null) || (simpleTracks?.next != null)
            }
            Type.SIMPLE_ALBUM -> {
                (playlistTracks == null) || (playlistTracks?.next != null)
            }
        }
    }

    fun fetchNextTracks(api: SpotifyWebApi, callback: (tracks: List<Track?>) -> Unit) {
        when (type) {
            Type.SIMPLE_PLAYLIST -> {
                fetchPlaylistTracks(api, callback);
            }
            Type.SIMPLE_ALBUM -> {
                fetchAlbumTracks(api, callback);
            }
        }
    }

    fun fetchAllTracks(api: SpotifyWebApi, callback: (tracks: List<Track?>) -> Unit) {
        if (canFetchMoreTracks()) {
            fetchNextTracks(api) {
                fetchNextTracks(api, callback);
            }
        } else {
            callback(allTracks);
        }
    }
    
    private fun fetchAlbumTracks(api: SpotifyWebApi, callback: (tracks: List<Track?>) -> Unit) {
        if (simpleTracks == null) {
            api.service.albums.getAlbumTracks(uri).queue {
                simpleTracks = it;
                // Call this function again now that we have a pager object to use to fetch tracks
                fetchAlbumTracks(api, callback);
            }
            return;
        }
        simpleTracks?.items?.convertToTracks(api) { tracks ->
            simpleTracks?.fetchNextPage {
                simpleTracks = it as PagingObject<SimpleTrack>?;
                // Do callback after we've fetched the next paging object
                allTracks.addAll(tracks);
                callback(tracks);
            }
        }
    }

    private fun fetchPlaylistTracks(api: SpotifyWebApi, callback: (tracks: List<Track?>) -> Unit) {
        if (playlistTracks == null) {
            api.service.playlists.getPlaylistTracks(uri).queue {
                playlistTracks = it;
                // Call this function again now that we have a pager object to use to fetch tracks
                fetchPlaylistTracks(api, callback);
            }
            return;
        }
        playlistTracks?.fetchNextPage {
            playlistTracks = it as PagingObject<PlaylistTrack>?;
            // Do callback after we've fetched the next paging object
            playlistTracks?.items?.convertToTracks()?.let { tracks ->
                allTracks.addAll(tracks);
                callback(tracks);
            };
        }
    }

    companion object {
        private fun createFromPlaylist(playlist: SimplePlaylist, callback: (playableItem: PlayableItem) -> Unit) {
            callback(PlayableItem(
                name = playlist.name,
                type = Type.SIMPLE_PLAYLIST,
                authors = listOf(playlist.owner.displayName ?: "Unknown Author"),
                uri = playlist.uri.uri,
                href = playlist.href,
                images = playlist.images
            ));
        }

        private fun createFromAlbum(album: SimpleAlbum, callback: (playableItem: PlayableItem) -> Unit) {
            callback(PlayableItem(
                name = album.name,
                type = Type.SIMPLE_ALBUM,
                authors = album.artists.map { it -> it.name },
                uri = album.uri.uri,
                href = album.href,
                images = album.images
            ));
        }

        fun createFromCoreObject(api: SpotifyWebApi, album: SimpleAlbum, callback: (playableItem: PlayableItem) -> Unit) {
            createFromAlbum(album, callback);
        }

        fun createFromCoreObject(api: SpotifyWebApi, playlist: SimplePlaylist, callback: (playableItem: PlayableItem) -> Unit) {
            createFromPlaylist(playlist, callback);
        }

    }
}