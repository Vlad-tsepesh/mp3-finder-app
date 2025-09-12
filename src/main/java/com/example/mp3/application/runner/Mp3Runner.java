package com.example.mp3.application.runner;

import com.example.mp3.application.service.Mp3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class Mp3Runner implements CommandLineRunner {

    private final Mp3Service service;

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -jar app.jar <path-to-csv>");
            return;
        }
        String csvPath = args[0];
        service.downloadMp3(csvPath);
//        service.printAllTracks();
    }
}
