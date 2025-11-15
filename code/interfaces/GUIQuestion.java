//Creator: Ang Hui Ru (98167)
//Tester: Wong Jing Yi (101350)

package interfaces;

import javax.swing.*;

// This interface represents a generic question in a GUI format.
// It defines the structure for different types of questions (e.g., multiple-choice, true/false, fill-in-the-blank)
// and provides methods to create a panel for the question and check the answer.

public interface GUIQuestion {
    JPanel getPanel();
    boolean checkAnswer();
}
