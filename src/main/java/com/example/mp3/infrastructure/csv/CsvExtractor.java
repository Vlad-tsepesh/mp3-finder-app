package com.example.mp3.infrastructure.csv;

import com.example.mp3.domain.model.Track;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;

import java.io.IOException;
import java.util.List;

public interface CsvExtractor<T> {
    List<T> extract(String csvFile) throws IOException;
}
