package com.example.mp3.application.service;

import com.example.mp3.application.mapper.TrackMapper;
import com.example.mp3.domain.model.TrackEntity;
import com.example.mp3.domain.port.out.CsvExtractor;
import com.example.mp3.domain.port.out.TrackRepository;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Mp3Service {
    private final CsvExtractor<TrackCsvDto> csvExtractor;
    private final TrackMapper mapper;
    private final TrackRepository repository;
    private final SpotifyTrackService spotifyService;

    public void downloadMp3(String filePath) throws IOException {
        importFromCsv(filePath);
//        System.out.println(repository.fetchALlTracks());
//        updateMissingTracksId();
    }

    @Transactional
    private void importFromCsv(String filePath) throws IOException {
        validatePath(filePath);
        List<TrackEntity> tracks = extractTracks(filePath);
        log.info("Tracks extracted.");
        saveNewTracks(tracks);
        log.info("Tracks saved.");
    }

    private void validatePath(String filePath) {
        if (!Files.exists(Paths.get(filePath)))
            throw new IllegalArgumentException("CSV file not found: " + filePath);
    }

    private List<TrackEntity> extractTracks(String filePath) throws IOException {
        List<TrackCsvDto> tracksDto = csvExtractor.extract(filePath);
        return mapper.fromTrackDtos(tracksDto);
    }

    private void saveNewTracks(List<TrackEntity> tracks) {
        tracks.stream()
                .filter(repository::isNewTrack)
                .forEach(repository::saveTrack);
    }

    @Transactional
    private void updateMissingTracksId() {
        List<TrackEntity> tracks = repository.fetchTracksWithMissingSpotifyId();
        tracks.forEach(this::updateTrackUrl);
        tracks.forEach(repository::saveTrack);
        log.info("UpdateMissingTrackUrls done.");
    }

    private void updateTrackUrl(TrackEntity entity) {
        String uriId = spotifyService.findTrackUriId(entity);
//        entity.setUrl(uriId);
    }
}
