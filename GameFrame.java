package com.minecraft.mobbattle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GameFrame Class
 * Renders the 3x3 Minecraft battlefield.
 * Manages game states, clicks, delayed AI moves, and database writes.
 * 
 * Implements composition by managing a private JFrame instance.
 * No custom inheritance is utilized.
 */
public class GameFrame {
    private JFrame frame;
    private JButton[] gridButtons;
    private JLabel statusLabel;
    private Player currentPlayer;
    private PlayerService playerService;
    private GameLogic gameLogic;

    private boolean isPlayerTurn; // flag to block double clicks during computer turn
    private boolean isGameOver;

    public GameFrame(Player player) {
        this.currentPlayer = player;
        this.playerService = new PlayerService();
        this.gameLogic = new GameLogic();
        this.gridButtons = new JButton[9];
        this.isPlayerTurn = true;
        this.isGameOver = false;
        initialize();
    }

    /**
     * Initializes UI layout and logic.
     */
    private void initialize() {
        // Create standard JFrame
        frame = new JFrame("Minecraft Mob Battle - Battlefield");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(550, 600);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(false);

        // Frame window listener to warn about abandoning battle
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmAbandon();
            }
        });

        // 1. Top Panel (Grass Green Background)
        JPanel topPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_GRASS_GREEN);
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));

        JLabel titleLabel = GameStyle.createMinecraftLabel("BATTLEFIELD Arena", GameStyle.FONT_HEADER, GameStyle.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel instructionLabel = GameStyle.createMinecraftLabel("Creeper Commander (You) vs. Zombie Army (AI)", GameStyle.FONT_REGULAR, GameStyle.COLOR_WHITE);
        instructionLabel.setHorizontalAlignment(JLabel.CENTER);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(instructionLabel, BorderLayout.SOUTH);

        // 2. Battlefield 3x3 Grid Panel (Stone Gray Background)
        JPanel gridPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_STONE_GRAY);
        gridPanel.setLayout(new GridLayout(3, 3, 10, 10)); // Grid layout with gaps
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (int i = 0; i < 9; i++) {
            final int index = i;
            // Create a styled blocky button
            JButton btn = GameStyle.createMinecraftButton("", GameStyle.COLOR_STONE_GRAY, GameStyle.COLOR_LIGHT_GRAY);
            btn.setFont(GameStyle.FONT_HEADER);
            btn.setPreferredSize(new Dimension(100, 100));
            
            // STUDENT TASK 8: GameFrame (Event Connections)
            // COMPLETED: Connect game buttons with game logic.
            btn.addActionListener(e -> handleCellClick(index));
            
            gridButtons[index] = btn;
            gridPanel.add(btn);
        }

        // 3. Status Footer Panel (Dark Gray Background)
        JPanel footerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_DARK_GRAY);
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        statusLabel = GameStyle.createMinecraftLabel("Your Turn! Commander, place your CREEPER!", GameStyle.FONT_LABEL, GameStyle.COLOR_GOLD);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(statusLabel, BorderLayout.CENTER);

        // Escape Button to retreat
        JButton retreatBtn = GameStyle.createMinecraftButton("🏳 Retreat", GameStyle.COLOR_DIRT_BROWN, GameStyle.COLOR_DIRT_BROWN.brighter());
        retreatBtn.setPreferredSize(new Dimension(120, 32));
        retreatBtn.setFont(GameStyle.FONT_BUTTON);
        retreatBtn.addActionListener(e -> confirmAbandon());
        footerPanel.add(retreatBtn, BorderLayout.EAST);

        // Assemble Layout
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Triggered when a grid cell is clicked by the player.
     * 
     * @param index grid index 0-8
     */
    private void handleCellClick(int index) {
        // Block actions if it's not the player's turn or game has already ended
        if (!isPlayerTurn || isGameOver) {
            return;
        }

        // Place Player move
        if (gameLogic.makeMove(index, GameLogic.CREEPER)) {
            // Update UI for the player
            JButton clickedButton = gridButtons[index];
            clickedButton.setText("CREEPER");
            // Style it green for Creeper
            styleButtonPlayed(clickedButton, GameStyle.COLOR_CREEPER_BG, Color.GREEN);
            
            // Check if player won
            String winner = gameLogic.checkWinner();
            if (winner != null) {
                isGameOver = true;
                processGameResult(GameLogic.CREEPER);
                return;
            }

            // Check if it's a draw
            if (gameLogic.isDraw()) {
                isGameOver = true;
                processGameResult(null);
                return;
            }

            // If not over, hand turn over to computer
            isPlayerTurn = false;
            statusLabel.setText("Zombie Army is planning its move...");

            // Trigger computer move with a slight delay (400ms) for visual polish
            Timer computerTimer = new Timer(400, e -> {
                executeComputerMove();
            });
            computerTimer.setRepeats(false);
            computerTimer.start();
        }
    }

    /**
     * Executes the Zombie computer move.
     */
    private void executeComputerMove() {
        if (isGameOver) return;

        int compIndex = gameLogic.computerMove();
        if (compIndex != -1) {
            gameLogic.makeMove(compIndex, GameLogic.ZOMBIE);
            
            // Update UI for computer
            JButton compBtn = gridButtons[compIndex];
            compBtn.setText("ZOMBIE");
            // Style it dark gray-green for Zombie
            styleButtonPlayed(compBtn, GameStyle.COLOR_ZOMBIE_BG, Color.LIGHT_GRAY);

            // Check if computer won
            String winner = gameLogic.checkWinner();
            if (winner != null) {
                isGameOver = true;
                processGameResult(GameLogic.ZOMBIE);
                return;
            }

            // Check if draw
            if (gameLogic.isDraw()) {
                isGameOver = true;
                processGameResult(null);
                return;
            }
        }

        // Return turn to player
        isPlayerTurn = true;
        statusLabel.setText("Your Turn! Commander, place your CREEPER!");
    }

    /**
     * Configures a button once it has been played.
     */
    private void styleButtonPlayed(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setEnabled(false); // Disable to prevent clicking again
        
        // Remove standard disabled grey look in Swing
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            protected void paintText(java.awt.Graphics g, javax.swing.AbstractButton b, java.awt.Rectangle textRect, String text) {
                g.setColor(textColor);
                super.paintText(g, b, textRect, text);
            }
        });
    }

    /**
     * Updates database, displays message dialog, and returns to main menu.
     * 
     * @param winner null for stalemate, GameLogic.CREEPER for player, GameLogic.ZOMBIE for computer.
     */
    private void processGameResult(String winner) {
        String msg = "";
        int winsDelta = 0;
        int lossesDelta = 0;
        int drawsDelta = 0;
        int scoreDelta = 0;

        if (winner == null) {
            msg = "Both armies retreated. The battle ended in a stalemate.";
            drawsDelta = 1;
            scoreDelta = 3;
            statusLabel.setText("Stalemate!");
        } else if (winner.equals(GameLogic.CREEPER)) {
            msg = "Victory! The Creepers defended the Overworld!";
            winsDelta = 1;
            scoreDelta = 10;
            statusLabel.setText("Victory!");
        } else if (winner.equals(GameLogic.ZOMBIE)) {
            msg = "The Zombie Army has invaded the village!";
            lossesDelta = 1;
            statusLabel.setText("Defeat!");
        }

        // STUDENT TASK 8: GameFrame (Database Update)
        // COMPLETED: Update wins, losses, draws, and score after game result.
        // Update MySQL database via service layer
        boolean dbUpdated = playerService.updateStatistics(
            currentPlayer.getId(), winsDelta, lossesDelta, drawsDelta, scoreDelta
        );

        if (!dbUpdated) {
            System.err.println("Warning: Could not update player statistics in the database.");
        }

        // Show Game Over Dialog
        JOptionPane.showMessageDialog(frame, msg, "Battle Resolved", JOptionPane.INFORMATION_MESSAGE);

        // Fetch refreshed player data from database and return to Main Menu
        Player refreshedPlayer = playerService.getPlayerById(currentPlayer.getId());
        if (refreshedPlayer == null) {
            refreshedPlayer = currentPlayer; // Fallback
        }

        frame.dispose();
        MainMenuFrame menu = new MainMenuFrame(refreshedPlayer);
        menu.show();
    }

    /**
     * Confirms whether user wants to exit during the game.
     */
    private void confirmAbandon() {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Abandoning the battlefield will count as a tactical retreat!\nAre you sure you want to return to the Main Menu?",
            "Retreat",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            MainMenuFrame menu = new MainMenuFrame(currentPlayer);
            menu.show();
        }
    }

    /**
     * Renders frame visible.
     */
    public void show() {
        frame.setVisible(true);
    }
}
