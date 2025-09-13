package com.example.mp3.application.service;

import com.example.mp3.application.mapper.TrackMapper;
import com.example.mp3.domain.model.Track;
import com.example.mp3.domain.port.out.CsvExtractor;
import com.example.mp3.domain.port.out.SpotifyClient;
import com.example.mp3.domain.port.out.TrackRepository;
import com.example.mp3.infrastructure.client.dto.SpotifyRequest;
import com.example.mp3.infrastructure.client.dto.SpotifyResponse;
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
    private final SpotifyClient spotifyClient;

    public void downloadMp3(String filePath) throws IOException {
//        importFromCsv(filePath);
//        System.out.println(repository.fetchALlTracks());
        updateMissingTrackUrls();
    }

    @Transactional
    private void importFromCsv(String filePath) throws IOException {
        validatePath(filePath);
        List<Track> tracks = extractTracks(filePath);
        log.info("Tracks extracted.");
        saveNewTracks(tracks);
        log.info("Tracks saved.");
    }

    private void validatePath(String filePath) {
        if (!Files.exists(Paths.get(filePath)))
            throw new IllegalArgumentException("CSV file not found: " + filePath);
    }

    private List<Track> extractTracks(String filePath) throws IOException {
        List<TrackCsvDto> tracksDto = csvExtractor.extract(filePath);
        return mapper.fromTrackDtos(tracksDto);
    }

    private void saveNewTracks(List<Track> tracks) {
        tracks.stream()
                .filter(repository::isNewTrack)
                .forEach(repository::saveTrack);
    }

    @Transactional
    private void updateMissingTrackUrls() {
        List<Track> tracks = repository.fetchTracksWithEmptyUrl();
        tracks.forEach(this::updateTrackUrl);
        tracks.forEach(repository::saveTrack);
    }

    private void updateTrackUrl(Track track) {
        SpotifyRequest request = mapper.toSpotifyRequest(track);
        SpotifyResponse response = spotifyClient.searchTracksUrl(request);
        track.setUrl(response.uri());
    }
}
