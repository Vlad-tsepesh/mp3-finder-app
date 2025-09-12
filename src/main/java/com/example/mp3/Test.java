package com.example.mp3;

import com.example.mp3.domain.model.Artist;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {

//        System.out.println(cleanTitle("Yamore (feat. Cesária Evora, Benja (NL) & Franc Fala)"));
//        System.out.println(extractArtistsFromTitle("Yamore"));
//        System.out.println((long) splitArtists("MoBlack & Salif Keïta").size());


        TrackCsvDto dto = TrackCsvDto.builder()
                .artist(null)
                .title("Yamore (feat. Cesária Evora, Benja (NL) & Franc Fala)")
                .build();

        extractArtists(dto).stream().map(Artist::getName).forEach(System.out::println);
    }

    static String cleanTitle(String raw) {
        if (raw == null) return null;
        return Arrays.stream(raw.split("\\(feat\\.")).findFirst().orElse(raw).trim();
    }


    static List<Artist> extractArtists(TrackCsvDto dto) {
        if (dto == null) {
            return List.of();
        }

        String combined = Stream.of(
                        extractArtistsFromTitle(dto.title()).orElse(""),
                        dto.artist()
                )
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(","));

        return splitArtists(combined).stream()
                .distinct()
                .map(name -> Artist.builder().name(name).build())
                .toList();
    }



    static List<String> splitArtists(String artist) {
        return Arrays.stream(artist.split("[,&]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    static Optional<String> extractArtistsFromTitle(String title) {
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

}


