package com.example.mp3.infrastructure.client;

import com.example.mp3.application.exception.TrackNotFoundException;
import com.example.mp3.domain.port.out.SpotifyClient;
import com.example.mp3.infrastructure.client.dto.ArtistDto;
import com.example.mp3.infrastructure.client.dto.SpotifyRequest;
import com.example.mp3.infrastructure.client.dto.SpotifyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyAdapter implements SpotifyClient {

    private final SpotifyApi spotifyApi;

    @Override
    public SpotifyResponse searchTracksUrl(SpotifyRequest request) {
        try {
            Paging<Track> paging = fetchMatchingTracks(request.trackName());
            return filterTrack(paging, request);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return new SpotifyResponse(null);
    }

    private Paging<Track> fetchMatchingTracks(String trackName) throws IOException, ParseException, SpotifyWebApiException {
        return spotifyApi
                .searchTracks(trackName)
                .build()
                .execute();
    }

//    private SpotifyResponse filterTrack(Paging<Track> trackPaging, SpotifyRequest request) {
//        return Arrays.stream(trackPaging.getItems())
//                .filter(track -> existsByTitle(track, request.title()))
//                .filter(track -> existsByArtist(track, request.artists()))
//                .findFirst()
//                .map(SpotifyAdapter::buildResponse)
//                .orElseThrow(() -> new TrackNotFoundException("Track not found: " + request.trackName()));
//    }

    private SpotifyResponse filterTrack(Paging<Track> trackPaging, SpotifyRequest request) {
        System.out.println(request.trackName());
        System.out.println("-------------------------------------------------------------------------------------------");
        Optional<Track> best = Arrays.stream(trackPaging.getItems())
                .map(track -> {
                    String combined = track.getName() + " - " + Arrays.stream(track.getArtists())
                        .map(ArtistSimplified::getName)
                        .collect(Collectors.joining(", "));
                    double score = similarity(request.trackName(), combined);
                    return new AbstractMap.SimpleEntry<>(track, score);
                    })
                .filter(entry -> entry.getValue() >= 0.7)
                        .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey);
        best.stream().map(track -> track.getName() + " - " + Arrays.stream(track.getArtists()).map(ArtistSimplified::getName)
                .collect(Collectors.joining(", "))).forEach(System.out::println);

        System.out.println("-------------------------------------------------------------------------------------------");

        return SpotifyResponse.builder().build();

//                .filter(track -> existsByTitle(track, request.title()))
//                .filter(track -> existsByArtist(track, request.artists()))
//                .findFirst()
//                .map(SpotifyAdapter::buildResponse)
//                .orElseThrow(() -> new TrackNotFoundException("Track not found: " + request.trackName()));
    }

    private static SpotifyResponse buildResponse(Track track) {
        SpotifyResponse response =  SpotifyResponse.builder()
                .uri(track.getId())
                .build();

        log.info("ID found : {}", response.uri());
        return response;
    }

    private boolean matchesSpotifyRequest(Track track, SpotifyRequest request){
        return true;
    }

    private boolean existsByArtist(Track track, List<ArtistDto> expectedArtists) {
        return Arrays.stream(track.getAlbum().getArtists()).allMatch(
                artist -> matchByAnyArtist(artist, expectedArtists));
    }

    private boolean matchByAnyArtist(ArtistSimplified artistSimplified, List<ArtistDto> expectedArtist) {
        return expectedArtist.stream()
                .anyMatch(a -> a.artist().equalsIgnoreCase(artistSimplified.getName().trim()));
    }

    private boolean existsByTitle(Track track, String expectedTitle) {
        return track.getName().equalsIgnoreCase(expectedTitle.trim());
    }

    private static double similarity(String s1, String s2) {
        JaroWinklerSimilarity jw = new JaroWinklerSimilarity();
        return jw.apply(s1, s2);
    }
}
