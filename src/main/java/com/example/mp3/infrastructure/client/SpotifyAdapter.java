package com.example.mp3.infrastructure.client;

import com.example.mp3.domain.port.out.SpotifyClient;
import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyAdapter implements SpotifyClient {

    private final SpotifyApi spotifyApi;

    @Override
    public List<Track> searchForTracks(String trackName) {
        log.info("Started search with item: {}", trackName);
        try {
            List<Track> response = Arrays.stream(spotifyApi
                            .searchTracks(trackName)
                            .build()
                            .execute()
                            .getItems())
                    .toList();
//            log.info("Got response with item: {}", response.get(0).getName());
            return response;

        } catch (Exception ex) {
            log.error("Spotify search failed: {}", ex.getMessage());
        }
        return List.of();
    }

    @Override
    public List<Track> fetchSpotifyTrackById(String artistId) {
        try {
//            log.info("Starting searching track by artist id: {}", artistId);
            return List.of(spotifyApi.getArtistsTopTracks(artistId, CountryCode.valueOf("US"))
                    .build()
                    .execute());

        } catch (Exception ex) {
            log.error("Spotify search failed: {}, {}",artistId, ex.getMessage());
        }
        return List.of();
    }

    @Override
    public List<Artist> findArtistSpotifyId(String artistName) {
        try {
            List<Artist> artists =  Arrays.stream(spotifyApi
                            .searchArtists(artistName)
                            .build()
                            .execute()
                            .getItems())
                    .toList();

            return artists;

        } catch (Exception ex) {
            log.error("Spotify search failed: {}", ex.getMessage());
        }
        return List.of();
    }
}
