package com.harryjjacobs.musiq.model

import androidx.lifecycle.LiveData

data class PlayableItemList(val listName: String, val playableItems: LiveData<List<PlayableItem?>>)