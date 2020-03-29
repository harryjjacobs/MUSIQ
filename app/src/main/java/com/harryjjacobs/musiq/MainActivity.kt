package com.harryjjacobs.musiq

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentActivity
import com.harryjjacobs.musiq.ui.SectionsPagerAdapter
import com.harryjjacobs.musiq.ui.itemlist.dummy.DummyContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val progressBar: ProgressBar = findViewById(R.id.music_progress);
    }
}