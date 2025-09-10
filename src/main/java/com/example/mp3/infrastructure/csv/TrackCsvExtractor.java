package com.example.mp3.infrastructure.csv;

import com.example.mp3.domain.port.out.CsvExtractor;
import com.example.mp3.infrastructure.csv.dto.TrackCsvDto;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrackCsvExtractor implements CsvExtractor<TrackCsvDto> {
    private final CsvParser csvParser;

    @Override
    public List<TrackCsvDto> extract(String csvPath) throws IOException {
        try (Reader reader = openCsvFile(csvPath)) {
            List<Record> records = csvParser.parseAllRecords(reader);
            validateRecords(records, csvPath);
            return parseData(records);
        }
    }

    private Reader openCsvFile(String path) throws IOException {
        return new FileReader(path);
    }

    private void validateRecords(List<Record> records, String csvPath) throws IOException {
        if(records.isEmpty()) throw new IOException("CSV file contains no valid data: " + csvPath);
    }

    private List<TrackCsvDto> parseData(List<Record> records) {
        return records.stream()
                .map(e -> TrackCsvDto.builder()
                        .title(e.getString("Title"))
                        .artist(e.getString("Artist"))
                        .build())
                .toList();
    }
}
