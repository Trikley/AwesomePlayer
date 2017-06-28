package de.teamawesome.awesomeplayer.playerserviceutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlaybackQueueManager {

    private Queue<String> playQueue;

    public PlaybackQueueManager() {
        super();
        playQueue = new ConcurrentLinkedQueue<>();
    }

    public void appendSong(String pathToSong) {
        playQueue.add(pathToSong);
    }

    public void appendAllSongs(String[] paths) {
        playQueue.addAll(Arrays.asList(paths));
    }

    public void insertInPosition(int positionIndex, String path) {
        ArrayList<String> temp = new ArrayList<>(playQueue.size()+1);
        temp.addAll(playQueue);
        playQueue.clear();
        if(positionIndex>=temp.size()) {
            temp.add(path);
        }else {
            temp.add(positionIndex, path);
        }
        playQueue.addAll(temp);
    }

    public String pollSongPath() {
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
            ArrayList<String> temp = new ArrayList<>(playQueue.size());
            temp.addAll(playQueue);
            playQueue.clear();
            temp.remove(positionIndex);
            playQueue.addAll(temp);
        }
    }

    public List<String> returnListCopy() {
        List<String> result = new LinkedList<String>();
        result.addAll(playQueue);
        return result;
    }
}
