package com.example.mp3.infrastructure.client.dto;

import lombok.Builder;

@Builder
public record SpotifyRequest(String trackName,
        String title,
        String artist) {
}
