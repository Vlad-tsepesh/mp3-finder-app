# 🎵 Music Downloader

A **Spring Boot** application that automates the process of:

- Importing music tracks from a **CSV file**
- Parsing and cleaning song/artist names
- Storing them in an **H2 database**
- Looking up **Spotify IDs** for tracks and artists
- Preparing them for downloading

---

## 🚀 Features

- CSV import using [Univocity Parsers](https://github.com/uniVocity/univocity-parsers)
- Database persistence with **Spring Data JPA + H2**
- Spotify API integration with [spotify-web-api-java](https://github.com/spotify-web-api-java/spotify-web-api-java)
- Track & Artist deduplication and matching (using **Jaro-Winkler similarity**)
- Automatic resolution of missing Spotify IDs
- Simple command-line runner for automation

---

## 📦 Tech Stack

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA / Hibernate**
- **H2 Database**
- **Spring Security (H2 Console enabled)**
- **MapStruct** (DTO → Entity mapping)
- **Lombok** (boilerplate reduction)
- **Spotify Web API Java**
- **Univocity CSV Parser**

---

## 🗂️ Project Structure

```
com.example.mp3
 ├── application
 │   ├── config        # Security + CSV configs
 │   ├── mapper        # MapStruct mappers
 │   ├── runner        # CommandLineRunner entrypoint
 │   └── service       # Business services (CSV import, Spotify lookup, etc.)
 │
 ├── domain
 │   ├── model         # Entities: TrackEntity, ArtistEntity
 │   ├── port.out      # Interfaces (CSV, Spotify, Repositories)
 │   ├── repository    # Spring Data JPA repositories
 │   └── service       # Helper services (TrackNameService)
 │
 ├── infrastructure
 │   ├── client        # SpotifyAdapter (Spotify API implementation)
 │   ├── csv           # CsvExtractor & DTOs
 │   └── persistance   # RepositoryAdapter (implements SpotifyRepository)
 │
 ├── utils             # Helpers (Matcher, Sanitizer)
 └── Mp3Application    # Spring Boot entrypoint
```

---

## 📂 CSV Format

The input CSV file should have headers:

```csv
Title,Artist
Song 1,Artist A
Song 2 (feat. Artist B),Artist C
```

---

## ⚙️ Setup & Run

### 1. Clone repository

```bash
git clone https://github.com/your-username/music-downloader.git
cd music-downloader
```

### 2. Build with Maven

```bash
mvn clean install
```

### 3. Run the app

```bash
java -jar target/Music-Downloader-0.0.1-SNAPSHOT.jar path/to/your/tracks.csv
```

If no CSV is provided, usage instructions will be displayed.

---

## 💾 Database

- Uses **H2** (file-based) → `./data/demo-db`
- Console available at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:file:./data/demo-db`
- User: `sa` / Password: *(empty)*

---

## 🔑 Spotify API Setup

To use Spotify API features, you’ll need to:

1. Register an app at [Spotify Developer Dashboard](https://developer.spotify.com/dashboard/)
2. Configure **client ID & secret** in your `application.yml` or environment variables
3. The app uses `spotify-web-api-java` to handle authentication and requests

---

## 🛠️ Future Improvements

- Implement actual **MP3 downloading** once Spotify IDs are resolved
- Add **REST endpoints** for importing tracks from a web API instead of CSV
- Support other music providers (YouTube, SoundCloud, etc.)
- Export enriched track data back to CSV/JSON
- Switch from **CommandLineRunner** to **Spring Boot MVC** for web-based imports

---

## 🎯 Current Status

- CSV import & parsing works
- Spotify ID lookup precision: **(~86%)**
- MP3 downloading service is planned