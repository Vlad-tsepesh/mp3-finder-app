package com.example.mp3.application.service;

import com.example.mp3.application.mapper.TrackMapper;
import com.example.mp3.domain.model.Track;
import com.example.mp3.infrastructure.csv.CsvExtractor;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportTracksService {
    private final CsvExtractor<TrackCsvDto> csvExtractor;
    private final TrackMapper mapper;

    public void importFromCsv(String filePath) throws IOException {
        validatePath(filePath);
        List<TrackCsvDto> tracksDto = csvExtractor.extract(filePath);
        List<Track> tracks = mapper.fromTrackDtos(tracksDto);
        tracks.forEach(System.out::println);
    }

    private void validatePath(String filePath) {
        if (!Files.exists(Paths.get(filePath)))
            throw new IllegalArgumentException("CSV file not found: " + filePath);
    };

//    public List<Track> getAllTracks() {
//    }
}
