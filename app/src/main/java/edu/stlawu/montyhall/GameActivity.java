package edu.stlawu.montyhall;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;

public class GameActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    // instance variables for audio files
    public AudioAttributes  audioAttributes = null;
    public SoundPool soundPool = null;
    private int clickSound = 0;
    private int goatSound = 1;
    private int carSound = 2;
    private int doorSound = 3;
    private int countdownSound = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // build the audio
        this.audioAttributes = new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        this.soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        this.clickSound = this.soundPool.load(
                this, R.raw.click, 1);
        this.goatSound = this.soundPool.load(
                this, R.raw.goat, 1);
        this.carSound = this.soundPool.load(
                this, R.raw.car, 1);
        this.doorSound = this.soundPool.load(
                this, R.raw.door, 1);
        this.countdownSound = this.soundPool.load(
                this, R.raw.countdown, 1);
    }

    // Method used to get preference stuff
    public int getWinsLosses(int determiner) {
        if (determiner == 0) {
            return getPreferences(MODE_PRIVATE).getInt("WSTAYED", 0);
        }
        if (determiner == 1) {
            return getPreferences(MODE_PRIVATE).getInt("LSTAYED", 0);
        }
        if (determiner == 2) {
            return getPreferences(MODE_PRIVATE).getInt("WSWITCHED", 0);
        }
        if (determiner == 3) {
            return getPreferences(MODE_PRIVATE).getInt("LSWITCHED", 0);
        }
        else {
            SharedPreferences sharedPreferences = getSharedPreferences("NC", MODE_PRIVATE);
            return sharedPreferences.getInt("NEWCONT", 0);
        }
    }

    // Method used to play a sound based on input
    public void playSound(int soundToPlay) {

        if (soundToPlay == 0) {
            soundPool.play(clickSound, 1f, 1f, 1, 0, 1f);
        }
        if (soundToPlay == 1) {
            soundPool.play(goatSound, 1f, 1f, 1, 0, 1f);
        }
        if (soundToPlay == 2) {
            soundPool.play(carSound, 1f, 1f, 1, 0, 1f);
        }
        if (soundToPlay == 3) {
            soundPool.play(doorSound, 1f, 1f, 1, 0, 1f);
        }
        if (soundToPlay == 4) {
            soundPool.play(countdownSound, 1f, 1f, 1, 0, 1f);
        }

    }

    // Method used to store and access variables from personal preferences
    public void saveWinsLosses(int winsStayed, int lossesStayed, int winsSwitched, int lossesSwitched) {

        getPreferences(MODE_PRIVATE)
                .edit()
                .putInt("WSTAYED", winsStayed)
                .putInt("LSTAYED", lossesStayed)
                .putInt("WSWITCHED", winsSwitched)
                .putInt("LSWITCHED", lossesSwitched)
                .apply();
    }

    public void isNoLongerNewGame() {
        SharedPreferences sharedPreferences = getSharedPreferences("NC", MODE_PRIVATE);
        sharedPreferences.edit().putInt("NEWCONT", 1).apply();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
