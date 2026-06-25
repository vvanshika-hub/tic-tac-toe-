package com.minecraft.mobbattle;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main Class
 * The main entry point of the Minecraft Mob Battle application.
 * Initializes the database first to ensure the SQL backend is active,
 * configures styling elements, and launches the initial LoginFrame.
 * 
 * Complies with the Programming Fundamentals university constraints:
 * - Simple OOP principles
 * - No custom inheritance
 * - Fully commented and clean
 */
public class Main {

    /**
     * Application entry main method.
     * 
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("Starting Minecraft Mob Battle...");

        // 1. Initialize Database & Schema
        // Automatically creates the database and player table if they don't exist
        DatabaseManager.initializeDatabase();

        // 2. Set UI Look and Feel to System Default for high compatibility
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not load native system look and feel. Falling back to default Swing style.");
        }

        // 3. Launch GUI on the Swing Event Dispatch Thread (EDT)
        // This is safe practice to prevent multi-threading rendering glitches.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Initialize the Login Window and show it
                LoginFrame loginWindow = new LoginFrame();
                loginWindow.show();
                System.out.println("Login frame displayed.");
            }
        });
    }
}
