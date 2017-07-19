package de.teamawesome.awesomeplayer.playerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;
import java.util.Queue;

import de.teamawesome.awesomeplayer.model.Song;

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
        startMediaPlayerManagerThread();
        System.out.println("PlayerService created!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerManager.quit();
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

    boolean isBound() {
        return bindCount>0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    MediaPlayerManager returnMediaPlayerManager() {
        return mediaPlayerManager;
    }

    PlaybackQueueManager getPlaybackQueueManager() {
        return playbackQueueManager;
    }

    private void startMediaPlayerManagerThread() {
        mediaPlayerManager = new MediaPlayerManager(getPlaybackQueueManager(), this);
        mediaPlayerManager.setDaemon(true);
        mediaPlayerManager.start();
        try {
            synchronized (this) {
                while (!mediaPlayerManager.isPrepared()) {
                    this.wait();
                }
            }
        }catch (InterruptedException e) {
            if(!mediaPlayerManager.isPrepared()) {
                //TODO something useful!
            }
        }
    }

    class PlayerBind extends Binder {
        void playSongWhenReady(Song song) {
            PlayerService.this.getPlaybackQueueManager().appendSongToPlayQueue(song);
        }

        void playAllSongsWhenReady(Song[] songs) {
            PlayerService.this.getPlaybackQueueManager().appendAllSongsToPlayQueue(songs);
        }

        void playSongDelayed(int amountOfSongsToDelay, Song song) {
            PlayerService.this.getPlaybackQueueManager().insertInPosition(amountOfSongsToDelay, song);
        }

        void playSongNow(Song song) {
            PlayerService.this.getPlaybackQueueManager().insertInPosition(0, song);
            PlayerService.this.returnMediaPlayerManager().stopCurrentSong(true);
        }


        void pause() {
            PlayerService.this.returnMediaPlayerManager().pausePlayback();
        }

        void resume() {
            PlayerService.this.returnMediaPlayerManager().resumePlayback();
        }

        void stop() {
            PlayerService.this.returnMediaPlayerManager().stopPlayback();
        }

        void forward() {
            PlayerService.this.returnMediaPlayerManager().stopCurrentSong(true);
        }

        void backward() {
            Song last = PlayerService.this.getPlaybackQueueManager().popSongFormBackStack();
            Song current = PlayerService.this.returnMediaPlayerManager().getCurrentSong();
            if(last == null) {
                return;
            }
            if(current!=null) {
                PlayerService.this.getPlaybackQueueManager().insertInPosition(0, PlayerService.this.returnMediaPlayerManager().getCurrentSong());
            }

            PlayerService.this.getPlaybackQueueManager().insertInPosition(0, last);

            if(current!=null) {
                PlayerService.this.returnMediaPlayerManager().stopCurrentSong(false);
            }

        }

        void shufflePlayQueue() {
            PlayerService.this.getPlaybackQueueManager().shufflePlayQueue();
        }

        void setLoopingMode(boolean newMode) {
            PlayerService.this.returnMediaPlayerManager().setLoopingMode(newMode);
        }

        void setPlaybackPosition(int millis) {
            PlayerService.this.returnMediaPlayerManager().setPlaybackPosition(millis);
        }

        void clearPlayQueue() {
            PlayerService.this.getPlaybackQueueManager().clearQueue();
        }

        void clearBackStack() {
            PlayerService.this.getPlaybackQueueManager().clearStack();
        }

        void removeSongFromPlayQueue(int positionIndex) {
            PlayerService.this.getPlaybackQueueManager().removeSongFromQueue(positionIndex);
        }

        void removeSongFromPlayQueue(Song song) {
            PlayerService.this.getPlaybackQueueManager().removeSongFromQueue(song);
        }

        void removeSongFromBackStack(int positionIndex) {
            PlayerService.this.getPlaybackQueueManager().removeSongFromBackStack(positionIndex);
        }

        void removeSongFromBackStack(Song song) {
            PlayerService.this.getPlaybackQueueManager().removeSongFromBackStack(song);
        }

        List<Song> returnPlayQueueAsList() {
            return PlayerService.this.getPlaybackQueueManager().returnListCopyOfPlayQueue();
        }

        List<Song> returnBackStackAsList() {
            return PlayerService.this.getPlaybackQueueManager().returnListCopyOfBackStack();
        }

        int returnCurrentPosition() {
            return PlayerService.this.returnMediaPlayerManager().returnCurrentPosition();
        }

        int returnCurrentSongDuration() {
            return PlayerService.this.returnMediaPlayerManager().returnSongDuration();
        }

        boolean returnRepeatMode() {
            return PlayerService.this.returnMediaPlayerManager().returnRepeatMode();
        }

        void addPlaybackListener(IPlaybackListener playbackListener) {
            PlayerService.this.returnMediaPlayerManager().addPlaybackListener(playbackListener);
        }

        void removePlaybackListener(IPlaybackListener playbackListener) {
            PlayerService.this.returnMediaPlayerManager().removePlaybackListener(playbackListener);
        }

    }
}
