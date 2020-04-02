package com.harryjjacobs.musiq.ui.lists

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.model.PlayableItem
import com.harryjjacobs.musiq.ui.itemlist.ItemList
import com.harryjjacobs.musiq.ui.itemlist.ItemRecyclerViewAdapter
import com.harryjjacobs.musiq.ui.playableitem.PlayableItemActivity
import com.harryjjacobs.musiq.ui.util.Serialization.JSON

object ListFactory {
    fun createHorizontalPlayablesList(context: Context, container: LinearLayout, playables: List<PlayableItem?>, title: String) {
        // Set title
        container.findViewById<TextView>(R.id.hlist_title).text = title;
        // Add list content
        container.addView(ItemList.create(
            context,
            layoutId = R.layout.generic_horizontal_recycler,
            viewHolderId = R.layout.playlist_item,
            playables = playables,
            createViewHolder = { view: View ->
                object : ItemRecyclerViewAdapter.ViewHolder(view) {
                    val name: TextView = view.findViewById(R.id.playable_name)
                    val image: ImageView = view.findViewById(R.id.playable_image);
                }
            },
            bindItemToHolder = { holder, item ->
                holder.name.text = item?.name ?: "Untitled";
                item?.images?.getOrNull(0)?.let { image ->
                    Glide
                        .with(context)
                        .load(image.url)
                        .centerInside()
                        .placeholder(R.drawable.ic_album_white_24dp)
                        .into(holder.image);
                }
            },
            clickListener = { item ->
                val myIntent = Intent(context, PlayableItemActivity::class.java)
                myIntent.putExtra("playable", JSON.stringify(PlayableItem.serializer(), item as PlayableItem));
                context.startActivity(myIntent)
            }
        ));
    }

    @SuppressLint("InflateParams")
    fun createListContainer(context: Context): LinearLayout {
        val container = LayoutInflater.from(context)
            .inflate(R.layout.hlist_container, null) as LinearLayout;
        return container;
    }
}