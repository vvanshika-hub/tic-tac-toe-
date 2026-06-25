package com.minecraft.mobbattle;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * LoginFrame Class
 * Manages the Minecraft-themed login and registration interface.
 * Implements clean Swing composition by holding a private JFrame instance.
 * No custom inheritance hierarchy is created.
 */
public class LoginFrame {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private PlayerService playerService;

    public LoginFrame() {
        playerService = new PlayerService();
        initialize();
    }

    /**
     * Initializes UI layout and logic.
     */
    private void initialize() {
        // Create standard JFrame
        frame = new JFrame("Minecraft Mob Battle");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(false);

        // Frame window listener to confirm exit
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        // 1. Header Banner Panel (Dirt Brown Background)
        JPanel headerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_DIRT_BROWN);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel titleLabel = GameStyle.createMinecraftLabel("MINECRAFT MOB BATTLE", GameStyle.FONT_TITLE, GameStyle.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel subtitleLabel = GameStyle.createMinecraftLabel("Defend the Overworld", GameStyle.FONT_HEADER, GameStyle.COLOR_WHITE);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // 2. Input Fields Panel (Stone Gray Background)
        JPanel bodyPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_STONE_GRAY);
        bodyPanel.setLayout(new GridBagLayout());
        bodyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DARK_GRAY, 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = GameStyle.createMinecraftLabel("Username:", GameStyle.FONT_LABEL, GameStyle.COLOR_WHITE);
        bodyPanel.add(userLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = GameStyle.createMinecraftTextField(15);
        bodyPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passLabel = GameStyle.createMinecraftLabel("Password:", GameStyle.FONT_LABEL, GameStyle.COLOR_WHITE);
        bodyPanel.add(passLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        passwordField.setBackground(GameStyle.COLOR_DARK_GRAY);
        passwordField.setForeground(GameStyle.COLOR_WHITE);
        passwordField.setCaretColor(GameStyle.COLOR_WHITE);
        passwordField.setFont(GameStyle.FONT_REGULAR);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameStyle.COLOR_DIRT_BROWN, 2),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        bodyPanel.add(passwordField, gbc);

        // Status Label for instructions
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel statusLabel = GameStyle.createMinecraftLabel("Log in or create a new Defender account!", GameStyle.FONT_REGULAR, GameStyle.COLOR_GOLD);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        bodyPanel.add(statusLabel, gbc);

        // 3. Actions Panel (Buttons)
        JPanel footerPanel = GameStyle.createMinecraftPanel(GameStyle.COLOR_DARK_GRAY);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton loginButton = GameStyle.createMinecraftButton("⚔ Login", GameStyle.COLOR_GRASS_GREEN, GameStyle.COLOR_LIGHT_GREEN);
        JButton registerButton = GameStyle.createMinecraftButton("⚒ Register", GameStyle.COLOR_STONE_GRAY, GameStyle.COLOR_LIGHT_GRAY);
        JButton exitButton = GameStyle.createMinecraftButton("❌ Exit World", GameStyle.COLOR_DIRT_BROWN, GameStyle.COLOR_DIRT_BROWN.brighter());

        footerPanel.add(loginButton, gbc);
        footerPanel.add(registerButton, gbc);
        footerPanel.add(exitButton, gbc);

        // STUDENT TASK 5: LoginFrame
        // COMPLETED: Complete login button event handling.
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegistration());
        exitButton.addActionListener(e -> confirmExit());

        // Assemble Frame
        frame.setLayout(new BorderLayout());
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(bodyPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Attempts to log in using fields and databases.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both Username and Password!", "Entry Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Player player = playerService.login(username, password);
            if (player != null) {
                JOptionPane.showMessageDialog(frame, "Welcome back, Defender!", "Access Granted", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                MainMenuFrame mainMenu = new MainMenuFrame(player);
                mainMenu.show();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Try again or Register!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,
                "Database Connection Error:\n" + e.getMessage() + "\n\nPlease ensure MySQL is running on port 3306 and settings are correct.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Handles account creation.
     */
    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both Username and Password for Registration!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.length() < 3) {
            JOptionPane.showMessageDialog(frame, "Username must be at least 3 characters long!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success = playerService.register(username, password);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Account created successfully!\nYou can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            // MySQL error code 1062 is standard for "Duplicate entry" for unique keys
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(frame, "Username already exists. Please choose a different name.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Database Connection Error:\n" + e.getMessage() + "\n\nPlease ensure MySQL is running on port 3306 and settings are correct.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Prompts standard JOption dialog to confirm exit.
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
