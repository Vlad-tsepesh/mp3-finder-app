package com.example.mp3.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="track")
public class Track {
    @Id
    String trackName;
    String title;
    String artist;
    String url;
    boolean download;
}
