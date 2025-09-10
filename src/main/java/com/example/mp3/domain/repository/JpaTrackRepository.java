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
            SELECT t.trackName FROM Track t
            WHERE t.url IS NULL
            """)
    List<String> fetchTracksWithEmptyUrl();
}
