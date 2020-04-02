package com.harryjjacobs.musiq.ui

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.harryjjacobs.musiq.App
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.spotify.SpotifyAppRemoteApi
import com.harryjjacobs.musiq.ui.sections.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel;
    private lateinit var trackProgressBar: ProgressBar;
    private var trackProgressTimer: Timer? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(application, App.spotifyRepository)
        ).get(MainViewModel::class.java);
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            );
        val viewPager: ViewPager = findViewById(R.id.view_pager);
        viewPager.adapter = sectionsPagerAdapter;
        val tabs: TabLayout = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        trackProgressBar = findViewById(R.id.music_progress);

        play_button.setOnClickListener {
            viewModel.togglePause();
        }

        skip_button.setOnClickListener {
            viewModel.skip();
        }

        // Subscribe to viewmodel data
        observeConnectionStatus();
        observeTrackState();
    }

    override fun onStart() {
        super.onStart();
        // Can call this even if its already open, just to double check
        viewModel.openSpotifyRemote();
    }

    override fun onDestroy() {
        super.onDestroy();
        viewModel.closeSpotifyRemote();
    }

    private fun observeTrackState() {
        viewModel.playerState.observe(this, Observer { state ->
            if (state.track != null) {
                trackProgressBar.max = state.track.duration.toInt()
                trackProgressBar.progress = state.playbackPosition.toInt();
            }

            updateTrackProgressTimer(state.isPaused);   // Turn the track progress timer on or off
            updatePlayPauseIcon(state.isPaused);
        })
    }

    private fun observeConnectionStatus() {
        viewModel.connectionState.observe( this, Observer { state ->
            when (state) {
                SpotifyAppRemoteApi.ConnectionStatus.CONNECTED -> {
                    connection_indicator.setBackgroundResource(R.color.colorNoError);
                    connection_indicator.setText(R.string.connected);
                }
                SpotifyAppRemoteApi.ConnectionStatus.DISCONNECTED -> {
                    connection_indicator.setBackgroundResource(R.color.colorError);
                    connection_indicator.setText(R.string.disconnected);
                }
                SpotifyAppRemoteApi.ConnectionStatus.CONNECTING -> {
                    connection_indicator.setBackgroundResource(R.color.colorError);
                    connection_indicator.setText(R.string.connecting);
                }
            }
        })
    }

    private fun updateTrackProgressTimer(paused: Boolean) {
        if (paused && trackProgressTimer != null) {
            // Paused and have a timer, remove it
            trackProgressTimer?.cancel();
            trackProgressTimer = null;
        } else if (!paused && trackProgressTimer == null) {
            // Playing and have no timer, create one
            trackProgressTimer = Timer(false);
            trackProgressTimer?.schedule(object: TimerTask() {
                override fun run() {
                    runOnUiThread {
                        Log.i("TYEST", trackProgressBar.progress.toString())
                        trackProgressBar.progress += 1000    // Increment every second (progress in ms)
                    }
                }
            }, 0, 1000); // 1 second.
        }
    }

    private fun updatePlayPauseIcon(paused: Boolean) {
        if (paused) {
            play_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        } else {
            play_button.setImageResource(R.drawable.ic_pause_white_24dp)
        }
    }
}