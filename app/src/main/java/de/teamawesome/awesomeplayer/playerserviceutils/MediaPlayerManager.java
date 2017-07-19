package de.teamawesome.awesomeplayer.playerserviceutils;

import android.media.MediaPlayer;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.teamawesome.awesomeplayer.PlayerService;
import de.teamawesome.awesomeplayer.model.Song;

public class MediaPlayerManager implements Runnable {

    private static long IDLE_TIME_TILL_SELF_DESTRUCT = 5000;

    private PlaybackQueueManager playManager;
    private PlayerService playerService;

    private boolean keepRunning;
    private boolean paused;
    private long timestampLastActivity;

    private Queue<IPlaybackListener> playbackListeners;
    private MediaPlayer mediaPlayer;

    public MediaPlayerManager(PlaybackQueueManager playManager, PlayerService playerService) {
        super();
        this.playManager = playManager;
        this.playerService = playerService;
        keepRunning = true;
        paused = false;
        timestampLastActivity = System.currentTimeMillis();

        playbackListeners = new LinkedBlockingQueue<>();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void run() {
        try {
            Looper.prepare();
            while(keepRunning) {
                if(mediaPlayer.isPlaying()) {
                    timestampLastActivity = System.currentTimeMillis();
                    Thread.yield();
                    continue;
                }
                if(System.currentTimeMillis()-IDLE_TIME_TILL_SELF_DESTRUCT>timestampLastActivity
                        && !playerService.isBound()) {
                    keepRunning = false;
                    break;
                }
                if(playManager.returnQueueLength()<1 || paused) {
                    Thread.yield();
                    continue;
                }
                startPlayingSong(playManager.pollSongPath());
            }
        }catch (Exception e) {
            //TODO Make catch-clause more useful?
            e.printStackTrace();
        }finally {
            dispose();
            playerService.stopSelf();
        }
    }


    private void startPlayingSong(Song song){
        //TODO Remove Toast
        Toast.makeText(playerService.getApplicationContext(), "Playing: " + song.getTitle(), Toast.LENGTH_SHORT).show();
        stop();

        try {
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            for(IPlaybackListener playbackListener: playbackListeners) {
                playbackListener.newSongStartsPlaying(song);
            }
        }catch (IOException e) {
            throw new IllegalArgumentException("The Path to the song (" + song.getTitle() + " Path: " + song.getPath() + ") supplied as Argument " + " is invalid!", e);
        }
    }

    public void dispose() {
        keepRunning = false;
        paused = false;
        mediaPlayer.release();
    }

    public void stop() {
        paused = false;
        mediaPlayer.reset();
    }

    public void pause() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            paused = true;
        }
    }

    public void resume() {
        if(paused) {
            mediaPlayer.start();
            paused = false;
        }
    }

    public void jumpForward() {
        //TODO
    }

    public void jumpBackward() {
        //TODO
    }

    public void addPlaybackListener(IPlaybackListener playbackListener) {
        playbackListeners.add(playbackListener);
    }

    public void removePlaybackListener(IPlaybackListener playbackListener) {
        playbackListeners.remove(playbackListener);
    }
}
