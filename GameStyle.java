package com.minecraft.mobbattle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * GameStyle Class
 * Serves as the visual design system for the Minecraft Mob Battle application.
 * Centralizes color palettes, fonts, borders, and UI builders to keep styling
 * consistent, premium, and clean across all screens.
 * 
 * No inheritance used. Just static factory methods and helpers.
 */
public class GameStyle {

    // Minecraft Palette
    public static final Color COLOR_GRASS_GREEN = new Color(74, 114, 39);      // #4A7227
    public static final Color COLOR_LIGHT_GREEN = new Color(92, 143, 49);      // Hover green
    public static final Color COLOR_DIRT_BROWN  = new Color(92, 58, 33);       // #5C3A21
    public static final Color COLOR_STONE_GRAY  = new Color(74, 74, 74);       // #4A4A4A
    public static final Color COLOR_LIGHT_GRAY  = new Color(110, 110, 110);    // Hover gray
    public static final Color COLOR_DARK_GRAY   = new Color(38, 38, 38);       // Dark borders/panels
    public static final Color COLOR_GOLD        = new Color(253, 184, 19);     // Title Gold
    public static final Color COLOR_WHITE       = Color.WHITE;
    public static final Color COLOR_CREEPER_BG  = new Color(30, 120, 30);      // Dark green for creeper
    public static final Color COLOR_ZOMBIE_BG   = new Color(60, 80, 70);       // Dark gray-green for zombie

    // Fonts
    public static final Font FONT_TITLE = new Font("Monospaced", Font.BOLD, 26);
    public static final Font FONT_HEADER = new Font("Monospaced", Font.BOLD, 20);
    public static final Font FONT_BUTTON = new Font("Monospaced", Font.BOLD, 15);
    public static final Font FONT_REGULAR = new Font("Monospaced", Font.PLAIN, 14);
    public static final Font FONT_LABEL = new Font("Monospaced", Font.BOLD, 14);

    /**
     * Creates a blocky, double-lined Minecraft-themed border.
     */
    public static Border createBlockBorder() {
        Border outer = BorderFactory.createLineBorder(COLOR_DARK_GRAY, 3);
        Border inner = BorderFactory.createEmptyBorder(6, 12, 6, 12);
        return BorderFactory.createCompoundBorder(outer, inner);
    }

    /**
     * Creates a panel styled like a block/stone texture background.
     */
    public static JPanel createMinecraftPanel(Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_DARK_GRAY, 4));
        return panel;
    }

    /**
     * Custom-styled label.
     */
    public static JLabel createMinecraftLabel(String text, Font font, Color textColor) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(textColor);
        return label;
    }

    /**
     * Custom-styled text field with a blocky border.
     */
    public static JTextField createMinecraftTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setBackground(COLOR_DARK_GRAY);
        textField.setForeground(COLOR_WHITE);
        textField.setCaretColor(COLOR_WHITE);
        textField.setFont(FONT_REGULAR);
        
        Border lineBorder = BorderFactory.createLineBorder(COLOR_DIRT_BROWN, 2);
        Border marginBorder = BorderFactory.createEmptyBorder(4, 8, 4, 8);
        textField.setBorder(BorderFactory.createCompoundBorder(lineBorder, marginBorder));
        
        return textField;
    }

    /**
     * Creates a premium, highly responsive Minecraft-themed button.
     * Features hover state micro-animations (color changes and cursor modifications).
     */
    public static JButton createMinecraftButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // Remove default button painting to render custom background
                if (getModel().isArmed()) {
                    g.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g.setColor(hoverColor);
                } else {
                    g.setColor(baseColor);
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(COLOR_WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(createBlockBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));

        // Add micro-animation effect for hover border changes
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_GOLD, 3),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(createBlockBorder());
            }
        });

        return button;
    }
}
