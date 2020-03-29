package com.harryjjacobs.musiq.ui.sections.playlist

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.harryjjacobs.musiq.R
import kotlinx.android.synthetic.main.activity_playlist.*


class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        setSupportActionBar(toolbar);

        toolbar_title.text = intent.getStringExtra("name");

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
