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
    public Track saveTrack(Track track) {
        return jpaTrackRepository.save(track);
    }

    @Override
    public List<Track> saveTracks(List<Track> tracks) {
        return jpaTrackRepository.saveAll(tracks);
    }

    @Override
    public boolean existsByTrackName(String trackName) {
        return jpaTrackRepository.existsById(trackName);
    }

    @Override
    public boolean haveUrl(String trackName) {
        return !(jpaTrackRepository.getUrlByTrackName(trackName)==null);

    }

    @Override
    public List<String> fetchTracksWithEmptyUrl() {
        return jpaTrackRepository.fetchTracksWithEmptyUrl();
    }
}
