package com.example.mp3.application.runner;

import com.example.mp3.application.service.ImportTracksService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TrackImporterRunner implements CommandLineRunner {

    private final ImportTracksService service;

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -jar app.jar <path-to-csv>");
            return;
        }
        String csvPath = args[0];
        service.importFromCsv(csvPath);

//
//        List<Track> tracks = service.getAllTracks();
//        System.out.println("Imported tracks:");
//        tracks.forEach(t -> System.out.printf("• %s - %s (downloaded=%s)%n",
//                t.getTitle(), t.getArtist()));
    }
}
