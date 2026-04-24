# Quiz Leaderboard System

## Project Overview
This project is a Java-based solution for the Quiz Leaderboard Challenge. It fetches quiz event data from a REST API, processes multiple polling responses, removes duplicate entries, calculates participant scores, generates a sorted leaderboard, and submits the result back to the server.

---

## Objective
- Fetch quiz data from API (multiple polls)
- Remove duplicate entries
- Calculate total score per participant
- Sort leaderboard in descending order
- Submit final result via POST API

---

## Features
- Polls API 10 times (poll = 0 to 9)
- Maintains delay between API calls
- Deduplicates entries using roundId + participant
- Uses HashMap for score calculation
- Sorts leaderboard in descending order
- Handles API/server errors safely

---

## Deduplication Logic
Each entry is uniquely identified using:

roundId + participant

Duplicate entries are ignored using a HashSet.

---

## Approach

1. Call API 10 times:
   /quiz/messages?regNo=RA2311026010186&poll=i

2. Maintain delay between calls

3. Store unique entries using:
   HashSet<String>

4. Store scores using:
   HashMap<String, Integer>

5. Convert map to list and sort descending

6. Calculate total score

7. Submit result using POST:
   /quiz/submit

---

## Final Output

Leaderboard:

1. Bob - 295  
2. Alice - 280  
3. Charlie - 260  

Total Score:
835

---

## API Response

{
  "regNo": "RA2311026010186",
  "totalPollsMade": 20,
  "submittedTotal": 835,
  "attemptCount": 2
}

---

## Tech Stack
- Java
- HttpClient
- Jackson JSON Library
- VS Code

---

## Project Structure

QuizLeaderboard/
│
├── QuizLeaderboard.java
├── README.md
└── lib/
    ├── jackson-databind.jar
    ├── jackson-core.jar
    └── jackson-annotations.jar

---

## How to Run

Compile:
javac -cp "lib/*" QuizLeaderboard.java

Run:
java -cp ".;lib/*" QuizLeaderboard

---

## Error Handling
- Handles HTTP errors (like 503)
- Prevents program crash
- Continues execution safely

---

## Learnings
- API handling in Java
- Deduplication logic using HashSet
- Data aggregation using HashMap
- JSON parsing with Jackson

---

## Future Improvements
- Retry mechanism for API failures
- Logging system
- Spring Boot version
- Docker support

---

## Conclusion
The solution successfully:
- Fetches data correctly
- Removes duplicates
- Calculates accurate scores
- Generates sorted leaderboard
- Submits correct result

The API accepted the submission, confirming correctness.
