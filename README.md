# 🎵 Music Downloader (Spring Boot)

A Spring Boot application that automates the process of importing songs
from a CSV file, storing them in a database, looking up download URLs,
and downloading them.

## 📌 Features

-   Import songs from a CSV file (title, artist)
-   Prevent duplicates in the database
-   Search for downloadable URLs for each song (title + artist)
-   Download songs one by one and save them as `<artist> - <title>.mp3`
-   Update database with download status

## 🏗 Architecture

    src/main/java/com/example/musicdownloader   # Root package for your app
    │
    ├── application                             # Application layer: orchestration, use cases, workflows
    │   ├── service                             # Contains services that implement business logic (CSV import, lookup, download)
    │   ├── runner                              # Startup runners (CommandLineRunner / ApplicationRunner) to trigger orchestrators
    │
    ├── domain                                  # Domain layer: core business models and repository interfaces
    │   ├── model                               # Entities (e.g., Song.java) — pure business objects with minimal dependencies
    │   └── repository                          # Repository interfaces (Spring Data JPA, or abstract ports if hexagonal)
    │
    ├── infrastructure                          # Infrastructure layer: concrete implementations for I/O
    │   ├── csv                                 # CSV reader/parsers (e.g., Apache Commons CSV)
    │   ├── external                            # External API clients (e.g., YouTube/Spotify search for URLs)
    │   └── download                            # File downloaders (e.g., HTTP client to fetch songs)
    │
    └── MusicDownloaderApplication.java         # Main Spring Boot entry point (@SpringBootApplication)


### Layers

-   **application** → Services, Orchestrator, Runner
-   **domain** → Entities, Repositories
-   **infrastructure** → CSV reader, external API client, file
    downloader

## 🚀 Getting Started

### 1. Clone repository

``` bash
git clone https://github.com/your-username/music-downloader.git
cd music-downloader
```

### 2. Build & Run

``` bash
./mvnw spring-boot:run
```

### 3. Import CSV

Place your `songs.csv` file in `src/main/resources/`.\
Format:

    title,artist
    Song 1,Artist 1
    Song 2,Artist 2

### 4. Workflow

1.  Read CSV → Insert into DB (no duplicates)
2.  Lookup URLs → Update DB
3.  Download songs → Mark as downloaded

## ⚙️ Tech Stack

-   Java 17+
-   Spring Boot 3+
-   Spring Data JPA (H2/Postgres/MySQL)
-   Apache Commons CSV (CSV parsing)
-   OkHttp/RestTemplate/WebClient (external lookup + downloads)

## 📌 Next Steps

-   Implement real `SongUrlFetcher` (YouTube, Spotify, etc.)
-   Add unit/integration tests
-   Containerize with Docker
-   CI/CD pipeline

------------------------------------------------------------------------

Made with ❤️ using Spring Boot.
