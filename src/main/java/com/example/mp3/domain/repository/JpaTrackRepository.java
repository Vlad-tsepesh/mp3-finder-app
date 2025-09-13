package com.example.mp3.domain.repository;

import com.example.mp3.domain.model.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaTrackRepository extends JpaRepository<TrackEntity, String> {

    @Query("""
           SELECT t.spotifyId FROM TrackEntity t
           WHERE t.trackName = ?1
           """)
    String getUrlByTrackName(String trackName);

    @Query("""
           SELECT DISTINCT t FROM TrackEntity t
           LEFT JOIN FETCH t.artists
           WHERE t.spotifyId IS NULL OR t.spotifyId = ''
           """)
    List<TrackEntity> fetchTracksWithMissingSpotifyIdWithArtists();

    @Query("""
           SELECT CASE WHEN COUNT(t) = 0 THEN true ELSE false END
           FROM TrackEntity t
           WHERE t.trackName = ?1
           """)
    boolean isNewTrack(String trackName);

    @Query("""
           SELECT DISTINCT t FROM TrackEntity t LEFT JOIN FETCH t.artists
           """)
    List<TrackEntity> findAllWithArtists();
}
