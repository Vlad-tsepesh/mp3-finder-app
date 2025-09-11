package com.example.mp3.infrastructure.persistance;

import com.example.mp3.domain.model.Track;
import com.example.mp3.domain.repository.JpaTrackRepository;
import com.example.mp3.domain.port.out.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrackRepositoryAdapter implements TrackRepository {
    private final JpaTrackRepository jpaTrackRepository;


    @Override
    public void saveTrack(Track track) {
        jpaTrackRepository.save(track);
    }

    @Override
    public boolean isNewTrack(Track track) {
        String trackName = track.getTrackName();
        return jpaTrackRepository.isNewTrack(trackName);
    }

    @Override
    public List<Track> fetchTracksWithEmptyUrl() {
        return jpaTrackRepository.fetchTracksWithEmptyUrl();
    }

    @Override
    public List<Track> fetchALlTracks() {
        return jpaTrackRepository.findAll();
    }
}
