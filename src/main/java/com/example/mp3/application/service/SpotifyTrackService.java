package com.example.mp3.application.service;

import com.example.mp3.domain.model.ArtistEntity;
import com.example.mp3.domain.model.TrackEntity;
import com.example.mp3.infrastructure.client.SpotifyAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.mp3.utils.Matcher.similarity;

@Service
@RequiredArgsConstructor
public class SpotifyTrackService {

    private static final AtomicInteger passedTracks = new AtomicInteger(0); //for debugging
    private final SpotifyAdapter spotifyAdapter;

    public String findTrackUriId(TrackEntity entity) {
        List<Track> spotifyTracks = spotifyAdapter.searchForTracks(entity.getTrackName());
        Optional<Track> bestMatch = findBestMatch(entity, spotifyTracks);
        bestMatch.ifPresent(track -> printBestMatches(track, entity));
        return bestMatch.stream().map(Track::getId).collect(Collectors.joining());
    }

    private Optional<Track> findBestMatch(TrackEntity entity, List<Track> tracks) {

        return tracks.stream()
                // 1. filter by track similarity threshold
                .filter(track -> similarity(entity.getTitle(), track.getName()) > 0.65)
                .filter(track -> similarity(formatArtists(entity), formatArtists(track)) > 0.65)
                // 2. calculate full score for remaining tracks
                .map(track -> {
                    double fullScore = similarity(entity.getTrackName(), formatTrackName(track));
                    return new AbstractMap.SimpleEntry<>(track, fullScore);
                })
                // 3. find track with highest combined score
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // debugging method
    private void printBestMatches(Track track, TrackEntity entity) {
        int currentCount = passedTracks.incrementAndGet();
        System.out.println(currentCount);
        System.out.println(entity.getTrackName());
        System.out.println(formatTrackName(track));
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    private String formatTrackName(Track track) {
        return track.getName() + " - " + joinNames(Arrays.asList(track.getArtists()), ArtistSimplified::getName);
    }

    private String formatArtists(TrackEntity entity){
        return joinNames(entity.getArtists(), ArtistEntity::getName);
    }

    private String formatArtists(Track response){
        return joinNames(Arrays.asList(response.getArtists()), ArtistSimplified::getName);
    }

    private <T> String joinNames(Collection<T> items, Function<T, String> nameExtractor) {
        return items.stream()
                .map(nameExtractor)
                .collect(Collectors.joining(", "));
    }
}

