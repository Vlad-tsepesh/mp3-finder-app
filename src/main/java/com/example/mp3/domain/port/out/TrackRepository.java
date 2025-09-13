package com.example.mp3.domain.port.out;

import com.example.mp3.domain.model.TrackEntity;

import java.util.List;

public interface TrackRepository {
    void saveTrack(TrackEntity track);
    boolean isNewTrack(TrackEntity track);
    List<TrackEntity> fetchTracksWithMissingSpotifyId();
    List<TrackEntity> fetchALlTracks();
}
