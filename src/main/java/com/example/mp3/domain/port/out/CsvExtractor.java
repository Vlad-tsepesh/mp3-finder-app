package com.example.mp3.domain.port.out;

import java.io.IOException;
import java.util.List;

public interface CsvExtractor<T> {
    List<T> extract(String csvFile) throws IOException;
}
