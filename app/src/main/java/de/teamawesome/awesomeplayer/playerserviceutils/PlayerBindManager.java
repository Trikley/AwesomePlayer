package de.teamawesome.awesomeplayer.playerserviceutils;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.teamawesome.awesomeplayer.PlayerService;
import de.teamawesome.awesomeplayer.model.Song;

public class PlayerBindManager implements ServiceConnection{

    private PlayerService.PlayerBind playerBind;
    private Application applicationContext;

    private int disposeCount;
    private Queue<Runnable> queuedActions;

    public PlayerBindManager(Application application) {
        super();
        this.applicationContext = application;
        bindToPlayerService();
        this.disposeCount = 0;
        queuedActions = new ConcurrentLinkedQueue<>();
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

    public void dispose() {
        if(disposeCount>0) {
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        playerBind = (PlayerService.PlayerBind) service;
        for(Runnable action : queuedActions) {
            new android.os.Handler(Looper.getMainLooper()).post(action);
        }
        queuedActions.clear();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerBind = null;
    }

    public void playSongWhenReady(final Song song) {
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

    public void playAllSongsWhenReady(final Song[] songs) {
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

    public void playSongDelayed(final int amountOfSongsToDelay, final Song song) throws  IllegalStateException{
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

    public void playSongNow(final Song song) throws  IllegalStateException{
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

    public void pause() throws  IllegalStateException{
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

    public void resume() throws  IllegalStateException{
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

    public void stop()throws  IllegalStateException {
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

    public void clearPlayQueue()throws  IllegalStateException {
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

    public void removeSongFromPlayQueue(final int positionIndex) throws  IllegalStateException{
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

    public void addPlaybackListener(final IPlaybackListener playbackListener) throws  IllegalStateException{
        if(isBound()) {
            playerBind.addPlaybackListener(playbackListener);
        }else {
            queuedActions.add(new Runnable() {
                @Override
                public void run() {
                    PlayerBindManager.this.playerBind.addPlaybackListener(playbackListener);
                }
            });
        }
    }

    public void removePlaybackListener(final IPlaybackListener playbackListener) throws  IllegalStateException{
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

}
