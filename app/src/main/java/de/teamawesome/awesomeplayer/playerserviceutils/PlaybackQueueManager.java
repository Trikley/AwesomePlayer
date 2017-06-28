package de.teamawesome.awesomeplayer.playerserviceutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.teamawesome.awesomeplayer.model.Song;

public class PlaybackQueueManager {

    private Queue<Song> playQueue;

    public PlaybackQueueManager() {
        super();
        playQueue = new ConcurrentLinkedQueue<>();
    }

    public void appendSong(Song song) {
        playQueue.add(song);
    }

    public void appendAllSongs(Song[] songs) {
        playQueue.addAll(Arrays.asList(songs));
    }

    public void insertInPosition(int positionIndex, Song song) {
        ArrayList<Song> temp = new ArrayList<>(playQueue.size()+1);
        temp.addAll(playQueue);
        playQueue.clear();
        if(positionIndex>=temp.size()) {
            temp.add(song);
        }else {
            temp.add(positionIndex, song);
        }
        playQueue.addAll(temp);
    }

    public Song pollSongPath() {
        return playQueue.poll();
    }

    public int returnQueueLength() {
        return playQueue.size();
    }

    public void clearQueue() {
        playQueue.clear();
    }

    public void removeSongFromQueue(int positionIndex) {
        if(positionIndex>=playQueue.size()) {
            throw new IllegalArgumentException("Argument " + positionIndex + " is greater or equal to " +
                    "Queue length: " + playQueue.size());
        }else {
            ArrayList<Song> temp = new ArrayList<>(playQueue.size());
            temp.addAll(playQueue);
            playQueue.clear();
            temp.remove(positionIndex);
            playQueue.addAll(temp);
        }
    }

    public List<Song> returnListCopy() {
        List<Song> result = new LinkedList<>();
        result.addAll(playQueue);
        return result;
    }
}
