package com.example.zeroescrosses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SharedPreferences preferences;
    private boolean isMusicOn;

    public static boolean isSoundOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isMusicOn = preferences.getBoolean("music", true);
        isSoundOn = preferences.getBoolean("sound", true);
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        menu.findItem(R.id.music).setChecked(isMusicOn);
        menu.findItem(R.id.sound).setChecked(isSoundOn);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isMusicOn) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.music:
                if(menuItem.isChecked()) {
                    menuItem.setChecked(false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("music", false);
                    editor.apply();
                    isMusicOn = false;
                    mediaPlayer.stop();
                }
                else {
                    menuItem.setChecked(true);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("music", true);
                    editor.apply();
                    isMusicOn = true;
                    mediaPlayer = MediaPlayer.create(this, R.raw.music);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } break;
            case R.id.sound:
                if(menuItem.isChecked()) {
                    menuItem.setChecked(false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("sound", false);
                    editor.apply();
                    isSoundOn = false;
                }
                else {
                    menuItem.setChecked(true);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("sound", true);
                    editor.apply();
                    isSoundOn = true;
                } break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}