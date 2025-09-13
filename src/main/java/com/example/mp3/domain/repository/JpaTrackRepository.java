package com.example.mp3.domain.repository;

import com.example.mp3.domain.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaTrackRepository extends JpaRepository<Track, String> {

    @Query("""
           SELECT t.url FROM Track t
           WHERE t.trackName = ?1
           """)
    String getUrlByTrackName(String trackName);

    @Query("""
           SELECT t FROM Track t
           LEFT JOIN FETCH t.artists
           WHERE t.url IS NULL OR t.url = ''
           """)
    List<Track> fetchTracksWithEmptyUrlWithArtists();

    @Query("""
           SELECT CASE WHEN COUNT(t) = 0 THEN true ELSE false END
           FROM Track t
           WHERE t.trackName = ?1
           """)
    boolean isNewTrack(String trackName);

    @Query("""
           SELECT DISTINCT t FROM Track t LEFT JOIN FETCH t.artists
           """)
    List<Track> findAllWithArtists();
}
