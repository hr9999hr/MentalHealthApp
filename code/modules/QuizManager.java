//Creator: Chong Siat Yii (98499)
//Tester: Ang Hui Ru (98167)

package modules;

import interfaces.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

// This class implements the QuizManagerInterface to manage the quiz functionality.
// It handles loading questions from a file, displaying them in a GUI, tracking the score,
// and managing the quiz timer. It provides methods to load questions, show the current question.

public final class QuizManager implements QuizManagerInterface {    
    private final JFrame frame;
    private final List<GUIQuestion> questions = new ArrayList<>();
    private final JPanel mainPanel;
    private int current = 0;
    private int score = 0;

    // Timer fields
    private final Timer quizTimer;
    private int elapsedSeconds = 0;
    private JLabel timerLabel;

    // Constructor to initialise the quiz manager and set up the GUI.
    public QuizManager() {
        frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(360, 800);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        frame.add(mainPanel);

        // Timer label in a box at the top right
        timerLabel = new JLabel("Timer: 0s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setForeground(new Color(91, 155, 213)); // Primary blue
        timerLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(91, 155, 213), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.WHITE);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        timerPanel.setOpaque(false);
        timerPanel.add(timerLabel);
        mainPanel.add(timerPanel, BorderLayout.NORTH);

        // Start timer
        quizTimer = new Timer(1000, e -> {
            elapsedSeconds++;
            timerLabel.setText("Timer: " + elapsedSeconds + "s");
        });
        quizTimer.start();

        loadQuestions();
        showQuestion();

        frame.setVisible(true);
    }

    // This method loads questions from a text file and populates the questions list.
    // It reads the file line by line, identifies the question type, and creates the appropriate
    // GUIQuestion object (MC, TF, or FB) based on the content.
    @Override
    public void loadQuestions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Assets//Questions.txt"))) {
        String line;
        String type = null, question = null, answer = null ;
        List<String> options = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("TYPE:")) {
                type = line.substring(5).trim();
                options.clear();
            } else if (line.startsWith("QUESTION:")) {
                question = line.substring(9).trim();
            } else if (line.startsWith("OPTIONS:")) {
                options = Arrays.asList(line.substring(8).trim().split(";"));
            } else if (line.startsWith("ANSWER:")) {
                answer = line.substring(7).trim();

                // Construct the appropriate question type
                switch (type) {
                    case "MC" -> questions.add(new GUIMCQuestion(question, options, answer));
                    case "TF" -> questions.add(new GUITFQuestion(question, Boolean.parseBoolean(answer.toLowerCase())));
                    case "FB" -> questions.add(new GUIFillBlankQuestion(question, answer));
                    default -> System.err.println("Unknown question type: " + type);
                }
                // Reset for next question block
                type = null;
                question = null;
                answer = null;
                options = new ArrayList<>();
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(frame, "Failed to load questions: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // This method displays the current question in the GUI.
    @Override
    public void showQuestion() {
        mainPanel.removeAll();
        mainPanel.setBackground(new Color(240, 248, 255));

        // Top panel with BorderLayout: left = question number, right = timer
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Question number at top left
        int maxQuestions = 25;
        JLabel questionNumberLabel = new JLabel("Question " + (current + 1) + " of " + maxQuestions);
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionNumberLabel.setForeground(new Color(60, 60, 80)); 
        topPanel.add(questionNumberLabel, BorderLayout.WEST);

        // Timer at top right in a box
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        timerPanel.setOpaque(false);
        timerPanel.add(timerLabel);
        topPanel.add(timerPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        if (current < questions.size() && current < maxQuestions) {
            try{
            GUIQuestion q = questions.get(current);
            JPanel questionPanel = q.getPanel();
            questionPanel.setBackground(new Color(240, 248, 255));
            
            mainPanel.add(q.getPanel(), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setOpaque(false);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            
            if (current > 0) {
                JButton prevBtn = new JButton("Previous");
                prevBtn.setFont(new Font("Arial", Font.BOLD, 14));
                prevBtn.setBackground(new Color(91, 155, 213)); // Primary blue
                prevBtn.setForeground(Color.WHITE);
                prevBtn.setFocusPainted(false);
                prevBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                prevBtn.addActionListener(e -> {
                    current--;
                    showQuestion();
                });
                buttonPanel.add(prevBtn);
            }

            JButton nextBtn = new JButton(current == maxQuestions - 1 ? "Finish" : "Next");
            nextBtn.setFont(new Font("Arial", Font.BOLD, 16));
            nextBtn.setBackground(new Color(92, 184, 92)); // Green
            nextBtn.setForeground(Color.WHITE);
            nextBtn.setFocusPainted(false);
            nextBtn.setPreferredSize(new Dimension(120, 50));
            nextBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            nextBtn.addActionListener(e -> {
                boolean answered = false;
                boolean correct = false;

                if (q instanceof GUIMCQuestion mcq) {
                    for (JRadioButton rb : mcq.getOptions()) {
                        if (rb.isSelected()) {
                            answered = true;
                            correct = mcq.checkAnswer();
                            break;
                        }
                    }
                } else if (q instanceof GUITFQuestion tfq) {
                    if (tfq.getTrueBtn().isSelected() || tfq.getFalseBtn().isSelected()) {
                        answered = true;
                        correct = tfq.checkAnswer();
                    }
                } else if (q instanceof GUIFillBlankQuestion fbq) {
                    String userInput = fbq.getField().getText().trim();
                    if (!userInput.isEmpty()) {
                        answered = true;
                        correct = fbq.checkAnswer();
                    }
                }

                if (!answered) {
                    JOptionPane.showMessageDialog(frame,
                        "Please select or enter an answer before proceeding.",
                        "No Answer",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (correct) {
                    score++;
                }

                current++;
                showQuestion();
            });
   
            buttonPanel.add(nextBtn);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        }catch (IndexOutOfBoundsException ex) {
            // Handle error if question index is invalid
            String errorMsg = "Error loading question #" + (current + 1) + "\nReason: " + ex.getMessage() + "\n\nSkipping to next question.";
                
            JOptionPane.showMessageDialog(frame, errorMsg,
                "Question Loading Error", 
                JOptionPane.ERROR_MESSAGE);
                    
                // Skip to next question
                current++;
                showQuestion(); // Recursively show next question
            }
        } else {
            showResult();
        }

        frame.revalidate();
        frame.repaint();
    }

    // This method displays the final result after the quiz is completed.
    // It shows the score, elapsed time, and a thank you message.
    @Override
    public void showResult() {
        if (quizTimer != null && quizTimer.isRunning()) {
            quizTimer.stop();
        }
        mainPanel.removeAll();
        mainPanel.setLayout(new GridLayout(3, 1, 0, 10)); // Add vertical spacing
        mainPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel message = new JLabel("Thanks for completing the Quiz!", JLabel.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 16));
        message.setForeground(new Color(70, 130, 180)); // Dark blue
        
        JLabel scoreLabel = new JLabel("Your score: " + score + " / " + questions.size(), JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(60, 60, 80)); // Dark text
        
        JLabel timeLabel = new JLabel("Time taken: " + elapsedSeconds + " seconds", JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(new Color(60, 60, 80)); // Dark text
        
        mainPanel.add(message);
        mainPanel.add(scoreLabel);
        mainPanel.add(timeLabel);
    }

    // Below are the methods from the QuizManagerInterface that need to be implemented.
    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getTotalQuestions() {
        return questions.size();
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }

    @Override
    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizManager::new);
    }
}

