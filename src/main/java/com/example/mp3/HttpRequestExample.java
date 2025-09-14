package com.example.mp3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequestExample {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://gateway.apyflux.com/downloadSong?songId=26kcJRHsCABz6lHttO4eqK");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        Map<String, String> headers = Map.of(
                "x-app-id", "ced094d9-8b9a-4cda-b513-d0e806230dcd",
                "x-client-id", "ou2k8EblXAOwoAVF9SmsdnVShIA2",
                "x-api-key", "ZdqNg0RWSL19ltJ1N8rSHtqXd3JhiYQXKRCj6/X8l6I="
        );

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }


        int responseCode = conn.getResponseCode();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + response.toString());
        }
    }
}
