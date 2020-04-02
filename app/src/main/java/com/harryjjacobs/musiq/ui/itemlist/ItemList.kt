package com.harryjjacobs.musiq.ui.itemlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


object ItemList {
    fun<TItem, TViewHolder: ItemRecyclerViewAdapter.ViewHolder> create(
        context: Context,
        layoutId: Int,
        viewHolderId: Int,
        columnCount: Int = 0,
        playables: List<TItem>,
        createViewHolder: (view: View) -> TViewHolder,
        bindItemToHolder: (holder: TViewHolder, item: TItem) -> Unit,
        clickListener: (item: TItem) -> Unit
    ): RecyclerView {
        val view: View = LayoutInflater.from(context)
            .inflate(layoutId, null);
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 0 -> LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    columnCount <= 1 -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ItemRecyclerViewAdapter(playables, viewHolderId, createViewHolder, bindItemToHolder, clickListener);
            }
        }
        return view as RecyclerView;
    }
}