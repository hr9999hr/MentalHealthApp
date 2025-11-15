//Creator: Tan Boon Sang (101173)
//Tester: Chong Siat Yii (98499)

package modules;

import interfaces.Scorable;
import java.io.*;

// This class implements the Scorable interface to manage scoring functionality
// It includes methods to calculate the score, show feedback, and save the score to score.txt file
public class ScoreManager implements Scorable {
    private final int score;
    private final int total;
    private final int elapsedSeconds; // Add this field
    private int percentage;

    // Constructor to initialise score, total, and elapsedSeconds
    public ScoreManager(int score, int total, int elapsedSeconds) { // Update constructor
        this.score = score;
        this.total = total;
        this.elapsedSeconds = elapsedSeconds;
    }

    // Getters for score
    @Override
    public int getScore() {
        return score;
    }

    // Method to calculate the score percentage based on score and total questions
    @Override
    public void calculateScore() {
        if (total > 0) {
            percentage = (score * 100) / total;
        } else {
            percentage = 0;
        }
    }

    // Method to show feedback based on the calculated score percentage
    @Override
    public String showFeedback() {
        calculateScore();
        StringBuilder feedback = new StringBuilder();
        feedback.append("Your Score: ").append(percentage).append("%\n");
        // Provide different feedback based on performance(percentage)
            if (percentage >= 80) {
                feedback.append("Outstanding! You understand your mental health well.\n");
            } else if (percentage >= 60) {
                feedback.append("That's good!\n");
            } else if (percentage >= 40) {
                feedback.append("Good try!\n");
            } else if (percentage >= 20) {
                feedback.append("You can do better!\n");
            } else {
                feedback.append("Don’t give up! Try again.\n");
            }

        return feedback.toString();
    }

    // Method to save the score to score.txt
    @Override
    public void saveScore(String username) {
        calculateScore();
        try {
        File file = new File("data/score.txt");
        file.getParentFile().mkdirs(); // Create data folder if it doesn't exist

        String badge = "";
            if (percentage >= 90) badge = "🏆 GOLD";
            else if (percentage >= 70) badge = "🥈 SILVER";
            else if (percentage >= 50) badge = "🥉 BRONZE";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + ": " + score + "/" + total + " (" + percentage + "%) - " + elapsedSeconds + "s"+ badge + "\n");
        }
        } catch (IOException e) {
            System.err.println("Failed to save score: " + e.getMessage());// Handle exception if needed
        }
    }

}
