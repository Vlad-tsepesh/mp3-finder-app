package com.example.mp3.application.service;

import com.example.mp3.domain.model.ArtistEntity;
import com.example.mp3.domain.model.TrackEntity;
import com.example.mp3.domain.port.out.SpotifyClient;
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
    private static final double MATCH_THRESHOLD = 0.7;
    private final SpotifyClient spotifyClient;

    public String findTrackUriId(TrackEntity entity) {
        List<Track> spotifyTracks = spotifyClient.searchForTracks(entity.getTrackName());
        Optional<Track> bestMatch = findBestMatch(entity, spotifyTracks);
        bestMatch.ifPresent(track -> printBestMatches(track, entity));
        return bestMatch.stream().map(Track::getId).collect(Collectors.joining());
    }

    public String findTrackUriIdByArtistId(TrackEntity entity) {
        return entity.getArtists().stream()
                .map(ArtistEntity::getSpotifyId)
                .filter(s -> !s.isEmpty() )
                .map(spotifyClient::fetchSpotifyTrackById)
                .map(response -> findBestMatch(entity, response))
                .flatMap(Optional::stream)
                .peek(track -> printBestMatches(track, entity))
                .map(Track::getId)
                .findFirst().orElse("");
    }

    private Optional<Track> findBestMatch(TrackEntity entity, List<Track> tracks) {

        return tracks.stream()
                .filter(track -> similarity(entity.getTitle(), track.getName()) > 0.70)
                .filter(track -> similarity(formatArtists(entity), formatArtists(track)) > 0.65)
//                .filter(track -> similarity(entity.getTrackName(), formatTrackName(track)) > 0.85)
                .map(track -> {
                    double fullScore = similarity(entity.getTrackName(), formatTrackName(track));
                    return new AbstractMap.SimpleEntry<>(track, fullScore);
                })

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

    private String formatArtists(TrackEntity entity) {
        return joinNames(entity.getArtists(), ArtistEntity::getName);
    }

    private String formatArtists(Track response) {
        return joinNames(Arrays.asList(response.getArtists()), ArtistSimplified::getName);
    }

    private <T> String joinNames(Collection<T> items, Function<T, String> nameExtractor) {
        return items.stream()
                .map(nameExtractor)
                .collect(Collectors.joining(", "));
    }

    //    public Optional<Track> findBestMatch(TrackEntity entity, List<Track> candidates) {
//        String title = entity.getTitle();
//        String artist = entity.getArtists().stream()
//                .map(ArtistEntity::getName)
//                .collect(Collectors.joining(" "));
//
//        return candidates.stream()
//                .map(track -> {
//                    double titleScore = wordMatchScore(track.getName(), title);
//                    double artistScore = wordMatchScore(
//                            Arrays.stream(track.getArtists())
//                                    .map(ArtistSimplified::getName)
//                                    .collect(Collectors.joining(" ")),
//                            artist
//                    );
//
//                    double totalScore = (titleScore + artistScore) / 2.0; // average
//                    return Map.entry(track, totalScore);
//                })
//                .filter(entry -> entry.getValue() >= MATCH_THRESHOLD)
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey);
//    }
//
//    private static double wordMatchScore(String a, String b) {
//        Set<String> w1 = normalizeWords(sanitize(a));
//        Set<String> w2 = normalizeWords(sanitize(b));
//
//        if (w1.isEmpty() || w2.isEmpty()) return 0.0;
//
//        long common = w1.stream().filter(w2::contains).count();
//        return (double) common / Math.max(w1.size(), w2.size());
//    }
//
//    private static Set<String> normalizeWords(String input) {
//        return Arrays.stream(input.toLowerCase().split("\\s+"))   // split on spaces
//                .map(word -> word.replaceAll("[^a-z0-9]", ""))    // strip symbols
//                .filter(w -> !w.isBlank())                        // ignore empties
//                .collect(Collectors.toSet());
//    }

}

