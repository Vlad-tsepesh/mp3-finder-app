package com.example.mp3.domain.port.out;

import com.example.mp3.infrastructure.client.dto.SpotifyRequest;
import com.example.mp3.infrastructure.client.dto.SpotifyResponse;
import reactor.core.publisher.Mono;

public interface SpotifyClient {
    SpotifyResponse searchTracksUrl(SpotifyRequest request);
}
