package de.teamawesome.awesomeplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils;
import de.teamawesome.awesomeplayer.playerserviceutils.IPlaybackListener;
import de.teamawesome.awesomeplayer.playerserviceutils.MediaPlayerManager;
import de.teamawesome.awesomeplayer.playerserviceutils.PlaybackQueueManager;

/**
 * Service that should be used to control the MediaPlayer responsible for Playback. In order to use
 * this Service, any Component should first call the startService(Intent) Method on the
 * {@link android.app.Application} object responsible for this application. For 5 seconds afterwards
 * the Service can be bound by invoking the bindService(Intent) method on the same Application object.
 *
 * Once the Service has been bound successfully it returns an Instance of {@link PlayerBind}. This
 * Instance should be used to control the Playback using the Methods defined in {@link PlayerBind}.
 *
 * The service destroys itself after 5 seconds of idle time, but only when it is no longer bound.
 * Therefore it is absolutely crucial, that any component binding the service also unbinds it once
 * it is finished controlling the playback.
 *
 * Components can use the {@link IPlaybackListener} interface to get notified when playback of any
 * song starts. An instance of this interface should be passed in the
 * addPlaybackListener(IPlaybackListener) method of the {@link PlayerBind} object returned after
 * binding the Service. If the service destroys itself, all IPlaybackListener are removed.
 */
public class PlayerService extends Service {

    private MediaPlayerManager mediaPlayerManager;
    private PlaybackQueueManager playbackQueueManager;
    private int bindCount = 0;

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        playbackQueueManager = new PlaybackQueueManager();
        mediaPlayerManager = new MediaPlayerManager(playbackQueueManager, this);
        new Thread(mediaPlayerManager, "MediaPlayerManagerThread").start();
        System.out.println("PlayerService created!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerManager.dispose();
        System.out.println("Player Service destroyed!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        bindCount++;
        return new PlayerBind();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bindCount--;
        return super.onUnbind(intent);
    }

    public boolean isBound() {
        return bindCount>0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }

    public PlaybackQueueManager getPlaybackQueueManager() {
        return playbackQueueManager;
    }

    public class PlayerBind extends Binder {
        public void playSongWhenReady(String pathToSong) {
            PlayerService.this.getPlaybackQueueManager().appendSong(pathToSong);
        }

        public void playAllSongsWhenReady(String[] pathToSongs) {
            PlayerService.this.getPlaybackQueueManager().appendAllSongs(pathToSongs);
        }

        public void playSongDelayed(int amountOfSongsToDelay, String pathToSong) {
            PlayerService.this.getPlaybackQueueManager().insertInPosition(amountOfSongsToDelay, pathToSong);
        }

        public void playSongNow(String pathToSong) {
            PlayerService.this.getPlaybackQueueManager().insertInPosition(0, pathToSong);
            PlayerService.this.getMediaPlayerManager().stop();
        }

        public void pause() {
            PlayerService.this.getMediaPlayerManager().pause();
        }

        public void resume() {
            PlayerService.this.getMediaPlayerManager().resume();
        }

        public void stop() {
            PlayerService.this.getMediaPlayerManager().stop();
        }

        public void clearPlayQueue() {
            PlayerService.this.getPlaybackQueueManager().clearQueue();
        }

        public void removeSongFromPlayQueue(int positionIndex) {
            PlayerService.this.getPlaybackQueueManager().removeSongFromQueue(positionIndex);
        }

        public List<String> returnPlayQueueAsList() {
            return PlayerService.this.getPlaybackQueueManager().returnListCopy();
        }

        public void addPlaybackListener(IPlaybackListener playbackListener) {
            PlayerService.this.getMediaPlayerManager().addPlaybackListener(playbackListener);
        }

        public void removePlaybackListener(IPlaybackListener playbackListener) {
            PlayerService.this.getMediaPlayerManager().removePlaybackListener(playbackListener);
        }
    }
}
