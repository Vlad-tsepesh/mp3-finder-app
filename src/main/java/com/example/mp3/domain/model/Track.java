package com.example.mp3.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Track {
    @Id
    String trackName;
    String title;
    String artist;
    String url;
    boolean download;
}
