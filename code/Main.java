//Creator: Wong Jing Yi (101350)
//Tester: Tan Boon Sang (101173)

import interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import modules.*;

// This is the main class for the MoodyU application.
// It initializes the application, handles user input, and manages the main menu and learning modules.

public class Main {
    private static int currentIndex = 0;
    private static List<LearningModule> modules;
    private static JFrame frame;
    private static JPanel modulePanel;
    private static String username;

    public static void main(String[] args) {
        try {
            // Load learning modules from the specified file
            modules = LearningModuleLoader.getModulesFromFile("/Users/drturtle/Downloads/G01_CS-G01-Project/Assets/LearningModule.txt");
            if (modules == null || modules.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No modules found.");
                System.exit(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to load learning modules:\n" + e.getMessage());
            System.exit(0);
        }

        SwingUtilities.invokeLater(Main::inputUserName);

    }
    
    // This method prompts the user to input their name
    private static void inputUserName() {
        JFrame inputFrame = new JFrame("MoodyU");
        inputFrame.setSize(360, 800);
        inputFrame.setLayout(new BorderLayout());
        inputFrame.getContentPane().setBackground(new Color(240, 245, 255));
        
        JLabel titleLabel = new JLabel("MoodyU");
        titleLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(new Color(240, 245, 255));
        
        JLabel instruction = new JLabel("Enter your name:");
        instruction.setFont(new Font("Arial", Font.PLAIN, 18));
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Text field for user input
        JTextField nameField = new JTextField(15);
        nameField.setMaximumSize(new Dimension(250, 40));
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton submitBtn = new JButton("Continue");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setBackground(new Color(91, 155, 213));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setPreferredSize(new Dimension(120, 45));
        
        submitBtn.addActionListener(e -> {
            username = nameField.getText().trim();
            if(username.matches("[a-zA-Z ]+")) {
                inputFrame.dispose();
                showMenu();
            } else {
                JOptionPane.showMessageDialog(inputFrame, "Please enter only letters", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(instruction);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(nameField);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(submitBtn);

        inputFrame.add(contentPanel, BorderLayout.CENTER);
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setVisible(true);
    }

    // This method displays the main menu with options for learning modules, quiz, and leaderboard
    // It also handles window closing events to confirm exit
    public static void showMenu() {
        JFrame menuFrame = new JFrame("Welcome, " + username + "!");
        menuFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        menuFrame.setSize(360, 800);
        
        menuFrame.addWindowListener(new WindowAdapter() {
            // This method reconfirms exit when user tries to close the window
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(menuFrame, "Exit application?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    if (currentIndex < modules.size()) {
                        modules.get(currentIndex).stopAudio();
                    }
                    System.exit(0);
                }
            }
        });
        
        // Create main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 248, 255), 0, getHeight(), new Color(220, 230, 240));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // App icon
        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon("Assets/logo.png");
        if (icon.getImage() != null) {
            Image scaledIcon = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledIcon));
        } else {
            iconLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        }
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Hello, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(70, 86, 143));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
        
        // Create menu buttons with consistent styling
        JButton modulesBtn = new JButton("Learning Modules");
        modulesBtn.setFont(new Font("Arial", Font.BOLD, 18));
        modulesBtn.setMaximumSize(new Dimension(280, 60));
        modulesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        modulesBtn.setBackground(new Color(91, 155, 213));
        modulesBtn.setForeground(Color.WHITE);
        modulesBtn.setFocusPainted(false);
        modulesBtn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        modulesBtn.setOpaque(true);
        
        // Quiz and leaderboard buttons
        JButton quizBtn = new JButton("Mental Health Quiz");
        quizBtn.setFont(new Font("Arial", Font.BOLD, 18));
        quizBtn.setMaximumSize(new Dimension(280, 60));
        quizBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizBtn.setBackground(new Color(150, 113, 190));
        quizBtn.setForeground(Color.WHITE);
        quizBtn.setFocusPainted(false);
        quizBtn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        quizBtn.setOpaque(true);
        
        JButton leaderboardBtn = new JButton("Leaderboard");
        leaderboardBtn.setFont(new Font("Arial", Font.BOLD, 18));
        leaderboardBtn.setMaximumSize(new Dimension(280, 60));
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setBackground(new Color(92, 184, 92));
        leaderboardBtn.setForeground(Color.WHITE);
        leaderboardBtn.setFocusPainted(false);
        leaderboardBtn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        leaderboardBtn.setOpaque(true);
        
        modulesBtn.addActionListener(e -> {
            menuFrame.dispose();
            createAndShowGUI();
        });
        
        quizBtn.addActionListener(e -> {
            menuFrame.dispose();
            QuizManagerInterface quiz = new QuizManager();
            JFrame quizFrame = quiz.getFrame();
            quizFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            quizFrame.addWindowListener(new WindowAdapter() {
                // This method handles the window closing event for the quiz frame
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        int score = quiz.getScore();
                        int total = quiz.getTotalQuestions();
                        int elapsed = quiz.getElapsedSeconds();
                        ScoreManager scorer = new ScoreManager(score, total, elapsed);
                        String feedback = scorer.showFeedback() + "\nTime taken: " + elapsed + " seconds";

                        // Show feedback in a phone-sized, scrollable dialog
                        JDialog dialog = new JDialog((JFrame) null, "Your Score", true);
                        dialog.setSize(360, 700);
                        dialog.setLocationRelativeTo(null);

                        JTextArea textArea = new JTextArea(feedback);
                        textArea.setEditable(false);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
                        JScrollPane scrollPane = new JScrollPane(textArea);

                        dialog.setLayout(new java.awt.BorderLayout());
                        dialog.add(scrollPane, java.awt.BorderLayout.CENTER);

                        JButton closeButton = new JButton("Close");
                        closeButton.addActionListener(ev -> dialog.dispose());
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(closeButton);
                        dialog.add(buttonPanel, java.awt.BorderLayout.SOUTH);

                        dialog.setVisible(true);

                        scorer.saveScore(username);
                        SwingUtilities.invokeLater(Main::showMenu); 

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while scoring.");
                    }
                }
            });
            quizFrame.setVisible(true);
        });
        
        leaderboardBtn.addActionListener(e -> LeaderboardManager.showLeaderboard(menuFrame));
        
        // Add components to panel
        mainPanel.add(iconLabel);
        mainPanel.add(welcomeLabel);
        mainPanel.add(modulesBtn);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(quizBtn);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(leaderboardBtn);
        mainPanel.add(Box.createVerticalGlue());
        
        menuFrame.setContentPane(mainPanel);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    // This method creates and displays the main GUI for the learning modules
    private static void createAndShowGUI() {
        frame = new JFrame("MoodyU App");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                modules.get(currentIndex).stopAudio(); // Stop any playing audio
                frame.dispose();
                showMenu();
                }
            });
        frame.setSize(360, 800);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        
        modulePanel = new JPanel(new BorderLayout());
        modulePanel.setBackground(new Color(240, 248, 255));
        
        // Navigation panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navPanel.setBackground(new Color(220, 230, 240));
        
        // Create navigation buttons
        JButton prevBtn = new JButton("← Previous");
        prevBtn.setFont(new Font("Arial", Font.BOLD, 14));
        prevBtn.setBackground(new Color(91, 155, 213));
        prevBtn.setForeground(Color.WHITE);
        prevBtn.setFocusPainted(false);
        prevBtn.setPreferredSize(new Dimension(120, 40));
        prevBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JButton nextBtn = new JButton("Next →");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 14));
        nextBtn.setBackground(new Color(91, 155, 213));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.setPreferredSize(new Dimension(120, 40));
        nextBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JButton backBtn = new JButton("Back to Menu");
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setBackground(new Color(92, 184, 92));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Add action listeners
        prevBtn.addActionListener(e -> navigateModule(-1));
        nextBtn.addActionListener(e -> navigateModule(1));
        backBtn.addActionListener(e -> {
            modules.get(currentIndex).stopAudio();
            frame.dispose();
            showMenu();
        });
        
        navPanel.add(prevBtn);
        navPanel.add(nextBtn);
        navPanel.add(backBtn);
        
        contentPanel.add(modulePanel, BorderLayout.CENTER);
        contentPanel.add(navPanel, BorderLayout.SOUTH);
        
        frame.setContentPane(contentPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        showModule(currentIndex);
    }

    // This method handles navigation between learning modules
    // It updates the current index and displays the appropriate module
    private static void navigateModule(int delta) {
        modules.get(currentIndex).stopAudio();
        currentIndex += delta;
        if (currentIndex < 0) currentIndex = 0;
        
        if (currentIndex >= modules.size()) {
            int option = JOptionPane.showOptionDialog(frame, "All modules completed. Proceed to quiz?", "Complete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new Object[]{"Back", "Quiz"}, "Quiz");
            if (option == 1) {
                frame.dispose();
                QuizManagerInterface quiz = new QuizManager();
                JFrame quizFrame = quiz.getFrame();
                quizFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Handle feedback and return to menu
                quizFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try {
                            int score = quiz.getScore();
                            int total = quiz.getTotalQuestions();
                            int elapsed = quiz.getElapsedSeconds();
                            ScoreManager scorer = new ScoreManager(score, total, elapsed);
                            String feedback = scorer.showFeedback() + "\nTime taken: " + elapsed + " seconds";

                            JDialog dialog = new JDialog((JFrame) null, "Your Score", true);
                            dialog.setSize(360, 700);
                            dialog.setLocationRelativeTo(null);

                            JTextArea textArea = new JTextArea(feedback);
                            textArea.setEditable(false);
                            textArea.setLineWrap(true);
                            textArea.setWrapStyleWord(true);
                            textArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
                            JScrollPane scrollPane = new JScrollPane(textArea);

                            dialog.setLayout(new java.awt.BorderLayout());
                            dialog.add(scrollPane, java.awt.BorderLayout.CENTER);

                            JButton closeButton = new JButton("Close");
                            closeButton.addActionListener(ev -> dialog.dispose());
                            JPanel buttonPanel = new JPanel();
                            buttonPanel.add(closeButton);
                            dialog.add(buttonPanel, java.awt.BorderLayout.SOUTH);

                            dialog.setVisible(true);

                            scorer.saveScore(username);
                            SwingUtilities.invokeLater(Main::showMenu);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "An error occurred while scoring.");
                        }
                    }
                });

                quizFrame.setVisible(true);
                return;
            } else {
                currentIndex = modules.size() - 1;
            }
        }
        
        showModule(currentIndex);
    }

    // This method displays the selected learning module in the main panel
    // It stops any audio from the previous module and updates the display panel
    private static void showModule(int index) {
        modules.get(index).stopAudio();
        modulePanel.removeAll();
        
        JScrollPane scrollPane = new JScrollPane(modules.get(index).getDisplayPanel());
        scrollPane.setPreferredSize(new Dimension(340, 560));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        modulePanel.add(scrollPane, BorderLayout.CENTER);
        
        frame.revalidate();
        frame.repaint();
    }
}