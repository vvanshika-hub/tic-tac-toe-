package com.minecraft.mobbattle;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * StatisticsFrame Class
 * Displays detailed player stats in a card-grid format.
 * Implements composition by holding a private JFrame instance.
 * No custom inheritance hierarchy.
 */
public class StatisticsFrame {
    private JFrame frame;
    private Player currentPlayer;
    private PlayerService playerService;

    // UI elements to update dynamically on refresh
    private JLabel usernameValLabel;
    private JLabel winsValLabel;
    private JLabel lossesValLabel;
    private JLabel drawsValLabel;
    private JLabel scoreValLabel;

    public StatisticsFrame(Player player) {
        this.currentPlayer = player;
        this.playerService = new PlayerService();
        initialize();
    }

    /**
     * Initializes UI layout and logic.
     */
    private void initialize() {
        // Create standard JFrame
        frame = new JFrame("Minecraft Mob Battle - My Statistics");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 480);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(false);

        // Frame window listener to redirect back to Main Menu on close
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                goBack();
            }
        });

        // 1. Header Banner Panel (Dirt Brown Background)
        JPanel headerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_DIRT_BROWN);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel titleLabel = GameStyle.createMinecraftLabel("MY STATISTICS", GameStyle.FONT_TITLE, GameStyle.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel subtitleLabel = GameStyle.createMinecraftLabel("Battle records for: " + currentPlayer.getUsername(), GameStyle.FONT_HEADER, GameStyle.COLOR_WHITE);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // 2. Statistics Cards Grid (Stone Gray Background)
        JPanel cardsGrid = GameStyle.createMinecraftPanel(GameStyle.COLOR_STONE_GRAY);
        cardsGrid.setLayout(new GridLayout(5, 1, 8, 8)); // 5 vertical rows for cards
        cardsGrid.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // STUDENT TASK 9: StatisticsFrame
        // COMPLETED: Show personal statistics from database on card grid.
        cardsGrid.add(createStatCard("Defender Name", usernameValLabel = new JLabel(currentPlayer.getUsername())));
        cardsGrid.add(createStatCard("Victories (Wins)", winsValLabel = new JLabel(String.valueOf(currentPlayer.getWins()))));
        cardsGrid.add(createStatCard("Defeats (Losses)", lossesValLabel = new JLabel(String.valueOf(currentPlayer.getLosses()))));
        cardsGrid.add(createStatCard("Draws (Stalemates)", drawsValLabel = new JLabel(String.valueOf(currentPlayer.getDraws()))));
        cardsGrid.add(createStatCard("Total Score", scoreValLabel = new JLabel(currentPlayer.getScore() + " Points")));

        // Style the labels to be consistent in the cards
        styleValueLabel(usernameValLabel);
        styleValueLabel(winsValLabel);
        styleValueLabel(lossesValLabel);
        styleValueLabel(drawsValLabel);
        styleValueLabel(scoreValLabel);

        // 3. Footer Action Buttons Panel (Dark Gray Background)
        JPanel footerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_DARK_GRAY);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton backBtn = GameStyle.createMinecraftButton("⬅ Back", GameStyle.COLOR_DIRT_BROWN, GameStyle.COLOR_DIRT_BROWN.brighter());
        JButton refreshBtn = GameStyle.createMinecraftButton("🔄 Refresh", GameStyle.COLOR_GRASS_GREEN, GameStyle.COLOR_LIGHT_GREEN);

        footerPanel.add(backBtn);
        footerPanel.add(refreshBtn);

        // Action Listeners
        backBtn.addActionListener(e -> goBack());
        refreshBtn.addActionListener(e -> refreshStats());

        // Assemble Layout
        frame.setLayout(new BorderLayout());
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(cardsGrid, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Helper to style the statistical value labels on screen.
     */
    private void styleValueLabel(JLabel label) {
        label.setFont(GameStyle.FONT_HEADER);
        label.setForeground(GameStyle.COLOR_GOLD);
        label.setHorizontalAlignment(JLabel.RIGHT);
    }

    /**
     * Helper to create a single card panel for displaying a stat.
     */
    private JPanel createStatCard(String labelText, JLabel valLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(GameStyle.COLOR_DARK_GRAY);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DIRT_BROWN, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        JLabel nameLabel = GameStyle.createMinecraftLabel(labelText, GameStyle.FONT_LABEL, GameStyle.COLOR_WHITE);
        
        card.add(nameLabel, BorderLayout.WEST);
        card.add(valLabel, BorderLayout.EAST);
        return card;
    }

    /**
     * Reloads values from database and repaints components.
     */
    private void refreshStats() {
        // COMPLETED: Fetch latest data from database using playerService
        Player updated = playerService.getPlayerById(currentPlayer.getId());
        if (updated != null) {
            currentPlayer = updated;
            winsValLabel.setText(String.valueOf(currentPlayer.getWins()));
            lossesValLabel.setText(String.valueOf(currentPlayer.getLosses()));
            drawsValLabel.setText(String.valueOf(currentPlayer.getDraws()));
            scoreValLabel.setText(currentPlayer.getScore() + " Points");
            JOptionPane.showMessageDialog(frame, "Statistics updated with latest records!", "Sync Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to load latest records from database.", "Sync Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Disposes this frame and returns to MainMenuFrame.
     */
    private void goBack() {
        frame.dispose();
        MainMenuFrame menu = new MainMenuFrame(currentPlayer);
        menu.show();
    }

    /**
     * Renders frame visible.
     */
    public void show() {
        frame.setVisible(true);
    }
}
