package com.minecraft.mobbattle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * MainMenuFrame Class
 * The post-login navigation hub.
 * Implements composition by managing a private JFrame instance.
 * No custom inheritance hierarchy.
 */
public class MainMenuFrame {
    private JFrame frame;
    private Player currentPlayer;

    public MainMenuFrame(Player player) {
        this.currentPlayer = player;
        initialize();
    }

    /**
     * Initializes UI layout and logic.
     */
    private void initialize() {
        // Create standard JFrame
        frame = new JFrame("Minecraft Mob Battle - Main Menu");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(false);

        // Window listener to confirm exit
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        // 1. Header Banner Panel (Grass Green Background)
        JPanel headerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_GRASS_GREEN);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel titleLabel = GameStyle.createMinecraftLabel("MINECRAFT MOB BATTLE", GameStyle.FONT_TITLE, GameStyle.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel subtitleLabel = GameStyle.createMinecraftLabel("Welcome, " + currentPlayer.getUsername() + "!", GameStyle.FONT_HEADER, GameStyle.COLOR_WHITE);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // 2. Buttons Menu Panel (Stone Gray Background)
        JPanel menuPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_STONE_GRAY);
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(25, 50, 25, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;

        // Button 1: Start Battle
        JButton startBtn = GameStyle.createMinecraftButton("⚔ Start Battle", GameStyle.COLOR_GRASS_GREEN, GameStyle.COLOR_LIGHT_GREEN);
        gbc.gridy = 0;
        menuPanel.add(startBtn, gbc);

        // Button 2: My Statistics
        JButton statsBtn = GameStyle.createMinecraftButton("📊 My Statistics", GameStyle.COLOR_STONE_GRAY, GameStyle.COLOR_LIGHT_GRAY);
        gbc.gridy = 1;
        menuPanel.add(statsBtn, gbc);

        // Button 3: Top Defenders
        JButton leaderBtn = GameStyle.createMinecraftButton("🏆 Top Defenders", GameStyle.COLOR_STONE_GRAY, GameStyle.COLOR_LIGHT_GRAY);
        gbc.gridy = 2;
        menuPanel.add(leaderBtn, gbc);

        // Button 4: Logout
        JButton logoutBtn = GameStyle.createMinecraftButton("🚪 Logout", GameStyle.COLOR_DIRT_BROWN, GameStyle.COLOR_DIRT_BROWN.brighter());
        gbc.gridy = 3;
        menuPanel.add(logoutBtn, gbc);

        // Button 5: Exit World
        JButton exitBtn = GameStyle.createMinecraftButton("❌ Exit World", GameStyle.COLOR_DARK_GRAY, GameStyle.COLOR_STONE_GRAY);
        gbc.gridy = 4;
        menuPanel.add(exitBtn, gbc);

        // STUDENT TASK 6: MainMenuFrame
        // COMPLETED: Complete navigation buttons to swap frames.
        startBtn.addActionListener(e -> {
            frame.dispose();
            GameFrame gameFrame = new GameFrame(currentPlayer);
            gameFrame.show();
        });

        statsBtn.addActionListener(e -> {
            frame.dispose();
            StatisticsFrame statsFrame = new StatisticsFrame(currentPlayer);
            statsFrame.show();
        });

        leaderBtn.addActionListener(e -> {
            frame.dispose();
            TopScorersFrame leaderFrame = new TopScorersFrame(currentPlayer);
            leaderFrame.show();
        });

        logoutBtn.addActionListener(e -> confirmLogout());
        exitBtn.addActionListener(e -> confirmExit());

        // Assemble Frame Layout
        frame.setLayout(new BorderLayout());
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(menuPanel, BorderLayout.CENTER);
    }

    /**
     * Prompts JOption dialog to confirm logging out.
     */
    private void confirmLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to log out, Defender?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.show();
        }
    }

    /**
     * Prompts JOption dialog to confirm exit.
     */
    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to exit the world?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Renders frame visible.
     */
    public void show() {
        frame.setVisible(true);
    }
}
