package com.example.mp3.utils;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import static com.example.mp3.utils.Sanitizer.sanitize;

public class Matcher {

    private static final JaroWinklerSimilarity jw = new JaroWinklerSimilarity();



    public static double similarity(String s1, String s2) {
        return jw.apply(sanitize(s1), sanitize(s2));
    }
}
