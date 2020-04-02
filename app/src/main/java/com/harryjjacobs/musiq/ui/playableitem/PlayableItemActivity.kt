package com.harryjjacobs.musiq.ui.playableitem

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.harryjjacobs.musiq.App
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.model.PlayableItem
import com.harryjjacobs.musiq.ui.util.Serialization.JSON
import kotlinx.android.synthetic.main.activity_playable_item.*

class PlayableItemActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayableItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playable_item);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProvider(
            this,
            PlayableItemModelFactory(App.spotifyRepository)
        ).get(PlayableItemViewModel::class.java);

        intent.getStringExtra("playable").let {
            val item = JSON.parse(PlayableItem.serializer(), it);
            playable_name.text = item.name;
            playable_author.text = item.authors.getOrElse(0) { "" }
            viewModel.setItem(item);
        }

        fab_play.setOnClickListener { _ ->
            viewModel.play();
            finish();
        }

        viewModel.getImageUrls()?.getOrNull(0)?.let { url ->
            Glide
                .with(this)
                .load(url)
                .centerInside()
                .placeholder(R.drawable.ic_album_white_24dp)
                .into(playable_image);
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
