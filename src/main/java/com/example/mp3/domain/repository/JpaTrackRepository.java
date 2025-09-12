package com.example.mp3.domain.repository;

import com.example.mp3.domain.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaTrackRepository extends JpaRepository<Track, String>{
    @Query(value= """
            SELECT url FROM Track t
            WHERE t.trackName = ?1
            """)
    String getUrlByTrackName(String trackName);

    @Query(value= """
            SELECT t FROM Track t
            WHERE t.url IS NULL OR t.url=''
            """)
    List<Track> fetchTracksWithEmptyUrl();

    @Query(value = """
            SELECT COUNT(t) = 0  FROM Track t
            WHERE t.trackName != ?1
            """)
    boolean isNewTrack(String trackName);

    @Query("SELECT t FROM Track t LEFT JOIN FETCH t.artists")
    List<Track> findAllWithArtists();

}
