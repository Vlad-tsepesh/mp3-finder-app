package com.example.mp3.domain.repository;

import com.example.mp3.domain.model.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaArtistRepository extends JpaRepository<ArtistEntity, Long> {

    @Query("""
           SELECT a FROM ArtistEntity a
           WHERE a.id = ?1 AND (a.spotifyId IS NULL OR a.spotifyId = '')
           """)
    Optional<ArtistEntity> fetchArtistsByIdWithMissingSpotifyID(Long id);
}