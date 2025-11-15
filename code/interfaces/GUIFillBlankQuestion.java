//Creator: Chong Siat Yii (98499)
//Tester: Ang Hui Ru (98167)

package interfaces;

import java.awt.*;
import javax.swing.*;

// This class represents a fill-in-the-blank question in a GUI format.
// It extends the GUIQuestion class and provides methods to create a panel for the question
// and check the entered answer against the correct answer.
// It includes a text field for user input and methods to validate the answer.

public class GUIFillBlankQuestion implements GUIQuestion {
    private final String questionText;
    private final String correctAnswer;
    private final JPanel panel;
    private final JTextField answerField = new JTextField(20);

    // Constructor to initialise the question text and correct answer.
    public GUIFillBlankQuestion(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.panel = createPanel();
    }

    // This method creates the panel for the fill-in-the-blank question.
    private JPanel createPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><b>" + questionText + "</b></html>");
        label.setFont(new Font("Arial", Font.BOLD, 16)); 
        p.add(label, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel answerLabel = new JLabel("Your answer:");
        answerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(answerLabel);
        
        // Text field with larger font and size
        answerField.setFont(new Font("Arial", Font.PLAIN, 14));
        answerField.setPreferredSize(new Dimension(200, 30)); 

        inputPanel.add(new JLabel());
        inputPanel.add(answerField);
        p.add(inputPanel, BorderLayout.CENTER);

        return p;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    // This method checks the user's answer against the correct answer.
    @Override
    public boolean checkAnswer() {
        String userAnswer = answerField.getText().trim();
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }

    // This method returns the text field for user input.
    public JTextField getField() {
        return answerField;
    }
}
