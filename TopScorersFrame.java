package com.minecraft.mobbattle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * TopScorersFrame Class
 * Displays the Top 5 Overworld Defenders leaderboard in a JTable.
 * Implements composition by holding a private JFrame instance.
 * No custom inheritance hierarchy.
 */
public class TopScorersFrame {
    private JFrame frame;
    private Player currentPlayer;
    private PlayerService playerService;

    public TopScorersFrame(Player player) {
        this.currentPlayer = player;
        this.playerService = new PlayerService();
        initialize();
    }

    /**
     * Initializes UI layout and logic.
     */
    private void initialize() {
        // Create standard JFrame
        frame = new JFrame("Minecraft Mob Battle - Top Defenders");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 480);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(false);

        // Frame window listener to redirect back to Main Menu on close
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                goBack();
            }
        });

        // 1. Header Banner Panel (Grass Green Background)
        JPanel headerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_GRASS_GREEN);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel titleLabel = GameStyle.createMinecraftLabel("LEADERBOARD", GameStyle.FONT_TITLE, GameStyle.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel subtitleLabel = GameStyle.createMinecraftLabel("Top Defenders of the Overworld", GameStyle.FONT_HEADER, GameStyle.COLOR_WHITE);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // 2. Body Panel with Leaderboard JTable (Stone Gray Background)
        JPanel bodyPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_STONE_GRAY);
        bodyPanel.setLayout(new BorderLayout());
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Column Names
        String[] columnNames = {"Rank", "Defender Name", "Wins 🟩", "Losses 🧟", "Draws 🏳", "Score 🏆"};

        // STUDENT TASK 10: TopScorersFrame
        // COMPLETED: Show Top 5 scorers using JTable.
        // Fetch data from database using playerService
        List<Player> topDefenders = playerService.getTopFiveScorers();
        Object[][] rowData = new Object[5][6];

        for (int i = 0; i < 5; i++) {
            if (i < topDefenders.size()) {
                Player p = topDefenders.get(i);
                rowData[i][0] = "Rank #" + (i + 1);
                rowData[i][1] = p.getUsername();
                rowData[i][2] = p.getWins();
                rowData[i][3] = p.getLosses();
                rowData[i][4] = p.getDraws();
                rowData[i][5] = p.getScore() + " pts";
            } else {
                // Empty rows if there are less than 5 players registered
                rowData[i][0] = "Rank #" + (i + 1);
                rowData[i][1] = "---";
                rowData[i][2] = 0;
                rowData[i][3] = 0;
                rowData[i][4] = 0;
                rowData[i][5] = "0 pts";
            }
        }

        // Initialize Table Model and Table without custom inheritance
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(model);
        
        // Prevent editing table cells
        table.setDefaultEditor(Object.class, null);
        table.setEnabled(false); // Disables cell selection and double-click editing
        
        // Styling JTable
        table.setFont(GameStyle.FONT_REGULAR);
        table.setForeground(GameStyle.COLOR_WHITE);
        table.setBackground(GameStyle.COLOR_STONE_GRAY);
        table.setGridColor(GameStyle.COLOR_DARK_GRAY);
        table.setRowHeight(32);

        // Customize Table Header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(GameStyle.FONT_BUTTON);
        tableHeader.setBackground(GameStyle.COLOR_DIRT_BROWN);
        tableHeader.setForeground(GameStyle.COLOR_GOLD);
        tableHeader.setBorder(BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 1));
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 35));
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(false);

        // Add Scroll Pane with Minecraft Border
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.getViewport().setBackground(GameStyle.COLOR_STONE_GRAY);

        bodyPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. Footer Action Panel (Dark Gray Background)
        JPanel footerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_DARK_GRAY);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton backBtn = GameStyle.createMinecraftButton("⬅ Back", GameStyle.COLOR_DIRT_BROWN, GameStyle.COLOR_DIRT_BROWN.brighter());
        footerPanel.add(backBtn);

        // Action Listener
        backBtn.addActionListener(e -> goBack());

        // Assemble Layout
        frame.setLayout(new BorderLayout());
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(bodyPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);
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
