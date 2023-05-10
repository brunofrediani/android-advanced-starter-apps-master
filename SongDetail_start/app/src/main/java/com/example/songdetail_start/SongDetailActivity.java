package com.example.songdetail_start;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.songdetail_start.content.SongUtils;

/**
 * An activity representing a single song detail screen.
 */
public class SongDetailActivity extends AppCompatActivity {

    // SongItem includes the song title and detail.
    public SongUtils.Song mSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // This activity displays the detail. In a real-world scenario,
        // get the data from a content repository.
        mSong = SongUtils.SONG_ITEMS.get
                (getIntent().getIntExtra(SongUtils.SONG_ID_KEY,0));
        // Show the detail information in a TextView.
        if (mSong != null){
            ((TextView) findViewById(R.id.song_detail)).setText(mSong.details);
        }
    }

    /**
     * Performs action if the user selects the Up button.
     *
     * @param item Menu item selected (Up button)
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown.
            // NavUtils allows users to navigate up one level in the
            // application structure.
            NavUtils.navigateUpTo(this,new Intent(this,MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
