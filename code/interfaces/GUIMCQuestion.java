//Creator: Chong Siat Yii (98499)
//Tester: Ang Hui Ru (98167)

package interfaces;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// This class represents a multiple-choice question in a GUI format.
// It extends the GUIQuestion class and provides methods to create a panel for the question
// and check the selected answer against the correct answer.

public class GUIMCQuestion implements GUIQuestion {
    private final String questionText;
    private final List<String> options;
    private final String correctAnswer;
    private final JPanel panel;
    private final List<JRadioButton> optionButtons = new ArrayList<>();
    private final ButtonGroup group = new ButtonGroup();

    // Constructor to initialise the question text, options, and correct answer.
    public GUIMCQuestion(String questionText, List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.panel = createPanel();
    }

    private JPanel createPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><b>" + questionText + "</b></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        p.add(label, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout (options.size(), 1));
        for (String opt : options) {
            JRadioButton rb = new JRadioButton(opt);
            rb.setFont(new Font("Arial", Font.PLAIN, 14));
            group.add(rb);
            optionButtons.add(rb);
            optionsPanel.add(rb);
        }
        p.add(optionsPanel, BorderLayout.CENTER);
        return p;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public boolean checkAnswer() {
        for (JRadioButton rb : optionButtons) {
            if (rb.isSelected()) {
                return rb.getText().equalsIgnoreCase(correctAnswer);
            }
        }
        return false;
    }

    // This method returns the list of options for user to choose
    public List<JRadioButton> getOptions() {
        return optionButtons;
    }
}
