package com.example.mp3.domain.service;

import com.example.mp3.domain.model.ArtistEntity;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TrackNameService {
    private static final Pattern FEAT_PATTERN = Pattern.compile("\\(feat\\.\\s*((?:[^()]*|\\([^()]*\\))*)\\)");

    @Named("formatTrackName")
    public String formatTrackName(TrackCsvDto dto) {
        return dto.title() + " - " + dto.artist();
    }

    @Named("cleanTitle")
    public String cleanTitle(String raw) {
        if (raw == null) return null;
        return Arrays.stream(raw.split("\\(feat\\.")).findFirst().orElse(raw).trim();
    }

    private Optional<String> extractFeaturedArtists(String title) {
        if (title == null) return Optional.empty();
        Matcher matcher = FEAT_PATTERN.matcher(title);
        if (matcher.find()) return Optional.of(matcher.group(1).trim());
        return Optional.empty();
    }

    private String extractArtistNames(TrackCsvDto dto) {
        if (dto == null) return "";

        return Stream.of(extractFeaturedArtists(dto.title()).orElse(""), dto.artist())
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(","));
    }

    private List<String> splitArtistNames(String artist) {
        return Arrays.stream(artist.split("[,&]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    @Named("buildArtists")
    public List<ArtistEntity> buildArtists(TrackCsvDto dto) {
        return splitArtistNames(extractArtistNames(dto)).stream()
                .distinct()
                .map(name -> ArtistEntity.builder().name(name).build())
                .toList();
    }
}
