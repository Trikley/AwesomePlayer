package de.teamawesome.awesomeplayer.playerservice;

import de.teamawesome.awesomeplayer.model.Song;

public interface IPlaybackListener {

    void playerBindManagerFinishedBinding(PlayerBindManager pbm);

    void newSongStartsPlaying(Song song);

    void playbackPaused();

    void playbackResumed();

    void playbackStopped();

    void volumeChanged(float newVolume);

}
