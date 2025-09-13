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
        try {
            return Arrays.stream(spotifyApi
                            .searchTracks(trackName)
                            .build()
                            .execute()
                            .getItems())
                    .toList();

        } catch (Exception ex) {
            log.error("Spotify search failed: {}", ex.getMessage());
        }
        return List.of();
    }

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
