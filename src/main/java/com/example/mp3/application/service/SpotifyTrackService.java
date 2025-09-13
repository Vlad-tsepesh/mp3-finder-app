package com.example.mp3.application.service;

import com.example.mp3.domain.model.TrackEntity;
import com.example.mp3.infrastructure.client.SpotifyAdapter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
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

@Service
@RequiredArgsConstructor
public class SpotifyTrackService {

    private static final AtomicInteger passedTracks = new AtomicInteger(0);
    private final SpotifyAdapter spotifyAdapter;
    private final JaroWinklerSimilarity jw = new JaroWinklerSimilarity();

    public String findTrackUriId(TrackEntity entity) {
        List<Track> spotifyTracks = spotifyAdapter.searchForTracks(entity.getTrackName());
        Optional<Track> bestMatch = findBestMatch(entity, spotifyTracks);
        bestMatch.ifPresent(track -> printBestMatches(track, entity));
        return bestMatch.stream().map(Track::getId).collect(Collectors.joining());
    }

    private Optional<Track> findBestMatch(TrackEntity entity, List<Track> tracks) {

        return tracks.stream()

                // 1. filter by track similarity threshold
//                .filter(track -> similarity(sanitize(entity.getTitle()), sanitize(track.getName())) > 0.55)
//                .filter(track -> similarity(
//                        sanitize(joinNames(entity.getArtists(), ArtistEntity::getName)),
//                        sanitize(joinNames(Arrays.asList(track.getArtists()), ArtistSimplified::getName))) > 0.55)
                // 2. calculate full score for remaining tracks
                .map(track -> {
                    double fullScore = similarity(entity.getTrackName(), getFullTrackName(track));
                    return new AbstractMap.SimpleEntry<>(track, fullScore);
                })
                // 3. find track with highest combined score
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }


    private void printBestMatches(Track track, TrackEntity entity) {
        int currentCount = passedTracks.incrementAndGet();
        System.out.println(currentCount);
        System.out.println(entity.getTrackName());
        System.out.println(getFullTrackName(track));
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    private double similarity(String s1, String s2) {
        return jw.apply(s1.toLowerCase(), s2.toLowerCase());
    }

    private String getFullTrackName(Track track) {
        return track.getName() + " - " + joinNames(Arrays.asList(track.getArtists()), ArtistSimplified::getName);
    }

    private <T> String joinNames(Collection<T> items, Function<T, String> nameExtractor) {
        return items.stream()
                .map(nameExtractor)
                .collect(Collectors.joining(", "));
    }

    private String sanitize(String s) {
        if (s == null) return "";

        String sanitized =
                s.toLowerCase()
                        // remove anything inside (), [], {}
                        .replaceAll("[({\\[].*?[)}\\]]", "")
                        // remove punctuation except letters/numbers/spaces
                        .replaceAll("[^\\p{L}\\p{N}\\s]", "")
                        // collapse multiple spaces
                        .replaceAll("\\s+", " ")
                        .trim();

        return sanitized;
    }
}

