package com.example.mp3.application.service;

import com.example.mp3.infrastructure.client.SpotifyAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpotifyUserService {
    private final SpotifyAdapter spotifyAdapter;

    public void findArtistSpotifyId(String artistName){

        spotifyAdapter.findArtistSpotifyId(artistName);
    }
}
