//Creator: Wong Jing Yi (101350)
//Tester: Tan Boon Sang (101173)

package modules;

import interfaces.Displayable;
import interfaces.PlayableMedia;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

// Represents a learning module with multimedia content
// Implements Displayable for GUI representation and PlayableMedia for audio/video playback
public class LearningModule implements Displayable, PlayableMedia {
    private final String title;
    private final String summary;
    private final String[] points;
    private final String visualCue;
    private final String imagePath;
    private final String audioPath;
    private final String videoPath;
    private Clip audioClip;
    private boolean isPaused = false;
    private int pauseFramePosition = 0;

    // Constructor to initialise the learning module with all necessary fields
    public LearningModule(String title, String summary, String[] points, String visualCue,
                          String imagePath, String audioPath, String videoPath) {
        this.title = title;
        this.summary = summary;
        this.points = points;
        this.visualCue = visualCue;
        this.imagePath = imagePath;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
    }

    @Override
    public JPanel getDisplayPanel() {
    // Auto-play audio when panel opens
    if (audioPath != null && !audioPath.isEmpty()) {
        playAudio(audioPath);
    }

    JPanel panel = new JPanel(new BorderLayout(10,10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    panel.setBackground(new Color(250, 252, 255));
    
    // Title with styling
    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setForeground(new Color(70, 130, 180));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Content panel
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(new Color(250, 252, 255));
    
    // Content area
    JTextArea contentArea = new JTextArea();
    contentArea.append(summary + "\n\n");
    contentArea.setFont(new Font("Arial", Font.PLAIN, 16));
    contentArea.setForeground(new Color(60, 60, 80));
    for (String point : points) contentArea.append(point + "\n");
    contentArea.setEditable(false);
    contentArea.setWrapStyleWord(true);
    contentArea.setLineWrap(true);
    contentArea.setBackground(new Color(250, 252, 255));
    
    // Constrain width to phone screen size (340 = 360 - 20px padding)
    JScrollPane contentScroll = new JScrollPane(contentArea);
    contentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    contentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    contentScroll.setPreferredSize(new Dimension(340, 100)); // Fixed height
    contentPanel.add(contentScroll, BorderLayout.CENTER);
    panel.add(contentPanel, BorderLayout.CENTER);

    JPanel mediaPanel = new JPanel();
    mediaPanel.setLayout(new BoxLayout(mediaPanel, BoxLayout.Y_AXIS));
    mediaPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    mediaPanel.setBackground(new Color(250, 252, 255));
    
    if (visualCue != null && !visualCue.isEmpty()) {
        try {
            ImageIcon icon = new ImageIcon(visualCue);
            JLabel iconLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mediaPanel.add(iconLabel);
            mediaPanel.add(Box.createVerticalStrut(10));
        } catch (Exception err) {
            JLabel errorLabel = new JLabel("Icon not found");
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mediaPanel.add(errorLabel);
        }
    }

    // Image display
    if (imagePath != null && !imagePath.isEmpty()) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            JLabel imgLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center image
            mediaPanel.add(imgLabel);
            mediaPanel.add(Box.createVerticalStrut(10)); // Spacer
        } catch (Exception err) {
            JLabel errorLabel = new JLabel("Image not found");
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mediaPanel.add(errorLabel);
        }
    }
    
    // Audio controls
    if (audioPath != null && !audioPath.isEmpty()) {
        JPanel audioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        audioPanel.setBackground(new Color(250, 252, 255));
        
        JButton pauseBtn = new JButton("Pause");
        pauseBtn.setFont(new Font("Arial", Font.BOLD, 14));
        pauseBtn.setBackground(new Color(150, 113, 190));
        pauseBtn.setForeground(Color.WHITE);
        pauseBtn.addActionListener(e -> pauseAudio());
        
        JButton resumeBtn = new JButton("Resume");
        resumeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        resumeBtn.setBackground(new Color(91, 155, 213));
        resumeBtn.setForeground(Color.WHITE);
        resumeBtn.addActionListener(e -> resumeAudio());
        
        JButton stopBtn = new JButton("Stop");
        stopBtn.setFont(new Font("Arial", Font.BOLD, 14));
        stopBtn.setBackground(new Color(200, 80, 80));
        stopBtn.setForeground(Color.WHITE);
        stopBtn.addActionListener(e -> stopAudio());
        
        audioPanel.add(pauseBtn);
        audioPanel.add(resumeBtn);
        audioPanel.add(stopBtn);
        audioPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mediaPanel.add(audioPanel);
        mediaPanel.add(Box.createVerticalStrut(10));
    }
    
    // Video button
    if (videoPath != null && !videoPath.isEmpty()) {
        JButton videoBtn = new JButton("Play Video");
        videoBtn.setFont(new Font("Arial", Font.BOLD, 14));
        videoBtn.setBackground(new Color(92, 184, 92));
        videoBtn.setForeground(Color.WHITE);
        videoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        videoBtn.addActionListener(e -> {
            stopAudio(); // stop before video
            try {
                Desktop.getDesktop().open(new File(videoPath));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Video file not found.");
            }
        });
        mediaPanel.add(videoBtn);
        mediaPanel.add(Box.createVerticalStrut(10));
    }
    
    // HEALING EXERCISE BUTTON
    if (title.toLowerCase().contains("breath") || title.toLowerCase().contains("relax")) {
        JButton startBtn = new JButton("Start Exercise");
        startBtn.setFont(new Font("Arial", Font.BOLD, 14));
        startBtn.setBackground(new Color(220, 160, 30));
        startBtn.setForeground(Color.WHITE);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> playVideo()); // Uses overloaded method
        mediaPanel.add(startBtn);
    }

    // Media panel in scroll if needed
    JScrollPane mediaScroll = new JScrollPane(mediaPanel);
    mediaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    mediaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    mediaScroll.setPreferredSize(new Dimension(340, 150)); // Fixed height
    mediaScroll.setBorder(BorderFactory.createEmptyBorder());
    panel.add(mediaScroll, BorderLayout.SOUTH);

    return panel;
}

    // Method to play audio from a file path
    @Override
    public void playAudio(String filePath) {
        try {
            if (audioClip != null) {
                audioClip.stop();
                audioClip.close();
            }
            isPaused = false;
            pauseFramePosition = 0;

            File file = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioIn);
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            JOptionPane.showMessageDialog(null, "Audio file not playable.");
        }
    }

    // Overloaded method to pause audio playback
    @Override
    public void pauseAudio() {
        if (audioClip != null && audioClip.isRunning()) {
            pauseFramePosition = audioClip.getFramePosition();
            audioClip.stop();
            isPaused = true;
        }
    }

    // Overloaded method to resume audio playback
    @Override
    public void resumeAudio() {
        if (audioClip != null && isPaused) {
            audioClip.setFramePosition(pauseFramePosition);
            audioClip.start();
            isPaused = false;
        }
    }

    // Method to stop audio playback
    @Override
    public void stopAudio() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
        }
        isPaused = false;
        pauseFramePosition = 0;
    }

    // Method to play video from a file path
    @Override
    public void playVideo(String filePath) {
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Video file not found.");
        }
    }

    public void playVideo() {
        playVideo(this.videoPath);
    }

    // Getters
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String[] getPoints() { return points; }
    public String getVisualCue() { return visualCue; }
    public String getImagePath() { return imagePath; }
    public String getAudioPath() { return audioPath; }
    public String getVideoPath() { return videoPath; }
}
