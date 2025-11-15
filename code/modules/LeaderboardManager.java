//Creator: Ang Hui Ru (98167)
//Tester: Wong Jing Yi (101350)
// This module manages the leaderboard functionality, including reading from a file and displaying scores.
package modules;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

// This class manages the leaderboard by reading scores from a txt file,
// it also provides a method to display the leaderboard in a dialog.
// The scores are sorted by highest score first, and then by time taken in ascending order.

public class LeaderboardManager {
    private static final String FILE = "data/score.txt";

    // Helper class to store username and score
    public static class UserScore {
        public final String username;
        public final int score;
        public final String rawLine;

        // Constructor to initialise UserScore with username, score, and the raw line from the file
        public UserScore(String username, int score, String rawLine) {
            this.username = username;
            this.score = score;
            this.rawLine = rawLine;
        }

        // Method to extract time in seconds from the raw line
        public int getTimeSeconds() {
            String[] parts = rawLine.split("-");
            if (parts.length >= 2) {
                String timePart = parts[1].trim();
                // Extract only digits
                String digitsOnly = timePart.replaceAll("[^0-9]", "").trim();
                    if (!digitsOnly.isEmpty()) {
                        try {
                            return Integer.parseInt(digitsOnly);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            return Integer.MAX_VALUE; // If time not found, put at the end
        }
    }

    // Method to read scores from the file and return a sorted list of UserScore objects
    public static List<UserScore> getSortedScores() {
        Map<String, UserScore> bestScores = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Parse username and score
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String username = parts[0].trim();
                    String[] scoreParts = parts[1].trim().split("/");
                    if (scoreParts.length >= 1) {
                        try {
                            int score = Integer.parseInt(scoreParts[0].trim());
                            // Keep the highest score for each user
                            UserScore existing = bestScores.get(username);
                            if (existing == null || score > existing.score) {
                                bestScores.put(username, new UserScore(username, score, line));
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        } catch (IOException e) {
            // Ignore if file doesn't exist
        }
        // Sort the scores
        List<UserScore> scores = new ArrayList<>(bestScores.values());
        scores.sort((a, b) -> {
            int scoreCompare = Integer.compare(b.score, a.score);
            if (scoreCompare != 0) return scoreCompare;
            int aTime = a.getTimeSeconds();
            int bTime = b.getTimeSeconds();
            return Integer.compare(aTime, bTime);
        });
        return scores;
    }

    // Method to display the leaderboard in a table format in a dialog
    public static void showLeaderboard(JFrame parent) {
        List<UserScore> scores = getSortedScores();

        // Create custom dialog with phone-sized dimensions
        JDialog leaderboardDialog = new JDialog(parent, "Leaderboard", true);
        leaderboardDialog.setSize(360, 800);
        leaderboardDialog.setLocationRelativeTo(parent);

        // Table column names
        String[] columns = {"Rank", "Username", "Score", "Time (s)", "Badge"};
        Object[][] data = new Object[scores.size()][5];

        int rank = 1;
        for (int i = 0; i < scores.size(); i++) {
            UserScore us = scores.get(i);

            // Parse username, score, and time from rawLine
            String username = us.rawLine.split(":")[0].trim();
            String[] parts = us.rawLine.split(":")[1].split("-");
            String scoreStr = parts[0].trim();
            String timeStr = parts.length > 1 ? parts[1].replace("s", "").trim() : "";

            // include badge based on rank
            String badge = switch (rank) {
                case 1 -> "🥇";
                case 2 -> "🥈";
                case 3 -> "🥉";
                default -> "";
            };

            data[i][0] = rank;
            data[i][1] = username;
            data[i][2] = scoreStr;
            data[i][3] = timeStr;
            data[i][4] = badge;
            rank++;
        }

        // Create a table with the data
        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);

        leaderboardDialog.setLayout(new BorderLayout());
        leaderboardDialog.add(scrollPane, BorderLayout.CENTER);

        // Add a close button at the bottom
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> leaderboardDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        leaderboardDialog.add(buttonPanel, BorderLayout.SOUTH);

        leaderboardDialog.setVisible(true);
        
    }
}