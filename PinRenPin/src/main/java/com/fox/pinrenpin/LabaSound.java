package com.fox.pinrenpin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by MagicFox on 2016/8/19.
 */
public class LabaSound implements SoundPool.OnLoadCompleteListener {
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;
    private MediaPlayer mediaPlayer;

    public LabaSound(Context context) {
        init(context);
    }

    @SuppressLint("UseSparseArrays")
    private void init(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.labanew_background);
        mediaPlayer.setLooping(true);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundPool.setOnLoadCompleteListener(this);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1, soundPool.load(context, R.raw.labanew_go, 1));
        soundPoolMap.put(2, soundPool.load(context, R.raw.labanew_add, 1));
        soundPoolMap.put(3, soundPool.load(context, R.raw.labanew_otherbtn, 1));
        soundPoolMap.put(4, soundPool.load(context, R.raw.labanew_singlewheel_stop, 1));
        soundPoolMap.put(5, soundPool.load(context, R.raw.labanew_failed, 1));
        soundPoolMap.put(6, soundPool.load(context, R.raw.labanew_2or10times, 1));
        soundPoolMap.put(7, soundPool.load(context, R.raw.labanew_bigaward, 1));
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    private void play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) {
        soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
    }

    public void play(int sound, int number) {
        play(soundPoolMap.get(sound), 1, 1, 1, number, 1);
    }

    public boolean isMediaPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void startMediaPlayer() {
        mediaPlayer.start();
    }

    public void stopMediaPlayer() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    public void releaseSoundPool() {
        soundPool.release();
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

}

