package com.example.mp3.utils;

public class Sanitizer {

    public static String sanitize(String s) {
        if (s == null) return "";

        String sanitized =
                s.toLowerCase()
                        // remove anything inside (), [], {}
                        .replaceAll("[({\\[].*?[)}\\]]", "")
                        // remove punctuation except letters/numbers/spaces
                        .replaceAll("[^\\p{L}\\p{N}\\s]", "")
                        // collapse multiple spaces
                        .replaceAll("\\s+", " ")
                        .trim();

        return sanitized;
    }
}
