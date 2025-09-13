package com.example.mp3.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tracks")
@Entity
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String spotifyId;

    @ManyToMany(mappedBy = "artists", fetch = FetchType.EAGER)
    private List<TrackEntity> tracks = new ArrayList<>();
}
