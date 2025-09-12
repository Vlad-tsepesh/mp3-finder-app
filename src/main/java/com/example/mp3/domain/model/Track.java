package com.example.mp3.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "track")
public class Track {

    @Id
    private String trackName; // primary key

    private String title;
    private String url;
    private boolean download;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "track_artist", // join table
        joinColumns = @JoinColumn(name = "track_name"), // points to Track
        inverseJoinColumns = @JoinColumn(name = "artist_id") // points to Artist
    )
    private List<Artist> artists = new ArrayList<>();
}
