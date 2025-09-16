package com.example.mp3.application.service;

import com.example.mp3.domain.port.out.SpotifyClient;
import com.example.mp3.infrastructure.client.SpotifyAdapter;
import com.example.mp3.utils.Matcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Artist;

@Service
@RequiredArgsConstructor
public class SpotifyArtistService {
    private final SpotifyClient spotifyClient;

    public String findArtistSpotifyId(String artistName){
        return spotifyClient.findArtistSpotifyId(artistName).stream()
                .filter(response -> matchByName(response.getName(), artistName))
                .map(Artist::getId).findFirst().orElse("");
    }

    private boolean matchByName(String responseName, String expectedName) {
        return Matcher.similarity(responseName, expectedName) > 0.95;
    }
}
