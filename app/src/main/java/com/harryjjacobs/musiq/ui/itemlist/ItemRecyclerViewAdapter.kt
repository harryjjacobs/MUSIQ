package com.harryjjacobs.musiq.ui.itemlist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.harryjjacobs.musiq.ui.itemlist.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [TODO: OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ItemRecyclerViewAdapter<TItem, TViewHolder : ItemRecyclerViewAdapter.ViewHolder>(
    private val values: List<TItem>,
    private val viewHolderType: Int,
    private val createViewHolder: (view: View) -> TViewHolder,
    private val bindItemToHolder: (holder: TViewHolder, item: TItem) -> Unit,
    private val clickListener: (item: TItem) -> Unit
) : RecyclerView.Adapter<TViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            @Suppress("UNCHECKED_CAST") val item = v.tag as TItem
            clickListener(item);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewHolderType, parent, false)
        return createViewHolder(view);
    }

    override fun onBindViewHolder(holder: TViewHolder, position: Int) {
        val item = values[position]
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
        bindItemToHolder(holder, item);
    }

    override fun getItemCount(): Int = values.size

    abstract class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) { }
}
