package de.teamawesome.awesomeplayer.playerserviceutils;

import de.teamawesome.awesomeplayer.model.Song;

public interface IPlaybackListener {

    void newSongStartsPlaying(Song song);
}
