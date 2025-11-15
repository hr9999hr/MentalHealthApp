//Creator: Chong Siat Yii (98499)
//Tester: Ang Hui Ru (98167)

package interfaces;

import java.awt.*;
import javax.swing.*;

// This class represents a true/false question in a GUI format.
// It extends the GUIQuestion class and provides methods to create a panel for the question
// and check the selected answer against the correct answer.
// It includes two radio buttons for true and false options and methods to validate the answer.

public class GUITFQuestion implements GUIQuestion {
    private final String questionText;
    private final boolean correctAnswer;
    private final JPanel panel;
    private final JRadioButton trueBtn = new JRadioButton("True");
    private final JRadioButton falseBtn = new JRadioButton("False");
    private final ButtonGroup group = new ButtonGroup();

    // Constructor to initialise the question text and correct answer.
    public GUITFQuestion(String questionText, boolean correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.panel = createPanel();
    }

    // This method creates the panel for the true/false question.
    private JPanel createPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><b>" + questionText + "</b></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        p.add(label, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(1, 2));

        trueBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        falseBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        
        trueBtn.setPreferredSize(new Dimension(120, 40));
        falseBtn.setPreferredSize(new Dimension(120, 40));

        group.add(trueBtn);
        group.add(falseBtn);
        optionsPanel.add(trueBtn);
        optionsPanel.add(falseBtn);
        p.add(optionsPanel, BorderLayout.CENTER);
        return p;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public boolean checkAnswer() {
        if (trueBtn.isSelected()) {
            return correctAnswer;
        } else if (falseBtn.isSelected()) {
            return !correctAnswer;
        }
        return false;
    }

    // This method returns the radio buttons for true option.
    public JRadioButton getTrueBtn() {
        return trueBtn;
    }

    // This method returns the radio button for false option.
    public JRadioButton getFalseBtn() {
        return falseBtn;
    }
}

