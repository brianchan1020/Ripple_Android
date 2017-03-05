package com.example.brianchan.ripple_android;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.player.Player;

import java.util.ArrayList;
import java.util.ListIterator;

import com.spotify.sdk.android.player.Error;

import static com.example.brianchan.ripple_android.Global.player;


/**
 * Created by Parikshit on 2/26/17.
 */

public class Playlist {
    public String list_type = "playlist";
    public String party_id;
    public ArrayList<Song> songs;

    private Party party;
    private PlaylistThread nextSongThread;
    private boolean firstTime;

    Player.OperationCallback op;

    Playlist(Party party){
        this.party = party;
        this.party_id = party.getId();
        firstTime = true;

        op = new Player.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.d("Success", "Callback successful");
            }

            @Override
            public void onError(Error error) {
                Log.d("Error", "Callback unsuccessful");
            }
        };

        songs = new ArrayList<Song>();
    }

    public void enqueue(Song song) {

        Song newSong = song;
        songs.add(newSong);

        String songName = newSong.getTitle();

        // TODO: push song to playlist
    }

    public Song dequeue() {
        return songs.remove(0);
    }

    private void remove(Song song) {
        songs.remove(song);
        //TODO  remove song from playlist
    }

    public void togglePlayPause(){
        if(player.getPlaybackState().isPlaying){
            player.pause(op);
            songs.get(0).markPaused();
            try {
                nextSongThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            if(player.getPlaybackState().positionMs < player.getMetadata().currentTrack.durationMs){
                player.resume(op);
                songs.get(0).markPlaying();
                notify();
            }

        }

    }

    //plays the next song on our playlist
    public void playNextSong(){
        Song currSong = songs.get(0);
        currSong.markFinishedPlaying();

        if(firstTime) {
            nextSongThread = new PlaylistThread(2000, this);
            player.playUri(op, currSong.getUri(), 0, 180000);
            nextSongThread.start();

            firstTime = !firstTime;
        }
        else{
            remove(currSong);
            if(songs.size() != 0) {
                Song nextSong =  songs.get(0);
                nextSongThread = new PlaylistThread(nextSong.getDuration(), this);
                player.playUri(op, nextSong.getUri(), 0, 0);
                nextSongThread.start();
                //hist.push(currSong);
            }
        }
    }

    //skips to next song
    public void skip(){
        nextSongThread.interrupt();
        Song skippedSong = songs.get(0);
        remove(skippedSong);
        skippedSong.markFinishedPlaying();
    }

    public ArrayList<Song> reorder(int fromIndex, int toIndex){
        Song toMove = songs.remove(fromIndex);
        songs.add(toIndex, toMove);
        return songs;
    }

    public ListIterator<Song> listIterator(){
        return songs.listIterator();
    }

    public Song getCurrSong(){ return songs.get(0);}
}
