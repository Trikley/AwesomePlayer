package de.teamawesome.awesomeplayer.playerservice;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.teamawesome.awesomeplayer.model.Song;

/**
 * Use this class to interact with the PlayerService. Just instantiate this class and call any
 * of its public methods.
 * !IMPORTANT!
 * Do not fail to call the dispose method on every instance of this class you created once you
 * are done with it.
 */
public class PlayerBindManager implements ServiceConnection{

    private PlayerService.PlayerBind playerBind;
    private Application applicationContext;

    private int disposeCount;
    private Queue<Runnable> queuedActions;
    private List<IPlaybackListener> playbackListenersWaitingOnBind;

    public PlayerBindManager(Application application) {
        super();
        this.applicationContext = application;
        bindToPlayerService();
        this.disposeCount = 0;
        queuedActions = new ConcurrentLinkedQueue<>();
        playbackListenersWaitingOnBind = new LinkedList<>();
    }

    private void bindToPlayerService() {
        Intent playerServiceIntent = new Intent(applicationContext, PlayerService.class);
        System.out.println("Service started: " + applicationContext.startService(playerServiceIntent));
        System.out.println("Service bound? " + applicationContext.bindService(playerServiceIntent , this, 0));
    }

    private void unbindFromPlayerService() {
        applicationContext.unbindService(this);
        playerBind = null;
    }

    public void dispose() throws IllegalStateException{
        if(isDisposed()) {
            throw new IllegalStateException("The dispose-method must never be called more than once on each Instance of PlayerBindManager!");
        }
        disposeCount++;
        if(isBound()) {
            unbindFromPlayerService();
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.unbindFromPlayerService();
                }
            });
        }

    }

    public boolean isBound() {
        return playerBind!=null;
    }

    public boolean isDisposed() {
        return disposeCount>0;
    }

    public List<Song> getPlayQueueAsListCopy() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnPlayQueueAsList();
    }

    public List<Song> getBackStackAsListCopy() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnBackStackAsList();
    }

    public int returnCurrentPosition() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnCurrentPosition();
    }

    public int returnCurrentSongDuration() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnCurrentSongDuration();
    }

    public boolean returnRepeatMode() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnRepeatMode();
    }

    public float returnVolumeScale() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnVolumeScale();
    }

    public boolean returnIsPaused() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnIsPaused();
    }

    public Song returnCurrentSong() {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        return playerBind.returnCurrentSong();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        playerBind = (PlayerService.PlayerBind) service;
        for(Runnable action : queuedActions) {
            action.run();
        }
        queuedActions.clear();
        for(IPlaybackListener pbl : playbackListenersWaitingOnBind) {
            putBindNotificationForPlaybackListener(pbl);
        }
        playbackListenersWaitingOnBind.clear();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerBind = null;
    }

    public void playSongWhenReady(final Song song) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.playSongWhenReady(song);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.playSongWhenReady(song);
                }
            });
        }
    }

    public void playAllSongsWhenReady(final Song[] songs) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.playAllSongsWhenReady(songs);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.playAllSongsWhenReady(songs);
                }
            });
        }
    }

    public void playSongDelayed(final int amountOfSongsToDelay, final Song song) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.playSongDelayed(amountOfSongsToDelay, song);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.playSongDelayed(amountOfSongsToDelay, song);
                }
            });
        }
    }

    public void playSongNow(final Song song) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.playSongNow(song);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.playSongNow(song);
                }
            });
        }
    }

    public void pause() throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.pause();
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.pause();
                }
            });
        }
    }

    public void resume()throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.resume();
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.resume();
                }
            });
        }
    }

    public void stop() throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.stop();
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.stop();
                }
            });
        }
    }

    public void next() throws IllegalStateException {
        disposeCheck();
            disposeCheck();
            if(isBound()) {
                playerBind.forward();
            }else {
                queuedActions.add(new Runnable() {
                    @Override
                    public void run() {
                        PlayerBindManager.this.playerBind.forward();
                    }
                });
            }
    }

    public void previous() throws IllegalStateException {
        disposeCheck();
        if (isBound()) {
            playerBind.backward();
        } else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.backward();
                }
            });
        }
    }

    public void jump10SecondsForward() throws IllegalStateException {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        int currentPosition = this.returnCurrentPosition();
        int songDuration = this.returnCurrentSongDuration();
        if(currentPosition>=0 && currentPosition+10000 < songDuration) {
            this.setPlaybackPosition(currentPosition+10000);
        }else {
            this.next();
        }
    }

    public void jump10SecondsBackward() throws IllegalStateException {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        int currentPosition = this.returnCurrentPosition();
        int songDuration = this.returnCurrentSongDuration();
        if(currentPosition<1000 || songDuration<=0) {
            this.previous();
        }else if(currentPosition<10000) {
            this.setPlaybackPosition(1);
        }else if(currentPosition>10000) {
            this.setPlaybackPosition(currentPosition-10000);
        }
    }

    public void increaseVolumeBySomethingLikeOneTenth() throws IllegalStateException {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        float currentVolume = this.returnVolumeScale();
        if(currentVolume<=0.9f) {
            this.setVolumeScale(currentVolume+0.1f);
        }else {
            this.setVolumeScale(1f);
        }
    }

    public void decreaseVolumeBySomethingLikeOneTenth() throws IllegalStateException {
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        float currentVolume = this.returnVolumeScale();
        if(currentVolume>=0.1f) {
            this.setVolumeScale(currentVolume-0.1f);
        }else {
            this.setVolumeScale(0f);
        }
    }

    public void setLoopingMode(final boolean newMode) throws IllegalStateException{
        disposeCheck();
        if (isBound()) {
            playerBind.setLoopingMode(newMode);
        } else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.setLoopingMode(newMode);
                }
            });
        }
    }

    public void shufflePlayQueue() throws IllegalStateException {
        disposeCheck();
        if (isBound()) {
            playerBind.shufflePlayQueue();
        } else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.shufflePlayQueue();
                }
            });
        }
    }

    public void setPlaybackPosition(int millis) throws IllegalStateException{
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        if(millis<0 || millis>playerBind.returnCurrentSongDuration()) {
            throw new IllegalArgumentException("Parameter " + millis + " must be between 0 and" +
                    "the maximum length of the currently playing Song");
        }
        playerBind.setPlaybackPosition(millis);
    }

    public void setVolumeScale(float scale) throws IllegalStateException{
        disposeCheck();
        if(!isBound()) {
            throw new IllegalStateException("PlayerBindManager must have finished Binding for" +
                    "this method to be called!");
        }
        if(scale<0f || scale>1f) {
            throw new IllegalArgumentException("Parameter " + scale + " must be between 0 and" +
                    "1!");
        }
        playerBind.setVolumeScale(scale);
    }

    public void clearPlayQueue()throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.clearPlayQueue();
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.clearPlayQueue();
                }
            });
        }
    }

    public void clearBackStack() throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.clearBackStack();
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.clearBackStack();
                }
            });
        }
    }

    public void removeSongFromPlayQueue(final int positionIndex) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.removeSongFromPlayQueue(positionIndex);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.removeSongFromPlayQueue(positionIndex);
                }
            });
        }
    }

    public void removeSongFromPlayQueue(final Song song) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.removeSongFromPlayQueue(song);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.removeSongFromPlayQueue(song);
                }
            });
        }
    }

    public void removeSongFromBackStack(final int positionIndex) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.removeSongFromBackStack(positionIndex);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.removeSongFromBackStack(positionIndex);
                }
            });
        }
    }

    public void removeSongFromBackStack(final Song song) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.removeSongFromBackStack(song);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.removeSongFromBackStack(song);
                }
            });
        }
    }

    public void addPlaybackListener(final IPlaybackListener playbackListener) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.addPlaybackListener(playbackListener);
            putBindNotificationForPlaybackListener(playbackListener);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.addPlaybackListener(playbackListener);
                }
            });
            playbackListenersWaitingOnBind.add(playbackListener);
        }
    }

    public void removePlaybackListener(final IPlaybackListener playbackListener) throws IllegalStateException {
        disposeCheck();
        if(isBound()) {
            playerBind.removePlaybackListener(playbackListener);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.removePlaybackListener(playbackListener);
                }
            });
        }
    }

    private void disposeCheck() throws  IllegalStateException{
        if(isDisposed()) {
            throw new IllegalStateException("This Instance of PlayerBindManager has already been" +
                    "disposed!");
        }
    }

    private void putBindNotificationForPlaybackListener(final IPlaybackListener listener) {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.playerBindManagerFinishedBinding(PlayerBindManager.this);
            }
        });
    }
}
