package com.example.mp3.application.mapper;

import com.example.mp3.domain.model.TrackEntity;
import com.example.mp3.domain.service.TrackNameService;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TrackNameService.class)
public interface TrackMapper {

    @Mapping(target = "title", source = "title", qualifiedByName = "cleanTitle")
    @Mapping(target = "trackName", source = ".", qualifiedByName = "formatTrackName")
    @Mapping(target = "artists", source = ".", qualifiedByName = "buildArtists")
    @Mapping(target = "spotifyId", ignore = true)
    @Mapping(target = "download", constant = "false")
    TrackEntity fromDto(TrackCsvDto dto);

    List<TrackEntity> fromTrackDtos(List<TrackCsvDto> dto);
}
