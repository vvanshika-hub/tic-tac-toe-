package com.minecraft.mobbattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameLogic Class
 * Manages the state, rules, and AI of the 3x3 battlefield.
 * Uses 0-8 coordinates representing grid cells:
 * 0 | 1 | 2
 * ---------
 * 3 | 4 | 5
 * ---------
 * 6 | 7 | 8
 * 
 * Simple OOP approach. Contains no custom inheritance or abstract designs.
 */
public class GameLogic {
    // Constants for representing symbols on the board
    public static final String CREEPER = "CREEPER";
    public static final String ZOMBIE = "ZOMBIE";

    private String[] board;
    private Random random;

    // Hardcoded win lines for evaluation
    private static final int[][] WIN_LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontal rows
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertical columns
        {0, 4, 8}, {2, 4, 6}             // Diagonals
    };

    public GameLogic() {
        board = new String[9];
        random = new Random();
        resetBoard();
    }

    /**
     * Resets the entire grid to empty (null).
     */
    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = null;
        }
    }

    /**
     * STUDENT TASK 7: GameLogic (Valid Move)
     * COMPLETED: Complete valid move validation and placement.
     * Places a symbol on a specific cell of the board.
     * 
     * @param index Cell number (0-8)
     * @param playerType Either CREEPER or ZOMBIE
     * @return true if move was placed, false if cell occupied or invalid
     */
    public boolean makeMove(int index, String playerType) {
        if (index < 0 || index > 8 || board[index] != null) {
            return false;
        }
        board[index] = playerType;
        return true;
    }

    /**
     * STUDENT TASK 7: GameLogic (Winner Checking)
     * COMPLETED: Complete winner evaluation logic on the win lines.
     * Evaluates if there is a winner.
     * 
     * @return Winner string (CREEPER or ZOMBIE), or null if no winner yet.
     */
    public String checkWinner() {
        for (int[] line : WIN_LINES) {
            if (board[line[0]] != null &&
                board[line[0]].equals(board[line[1]]) &&
                board[line[0]].equals(board[line[2]])) {
                return board[line[0]];
            }
        }
        return null;
    }

    /**
     * STUDENT TASK 7: GameLogic (Draw Checking)
     * COMPLETED: Complete draw detection check.
     * Checks if the battlefield is fully filled and there is no winner (draw).
     * 
     * @return true if draw, false otherwise.
     */
    public boolean isDraw() {
        // If there is already a winner, it's not a draw
        if (checkWinner() != null) {
            return false;
        }
        // If there is any empty cell left, it's not a draw
        for (String cell : board) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * STUDENT TASK 7: GameLogic (Computer Move)
     * COMPLETED: Complete prioritized AI move engine (Win -> Block -> Center -> Corners -> Random).
     * Computes the next move for the Zombie computer.
     * Strategy:
     * 1. Make winning move (ZOMBIE check).
     * 2. Block player's winning move (CREEPER check).
     * 3. Take center (Index 4).
     * 4. Take corner (0, 2, 6, 8).
     * 5. Take random remaining cell.
     * 
     * @return Selected board cell index (0-8), or -1 if no move possible.
     */
    public int computerMove() {
        // Priority 1: Make winning move if possible
        int winningMove = findWinningCell(ZOMBIE);
        if (winningMove != -1) {
            return winningMove;
        }

        // Priority 2: Block player's winning move
        int blockMove = findWinningCell(CREEPER);
        if (blockMove != -1) {
            return blockMove;
        }

        // Priority 3: Take center (Index 4)
        if (board[4] == null) {
            return 4;
        }

        // Priority 4: Take corner (0, 2, 6, 8)
        int[] corners = {0, 2, 6, 8};
        List<Integer> emptyCorners = new ArrayList<>();
        for (int corner : corners) {
            if (board[corner] == null) {
                emptyCorners.add(corner);
            }
        }
        if (!emptyCorners.isEmpty()) {
            return emptyCorners.get(random.nextInt(emptyCorners.size()));
        }

        // Priority 5: Random move
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null) {
                emptyCells.add(i);
            }
        }
        if (!emptyCells.isEmpty()) {
            return emptyCells.get(random.nextInt(emptyCells.size()));
        }

        return -1; // Board is full
    }

    /**
     * Helper method to search if a symbol is 1 step away from completing a win line.
     * 
     * @param playerSymbol Symbol to check for winning condition
     * @return Cell index to complete the win line, or -1 if none
     */
    private int findWinningCell(String playerSymbol) {
        for (int[] line : WIN_LINES) {
            int matchCount = 0;
            int emptyIndex = -1;

            for (int index : line) {
                if (board[index] != null && board[index].equals(playerSymbol)) {
                    matchCount++;
                } else if (board[index] == null) {
                    emptyIndex = index;
                }
            }

            // If 2 are matched and the remaining cell is empty, this is the cell we need!
            if (matchCount == 2 && emptyIndex != -1) {
                return emptyIndex;
            }
        }
        return -1;
    }

    // Getter for testing board state
    public String[] getBoard() {
        return board;
    }
}
