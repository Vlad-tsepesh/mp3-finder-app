package com.example.mp3.infrastructure.client.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SpotifyRequest(String trackName,
        String title,
        List<ArtistDto> artists) {
}
