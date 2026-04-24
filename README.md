# Quiz Leaderboard System

This Java application fetches quiz messages from the API, removes duplicate records using roundId + participant, calculates total score for each participant, sorts the leaderboard, and submits the final result.

## Features
- Polls API 10 times
- Maintains 5-second delay between polls
- Removes duplicate entries
- Calculates total score
- Sorts leaderboard in descending order
- Handles server errors safely

## Error Handling
During testing, the API returned HTTP 503 with response "no available server", which indicates server-side unavailability. The code handles this safely without crashing.

## Tech Stack
- Java
- HttpClient
- Jackson JSON Library

## How to Run
1. Open project in VS Code
2. Ensure all Jackson JAR files are inside lib folder
3. Run QuizLeaderboard.java