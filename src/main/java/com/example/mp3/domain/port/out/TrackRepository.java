package com.example.mp3.domain.port.out;

import com.example.mp3.domain.model.Track;

import java.util.List;

public interface TrackRepository {
    Track saveTrack(Track track);
    List<Track> saveTracks(List<Track> tracks);
    boolean existsByTrackName(String trackName);
    boolean haveUrl(String trackName);
    List<String> fetchTracksWithEmptyUrl();
}
