import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import com.fasterxml.jackson.databind.*;

public class QuizLeaderboard {

    private static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    private static final String REG_NO = "RA2311026010186"; 

    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // To store unique entries
        Set<String> uniqueEntries = new HashSet<>();

        // To store total scores
        Map<String, Integer> scores = new HashMap<>();

        for (int i = 0; i < 10; i++) {

            String url = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + i;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
System.out.println("API Response: " + response.body());

if (!response.body().trim().startsWith("{")) {
    System.out.println("Server did not return JSON. Skipping poll " + i);
    continue;
}
            JsonNode root = mapper.readTree(response.body());
            JsonNode events = root.get("events");

            if (events == null || !events.isArray()) {
                System.out.println("No events found for poll " + i);
                continue;
            }

            for (JsonNode event : events) {
                String roundId = event.get("roundId").asText();
                String participant = event.get("participant").asText();
                int score = event.get("score").asInt();

                String key = roundId + "_" + participant;

                // Deduplication
                if (!uniqueEntries.contains(key)) {
                    uniqueEntries.add(key);
                    scores.put(participant, scores.getOrDefault(participant, 0) + score);
                }
            }

            // Mandatory delay of 5 seconds
            Thread.sleep(5000);
        }

        // Create leaderboard
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>(scores.entrySet());

        leaderboard.sort((a, b) -> b.getValue() - a.getValue());

        // Calculate total score
        int totalScore = scores.values().stream().mapToInt(Integer::intValue).sum();

        // Prepare JSON submission
        List<Map<String, Object>> leaderboardJson = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : leaderboard) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("participant", entry.getKey());
            obj.put("totalScore", entry.getValue());
            leaderboardJson.add(obj);
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("regNo", REG_NO);
        requestBody.put("leaderboard", leaderboardJson);

        String jsonBody = mapper.writeValueAsString(requestBody);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/quiz/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Leaderboard: " + leaderboardJson);
        System.out.println("Total Score: " + totalScore);
        System.out.println("Submission Response: " + postResponse.body());
    }
}