package com.example.mp3.infrastructure.csv.dto;

import lombok.Builder;

@Builder
public record TrackCsvDto(String title, String artist) {
}
