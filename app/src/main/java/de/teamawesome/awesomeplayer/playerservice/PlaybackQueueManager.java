package de.teamawesome.awesomeplayer.playerservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.teamawesome.awesomeplayer.model.Song;

class PlaybackQueueManager {

    private Queue<Song> playQueue;
    private Stack<Song> backStack;

    private Lock queueLock;
    private Lock stackLock;

    PlaybackQueueManager() {
        super();
        playQueue = new ConcurrentLinkedQueue<>();
        backStack = new Stack<>();
        queueLock = new ReentrantLock();
        stackLock = new ReentrantLock();
    }

    void appendSongToPlayQueue(Song song) {
        queueLock.lock();
        playQueue.add(song);
        queueLock.unlock();
    }

    void addSongToBackStack(Song song) {
        stackLock.lock();
        backStack.add(song);
        stackLock.unlock();
    }

    void appendAllSongsToPlayQueue(Song[] songs) {
        queueLock.lock();
        playQueue.addAll(Arrays.asList(songs));
        queueLock.unlock();
    }

    void addAllSongsToBackStack(Song[] songs) {
        stackLock.lock();
        backStack.addAll(Arrays.asList(songs));
        stackLock.unlock();
    }

    void insertInPosition(int positionIndex, Song song) {
        if(positionIndex>=0) {
            queueLock.lock();
            ArrayList<Song> temp = new ArrayList<>(playQueue.size()+1);
            temp.addAll(playQueue);
            playQueue.clear();
            if(positionIndex>=temp.size()) {
                temp.add(song);
            }else {
                temp.add(positionIndex, song);
            }
            playQueue.addAll(temp);
            queueLock.unlock();
        }else {
            stackLock.lock();
            positionIndex = positionIndex*-1;
            if(positionIndex>=backStack.size()) {
                backStack.add(backStack.size()-1, song);
            }else {
                backStack.add(positionIndex, song);
            }
            stackLock.unlock();
        }

    }

    Song pollSongFromPlayQueue() {
        queueLock.lock();
        Song result = playQueue.poll();
        queueLock.unlock();
        return result;
    }

    Song popSongFormBackStack() {
        stackLock.lock();
        Song result;
        try {
            result = backStack.pop();
        }catch (EmptyStackException e) {
            result = null;
        }
        stackLock.unlock();
        return result;
    }

    int returnQueueLength() {
        return playQueue.size();
    }

    int returnStackSize() {
        return backStack.size();
    }

    void clearQueue() {
        queueLock.lock();
        playQueue.clear();
        queueLock.unlock();
    }

    void clearStack() {
        stackLock.lock();
        backStack.clear();
        stackLock.unlock();
    }

    void removeSongFromQueue(int positionIndex) {
        if(positionIndex>=playQueue.size()) {
            throw new IllegalArgumentException("Argument " + positionIndex + " is greater or equal to " +
                    "Queue length: " + playQueue.size());
        }else {
            queueLock.lock();
            ArrayList<Song> temp = new ArrayList<>(playQueue.size());
            temp.addAll(playQueue);
            playQueue.clear();
            temp.remove(positionIndex);
            playQueue.addAll(temp);
            queueLock.unlock();
        }
    }

    void removeSongFromQueue(Song song) {
        queueLock.lock();
        playQueue.remove(song);
        queueLock.unlock();
    }

    void removeSongFromBackStack(int positionIndex) {
        if(positionIndex>=backStack.size()) {
            throw new IllegalArgumentException("Argument " + positionIndex + " is greater or equal to " +
                    "Stack size: " + backStack.size());
        }else {
            stackLock.lock();
            backStack.remove(positionIndex);
            stackLock.unlock();
        }
    }

    void removeSongFromBackStack(Song song) {
        stackLock.lock();
        backStack.remove(song);
        stackLock.unlock();
    }

    List<Song> returnListCopyOfPlayQueue() {
        List<Song> result = new LinkedList<>();
        queueLock.lock();
        result.addAll(playQueue);
        queueLock.unlock();
        return result;
    }

    List<Song> returnListCopyOfBackStack() {
        List<Song> result = new LinkedList<>();
        stackLock.lock();
        result.addAll(backStack);
        stackLock.unlock();
        return result;
    }
}
