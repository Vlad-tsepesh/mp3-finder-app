package com.example.mp3.application.mapper;

import com.example.mp3.domain.model.Track;
import com.example.mp3.infrastructure.client.dto.SpotifyRequest;
import com.example.mp3.infrastructure.client.dto.SpotifyResponse;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {

    @Mapping(target = "trackName", expression = "java(trackCsvDto.artist() + \" - \" + trackCsvDto.title())")
    Track fromTrackDto(TrackCsvDto trackCsvDto);

    List<Track> fromTrackDtos(List<TrackCsvDto> trackCsvDtos);

    SpotifyRequest fromTrackEntity(Track track);
    List<SpotifyRequest> fromTrackEntities(List<Track> track);

    Track fromSpotifyResponse(SpotifyResponse response);
}
