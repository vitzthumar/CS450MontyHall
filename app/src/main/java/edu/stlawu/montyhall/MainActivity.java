package edu.stlawu.montyhall;

import android.content.SharedPreferences;
import android.net.Uri;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.stlawu.montyhall.MainFragment.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    // instance variables for audio files
    public AudioAttributes  audioAttributes = null;
    private SoundPool soundPool = null;
    private int clickSound = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    // Method used to store if new or continue was clicked
    public void saveNewOrContinue(boolean isNew) {
        SharedPreferences sharedPreferences = getSharedPreferences("NC", MODE_PRIVATE);
        if (isNew) {
            sharedPreferences.edit().putInt("NEWCONT", 0).apply();
        } else {
            sharedPreferences.edit().putInt("NEWCONT", 1).apply();
        }
    }

    // Method to play audio
    public void playSound() {
        soundPool.play(clickSound, 1f, 1f, 1, 0, 1f);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}
