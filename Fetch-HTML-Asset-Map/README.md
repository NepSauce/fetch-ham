# Fetch Ham - HTML Asset Map

Fetch Ham is a Java desktop application for mapping and analyzing web assets from a target URL. It is designed to help you quickly inventory a site and understand what resources it serves.

## Purpose

Fetch Ham aims to make website asset discovery and crawl configuration accessible through a simple, visual desktop UI. The goal is to let you enter a URL, choose crawl depth, toggle extraction behaviors, and map a site into a structured asset view. Over time this will support richer outputs like asset summaries, crawl health signals, and rule-based filtering so you can tune what is collected and how it is displayed.

## Current experience

- Desktop app entry point at `ham.MainFrame`
- Swing-based main window with:
  - Top logo/header area
  - URL input field (`https://` default)
  - Crawl mode selector (`Seed`, `Contained`)
  - `Map URL` action button
  - Toggle controls:
    - Respect Robots.txt
    - Enable Rules
    - Extract Non-HTML
    - Extract Metadata
- Base crawler entry class (`HAMEntry`) scaffold for future implementation

## Tech stack

- Java 21 (toolchain configured in Gradle)
- Gradle (wrapper included)
- Swing (desktop UI)
- Jsoup (local JAR dependency in `app/libs`)
- Guava
- JUnit 5

## Project structure

```text
Fetch-HTML-Asset-Map/
├─ app/
│  ├─ src/main/java/ham/
│  │  ├─ MainFrame.java
│  │  ├─ hamcrawler/
│  │  └─ swing/
│  ├─ src/main/resources/media/
│  └─ libs/                     
├─ gradle/
├─ gradlew
└─ settings.gradle
```

## Roadmap ideas

- Implement crawling flow in `ham.hamcrawler.HAMEntry`
- Wire UI controls to crawler options
- Add rule-set management panel behavior
- Persist/load project settings
- Expand unit and integration test coverage
