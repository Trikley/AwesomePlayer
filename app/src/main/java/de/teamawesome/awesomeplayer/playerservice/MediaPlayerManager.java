package de.teamawesome.awesomeplayer.playerservice;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.teamawesome.awesomeplayer.model.Song;

class MediaPlayerManager extends HandlerThread implements MediaPlayer.OnCompletionListener{

    private long timestampLastAction;
    private static long idletimeTillQueueCheck = 500;

    private PlaybackQueueManager playManager;
    private PlayerService playerService;

    private Handler handler;
    private boolean prepared;
    private boolean finishingUp;

    private Queue<IPlaybackListener> playbackListeners;
    private MediaPlayer mediaPlayer;

    private Song currentSong;
    private boolean paused;
    private float volumeScale;
    private boolean looping;

    private boolean quit;

    MediaPlayerManager(PlaybackQueueManager playManager, PlayerService playerService) {
        super("MediaPlayManagerThread");
        this.playManager = playManager;
        this.playerService = playerService;
        paused = false;
        prepared = false;
        quit = false;
        finishingUp = false;

        volumeScale = 0.5f;
        looping = false;

        playbackListeners = new LinkedBlockingQueue<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        resetMediaPlayer();

        timestampLastAction = 0;
    }


    @Override
    protected void onLooperPrepared() {
        handler = new Handler(this.getLooper());
        synchronized(playerService) {
            prepared = true;
            playerService.notifyAll();
        }
        putPlayQueueCheckInLooper();
    }

    boolean isPrepared() {
        return prepared;
    }

    private void startPlayingSong(final Song song){
        //TODO Remove Toast
        Toast.makeText(playerService.getApplicationContext(), "Playing: " + song.getTitle(), Toast.LENGTH_SHORT).show();
        try {
            timestampLastAction = System.currentTimeMillis();
            resetMediaPlayer();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            for(IPlaybackListener playbackListener: playbackListeners) {
                playbackListener.newSongStartsPlaying(song);
            }
        }catch (IOException e) {
            throw new IllegalArgumentException("The Path to the song (" +
                    song.getTitle() + " Path: " + song.getPath() +
                    ") supplied as Argument is invalid!", e);
        }
    }

    void stopPlayback() {
        currentSong = null;
        paused = false;
        playManager.clearQueue();
        playManager.clearStack();
        handler.post(new Runnable() {
            @Override
            public void run() {
                resetMediaPlayer();
                finishSong(false);
                timestampLastAction = System.currentTimeMillis();
            }
        });
    }

    void stopCurrentSong(final boolean putToBackstack) {
        paused = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                resetMediaPlayer();
                finishSong(putToBackstack);
                timestampLastAction = System.currentTimeMillis();
            }
        });
    }

    void pausePlayback() {
        if(mediaPlayer.isPlaying()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.pause();
                    timestampLastAction = System.currentTimeMillis();
                    paused = true;
                    for(IPlaybackListener playbackListener : playbackListeners) {
                        playbackListener.playbackPaused();
                    }
                }
            });
        }
    }

    void resumePlayback() {
        if(paused) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.start();
                    timestampLastAction = System.currentTimeMillis();
                    paused = false;
                    for(IPlaybackListener playbackListener : playbackListeners) {
                        playbackListener.playbackResumed();
                    }
                }
            });
        }
    }

    void setLoopingMode(final boolean newMode) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer.isPlaying() || paused) {
                    mediaPlayer.setLooping(true);
                    looping = newMode;
                    timestampLastAction = System.currentTimeMillis();
                }

            }
        });
    }

    void setPlaybackPosition(final int millis) {
        if(millis>0 && millis<returnSongDuration()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer.isPlaying() || paused) {
                        mediaPlayer.seekTo(millis);
                        timestampLastAction = System.currentTimeMillis();
                    }
                }
            });
        }
    }

    void setVolume(final float scaler) {
        if(scaler>=0f && scaler<=1f) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer.isPlaying() || paused) {
                        float adjustedScaler;
                        if(scaler == 0f) {
                            adjustedScaler = 0f;
                        } else {
                            double scalerPowered = Math.pow((double) scaler, 0.75d);
                            adjustedScaler = (float) Math.pow(10d, (1d - (1d / scalerPowered)));
                            if (adjustedScaler < 0f) {
                                adjustedScaler = 0f;
                            } else if (adjustedScaler > 1f) {
                                adjustedScaler = 1f;
                            }
                        }
                        mediaPlayer.setVolume(adjustedScaler, adjustedScaler);
                        volumeScale = scaler;
                    }
                }
            });
        }
    }

    float returnVolume() {
        return volumeScale;
    }

    int returnCurrentPosition() {
        if(mediaPlayer.isPlaying() || paused) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    int returnSongDuration() {
        if(mediaPlayer.isPlaying() || paused) {
            return mediaPlayer.getDuration();
        }else {
            return 0;
        }
    }

    boolean returnRepeatMode() {
        return looping;
    }

    void addPlaybackListener(IPlaybackListener playbackListener) {
        playbackListeners.add(playbackListener);
    }

    void removePlaybackListener(IPlaybackListener playbackListener) {
        playbackListeners.remove(playbackListener);
    }

    Song getCurrentSong() {
        return currentSong;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finishSong(true);
    }

    private void finishSong(boolean inBackstack) {
        if(finishingUp) {
            return;
        }
        if(currentSong!=null && inBackstack) {
            playManager.addSongToBackStack(currentSong);
        }
        final Song next = playManager.pollSongFromPlayQueue();
        currentSong = next;
        if(next!=null) {
            finishingUp = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    timestampLastAction = System.currentTimeMillis();
                    startPlayingSong(next);
                    finishingUp = false;
                }
            });
        }else {
            for(IPlaybackListener playbackListener : playbackListeners) {
                playbackListener.playbackStopped();
            }
        }
    }

    private void resetMediaPlayer() {
        mediaPlayer.reset();
        mediaPlayer.setVolume(volumeScale, volumeScale);
        mediaPlayer.setLooping(looping);
    }

    @Override
    public boolean quit() {
        if(quit) {
            return false;
        }
        quit = true;
        currentSong = null;
        paused = false;
        mediaPlayer.release();
        return super.quit();
    }

    boolean isQuit() {
        return quit;
    }

    Queue<IPlaybackListener> getPlaybackListeners() {
        return playbackListeners;
    }

    private void putPlayQueueCheckInLooper() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(System.currentTimeMillis()-timestampLastAction>idletimeTillQueueCheck) {
                    if (!(mediaPlayer.isPlaying() || paused)) {
                        if (playManager.returnQueueLength() > 0) {
                            finishSong(false);
                        }
                    }
                }
                putPlayQueueCheckInLooper();
            }
        }, 200);
    }
}
