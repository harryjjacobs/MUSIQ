package com.harryjjacobs.musiq.ui.lists

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.model.Playlist
import com.harryjjacobs.musiq.ui.itemlist.ItemList
import com.harryjjacobs.musiq.ui.itemlist.ItemRecyclerViewAdapter
import com.harryjjacobs.musiq.ui.sections.playlist.PlaylistActivity

object ListFactory {
    @SuppressLint("InflateParams")
    fun createHorizontalPlaylistList(context: Context, playlists: List<Playlist>, title: String): View {
        val listView = ItemList.create(
            context,
            layoutId = R.layout.generic_horizontal_recycler,
            viewHolderId = R.layout.playlist_item,
            items = playlists,
            createViewHolder = { view: View ->
                object : ItemRecyclerViewAdapter.ViewHolder(view) {
                    val name: TextView = view.findViewById(R.id.playlist_name)
                    val image: ImageView = view.findViewById(R.id.playlist_image);
                }
            },
            bindItemToHolder = { holder, item ->
                holder.name.text = item.name;
                // TODO: Set image
                //holder.image.setImageBitmap();
            },
            clickListener = {
                val myIntent = Intent(context, PlaylistActivity::class.java)
                myIntent.putExtra("name", it.name) //Optional parameters
                context.startActivity(myIntent)
            }
        );
        val container = LayoutInflater.from(context)
            .inflate(R.layout.hlist_container, null);
        if (container is LinearLayout) {
            with(container) {
                findViewById<TextView>(R.id.hlist_title).text = title;
                addView(listView);
            }
        }
        return container;
    }
}