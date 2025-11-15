//Creator: Wong Jing Yi (101350)
//Tester: Tan Boon Sang (101173)

package interfaces;

// This interface defines methods for playing audio and video files.
// It includes methods to play, stop, pause, and resume audio playback.

public interface PlayableMedia {
    void playAudio(String filepath);
    void playVideo(String filepath);
    void stopAudio();
    void pauseAudio();
    void resumeAudio();
}
