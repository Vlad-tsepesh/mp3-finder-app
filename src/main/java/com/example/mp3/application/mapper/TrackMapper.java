package com.example.mp3.application.mapper;

import com.example.mp3.domain.model.Artist;
import com.example.mp3.domain.model.Track;
import com.example.mp3.domain.service.TrackNameService;
import com.example.mp3.infrastructure.client.dto.ArtistDto;
import com.example.mp3.infrastructure.client.dto.SpotifyRequest;
import com.example.mp3.infrastructure.client.dto.SpotifyResponse;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.*;

@Mapper(componentModel = "spring", uses = TrackNameService.class)
public interface TrackMapper {

    @Mapping(target = "title", source = "title", qualifiedByName = "cleanTitle")
    @Mapping(target = "trackName", source = ".", qualifiedByName = "formatTrackName")
    @Mapping(target = "artists", source = ".", qualifiedByName = "buildArtists")
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "download", constant = "false")
    Track fromDto(TrackCsvDto dto);

    List<Track> fromTrackDtos(List<TrackCsvDto> dto);

    @Mapping(target = "artists", source = "artists")
    SpotifyRequest toSpotifyRequest(Track track);

    default ArtistDto map(Artist entity) {
        return new ArtistDto(entity.getName());
    }

    List<SpotifyRequest> fromTrackEntities(List<Track> track);
    Track fromSpotifyResponse(SpotifyResponse response);
}
