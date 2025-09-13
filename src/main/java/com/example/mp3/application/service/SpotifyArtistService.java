package com.example.mp3.application.service;

import com.example.mp3.infrastructure.client.SpotifyAdapter;
import com.example.mp3.utils.Matcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Artist;

@Service
@RequiredArgsConstructor
public class SpotifyArtistService {
    private final SpotifyAdapter spotifyAdapter;

    public String findArtistSpotifyId(String artistName){
        return spotifyAdapter.findArtistSpotifyId(artistName).stream()
                .filter(response -> matchByName(response.getName(), artistName))
                .map(Artist::getId).findFirst().orElse("");
    }

    private boolean matchByName(String responseName, String expectedName) {
        return Matcher.similarity(responseName, expectedName) > 0.95;
    }
}
