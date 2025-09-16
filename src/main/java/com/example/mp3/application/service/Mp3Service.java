package com.example.mp3.application.service;

import com.example.mp3.application.mapper.TrackMapper;
import com.example.mp3.domain.model.ArtistEntity;
import com.example.mp3.domain.model.TrackEntity;
import com.example.mp3.domain.port.out.CsvExtractor;
import com.example.mp3.domain.port.out.SpotifyRepository;
import com.example.mp3.domain.service.TrackNameService;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Mp3Service {
    private final CsvExtractor<TrackCsvDto> csvExtractor;
    private final TrackMapper mapper;
    private final SpotifyRepository repository;
    private final SpotifyTrackService trackService;
    private final SpotifyArtistService artistService;
    private final TrackNameService trackNameService;

    public void downloadMp3(String filePath) throws IOException {
        importFromCsv(filePath);
        findAndSaveMissingArtistsSpotifyId();
////        System.out.println(repository.fetchALlTracks());
        findAndSaveMissingTracksSpotifyId();
    }

    @Transactional
    private void importFromCsv(String filePath) throws IOException {
        validatePath(filePath);
        List<TrackCsvDto> dtos = csvExtractor.extract(filePath);
        log.info("Tracks extracted.");
        mapAndSaveTracks(dtos);
        log.info("Tracks saved.");
    }

    private void validatePath(String filePath) {
        if (!Files.exists(Paths.get(filePath)))
            throw new IllegalArgumentException("CSV file not found: " + filePath);
    }

    public void mapAndSaveTracks(List<TrackCsvDto> dtos) {

        Set<String> allNames = dtos.stream()
                .flatMap(dto -> trackNameService.splitArtistNames(dto.artist()).stream())
                .collect(Collectors.toSet());

        Map<String, ArtistEntity> resolvedArtists = repository.findAllArtistsByNameIn(allNames)
                .stream()
                .collect(Collectors.toMap(ArtistEntity::getName, a -> a));

        allNames.forEach(name -> resolvedArtists.computeIfAbsent(
                name, n -> repository.save(ArtistEntity.builder().name(n).build())
        ));

        List<TrackEntity> tracks = mapper.fromTrackDtos(dtos, resolvedArtists);

        repository.saveAllTracks(tracks.stream().filter(repository::isNewTrack).toList());
    }

    private void saveNewTracks(List<TrackEntity> tracks) {
        tracks.stream()
                .filter(repository::isNewTrack)
                .forEach(repository::saveTrack);
    }

    @Transactional
    private void findAndSaveMissingTracksSpotifyId() {
        List<TrackEntity> tracks = repository.fetchTracksWithMissingSpotifyId();
        tracks.forEach(this::updateTrackUrl);
        repository.saveAllTracks(tracks);
        log.info("UpdateMissingTrackUrls done.");
    }

    private void updateTrackUrl(TrackEntity entity) {
        String spotifyId = trackService.findTrackUriId(entity);

//        if (spotifyId.isBlank()) {
//            spotifyId = trackService.findTrackUriIdByArtistId(entity);
//        }
        entity.setSpotifyId(spotifyId);
    }

    @Transactional
    private void findAndSaveMissingArtistsSpotifyId(){
        List<ArtistEntity> artistEntities = repository.fetchArtistsWithNoSpotifyId();
        artistEntities.forEach(artist ->{
                String spotifyId = artistService.findArtistSpotifyId(artist.getName());
                artist.setSpotifyId(spotifyId);
                repository.saveArtist(artist);
        });
    }
}
