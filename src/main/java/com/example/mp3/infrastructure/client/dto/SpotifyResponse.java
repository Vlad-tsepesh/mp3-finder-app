package com.example.mp3.infrastructure.client.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record SpotifyResponse(String uri) {
}
