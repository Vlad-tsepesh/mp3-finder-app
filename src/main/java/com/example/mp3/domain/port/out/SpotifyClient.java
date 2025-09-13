package com.example.mp3.domain.port.out;

import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

public interface SpotifyClient {
    List<Track> searchForTracks(String track);
}
