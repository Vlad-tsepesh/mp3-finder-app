package com.example.mp3.domain.port.out;

import com.example.mp3.domain.model.ArtistEntity;
import com.example.mp3.domain.model.TrackEntity;

import java.util.Collection;
import java.util.List;

public interface SpotifyRepository {
    void saveTrack(TrackEntity track);

    boolean isNewTrack(TrackEntity track);

    List<TrackEntity> fetchTracksWithMissingSpotifyId();

    List<TrackEntity> fetchALlTracks();

    List<ArtistEntity> fetchArtistsWithNoSpotifyId();

    List<ArtistEntity> fetchAllArtists();

    ArtistEntity saveArtist(ArtistEntity artist);

    List<ArtistEntity> findAllArtistsByNameIn(Collection<String> names);

    ArtistEntity save(ArtistEntity build);

    void saveAllTracks(List<TrackEntity> tracks);
}
