package de.teamawesome.awesomeplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerService extends Service implements Runnable {

    private boolean keepRunning;
    private MediaPlayer mediaPlayer;
    private Queue<String> audioPathQueue;

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioPathQueue = new ConcurrentLinkedQueue<>();
        new Thread(this, "PlayerServiceTherad").start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keepRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerBind();
    }

    private void addSongToQueue(String pathToSong) {
        audioPathQueue.add(pathToSong);
    }

    private void startMediaPlayer(String pathToSong) throws IOException{
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(pathToSong);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
    }

    @Override
    public void run() {
        try {
            while(keepRunning) {
                if(audioPathQueue.size()<1) {
                    continue;
                }
                startMediaPlayer(audioPathQueue.poll());
            }
        }catch (IOException e) {
            //TODO Make useful catch-clause
            e.printStackTrace();
            this.stopSelf();
        }
    }



    public class PlayerBind extends Binder {
        public void playSongWhenReady(String pathToSong) {
            PlayerService.this.addSongToQueue(pathToSong);
        }
    }
}
