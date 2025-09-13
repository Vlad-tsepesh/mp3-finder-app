package com.example.mp3.infrastructure.persistance;

import com.example.mp3.domain.model.TrackEntity;
//import com.example.mp3.domain.repository.JpaArtistRepository;
import com.example.mp3.domain.repository.JpaTrackRepository;
import com.example.mp3.domain.port.out.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryAdapter implements TrackRepository {
    private final JpaTrackRepository jpaTrackRepository;
//    private final JpaArtistRepository jpaArtistRepository;


    @Override
    public void saveTrack(TrackEntity track) {
        jpaTrackRepository.save(track);
    }

    @Override
    public boolean isNewTrack(TrackEntity track) {
        String trackName = track.getTrackName();
        return jpaTrackRepository.isNewTrack(trackName);
    }

    @Override
    public List<TrackEntity> fetchTracksWithMissingSpotifyId() {
        return jpaTrackRepository.fetchTracksWithMissingSpotifyIdWithArtists();
    }

    @Override
    public List<TrackEntity> fetchALlTracks() {
        return jpaTrackRepository.findAll();
    }
}
