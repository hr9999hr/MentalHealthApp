//Creator: Tan Boon Sang (101173)
//Tester: Chong Siat Yii (98499)

package interfaces;

// This interface defines methods for scoring functionality in a quiz or game application
// It includes methods to get the score, calculate it, show feedback and save the score

public interface Scorable {
    int getScore();
    void calculateScore();
    String showFeedback();
    void saveScore(String username);
}
