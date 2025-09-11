package com.example.mp3.infrastructure.client;

import com.example.mp3.application.exception.TrackNotFoundException;
import com.example.mp3.domain.port.out.SpotifyClient;
import com.example.mp3.infrastructure.client.dto.SpotifyRequest;
import com.example.mp3.infrastructure.client.dto.SpotifyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyAdapter implements SpotifyClient {

    private final SpotifyApi spotifyApi;

    public SpotifyResponse searchTracksUrl(SpotifyRequest request) {
        try {
            var paging = spotifyApi
                    .searchTracks(request.trackName())
                    .build()
                    .execute();

            return filterTrack(paging, request);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return new SpotifyResponse(null);
    }

    private SpotifyResponse filterTrack(Paging<Track> trackPaging, SpotifyRequest request) {
        return Arrays.stream(trackPaging.getItems())
                .filter(track -> existsByArtist(track, request.artist()))
                .filter(track -> existsByTitle(track, request.title()))
                .findFirst()
                .map(SpotifyAdapter::buildResponse)
                .orElseThrow(() -> new TrackNotFoundException("Track not found: " + request.trackName()));
    }

    private static SpotifyResponse buildResponse(Track track) {
        SpotifyResponse response =  SpotifyResponse.builder()
                .uri(track.getId())
                .build();

        log.info("ID found : {}", response.uri());
        return response;
    }

    private boolean existsByArtist(Track track, String expectedArtist) {
        return Arrays.stream(track.getAlbum().getArtists())
                .anyMatch(a -> a.getName().equalsIgnoreCase(expectedArtist.trim()));
    }

    private boolean existsByTitle(Track track, String expectedTitle) {
        return track.getName().equalsIgnoreCase(expectedTitle.trim());
    }
}
