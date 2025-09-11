package com.example.mp3.domain.port.out;

import com.example.mp3.domain.model.Track;

import java.util.List;

public interface TrackRepository {
    void saveTrack(Track track);
    boolean isNewTrack(Track track);
    List<Track> fetchTracksWithEmptyUrl();
    List<Track> fetchALlTracks();
}
