package com.example.mp3.infrastructure.persistance;

import com.example.mp3.domain.model.ArtistEntity;
import com.example.mp3.domain.model.TrackEntity;
import com.example.mp3.domain.repository.JpaArtistRepository;
import com.example.mp3.domain.repository.JpaTrackRepository;
import com.example.mp3.domain.port.out.SpotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RepositoryAdapter implements SpotifyRepository {
    private final JpaTrackRepository jpaTrackRepository;
    private final JpaArtistRepository jpaArtistRepository;


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

    @Override
    public List<ArtistEntity> fetchArtistsWithNoSpotifyId() {
        return jpaArtistRepository.fetchArtistsWithMissingSpotifyID();
    }

    @Override
    public List<ArtistEntity> fetchAllArtists(){
        return jpaArtistRepository.findAll();
    }

    @Override
    public ArtistEntity saveArtist(ArtistEntity artist) {
        return jpaArtistRepository.save(artist);
    }

    @Override
    public List<ArtistEntity> findAllArtistsByNameIn(Collection<String> names) {
        return jpaArtistRepository.findAllByName(names);
    }

    @Override
    public ArtistEntity save(ArtistEntity build) {
        return jpaArtistRepository.save(build);
    }

    @Override
    public void saveAllTracks(List<TrackEntity> tracks) {
        jpaTrackRepository.saveAll(tracks);
        log.info("Saved all tracks.");
    }

}
