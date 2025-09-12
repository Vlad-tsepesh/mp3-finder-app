package com.example.mp3.application.mapper;

import com.example.mp3.domain.model.Artist;
import com.example.mp3.domain.model.Track;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface TrackMapper {

    @Mapping(target = "title",
            expression = "java(cleanTitle(trackCsvDto.title()))")
    @Mapping(target = "trackName",
            expression = "java(getTrackName(trackCsvDto))")
    @Mapping(target = "artists",
            expression = "java(artistBuilder(trackCsvDto))")

    @Mapping(target = "url", ignore = true)
    @Mapping(target = "download", constant = "false")
    Track fromTrackDto(TrackCsvDto trackCsvDto);

    List<Track> fromTrackDtos(List<TrackCsvDto> trackCsvDtos);

//    SpotifyRequest fromTrackEntity(Track track);
//    List<SpotifyRequest> fromTrackEntities(List<Track> track);
//    Track fromSpotifyResponse(SpotifyResponse response);

    default List<Artist> artistBuilder(TrackCsvDto dto) {
        return splitArtists(extractArtists(dto)).stream()
                .distinct()
                .map(name -> Artist.builder().name(name).build())
                .toList();
    }

    default String cleanTitle(String raw) {
        if (raw == null) return null;
        return Arrays.stream(raw.split("\\(feat\\.")).findFirst().orElse(raw).trim();
    }

    default String extractArtists(TrackCsvDto dto) {
        if (dto == null) {
            return "";
        }

        return Stream.of(
                        extractArtistsFromTitle(dto.title()).orElse(""),
                        dto.artist()
                )
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(","));
    }

    default List<String> splitArtists(String artist) {
        return Arrays.stream(artist.split("[,&]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    default Optional<String> extractArtistsFromTitle(String title) {
        if (title == null) {
            return Optional.empty();
        }
        Pattern pattern = Pattern.compile("\\(feat\\.\\s*((?:[^()]*|\\([^()]*\\))*)\\)");
        Matcher matcher = pattern.matcher(title);
        if (matcher.find()) {
            return Optional.of(matcher.group(1).trim());
        }
        return Optional.empty();
    }

    default String getTrackName(TrackCsvDto dto) {
        return dto.title() + " - " + dto.artist();
    }
}
