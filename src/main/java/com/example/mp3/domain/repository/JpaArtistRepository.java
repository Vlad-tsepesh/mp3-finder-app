package com.example.mp3.domain.repository;

import com.example.mp3.domain.model.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JpaArtistRepository extends JpaRepository<ArtistEntity, Long> {

    @Query("""
           SELECT a FROM ArtistEntity a
           WHERE a.spotifyId IS NULL OR a.spotifyId = ''
           """)
    List<ArtistEntity> fetchArtistsWithMissingSpotifyID();

    @Query("SELECT a FROM ArtistEntity a WHERE a.name IN :names")
    List<ArtistEntity> findAllByName(@Param("names") Collection<String> names);
}