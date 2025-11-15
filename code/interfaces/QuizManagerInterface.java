//Creator: Chong Siat Yii (98499)
//Tester: Ang Hui Ru (98167)

package interfaces;

import javax.swing.*;

// This interface defines the methods required for a quiz manager in a GUI application.
// It includes methods to load questions, display questions and results, retrieve scores,

public interface QuizManagerInterface {
    void loadQuestions();
    void showQuestion();
    void showResult();
    int getScore();
    int getTotalQuestions();
    JFrame getFrame();
    int getElapsedSeconds();
}
