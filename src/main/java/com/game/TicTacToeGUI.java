package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class TicTacToeGUI extends JFrame {
    private static final int CELL_SIZE = 100;
    private static final Color HOVER_COLOR = new Color(230, 230, 250);
    private static final Color WIN_COLOR = new Color(144, 238, 144);
    private static final Color DRAW_COLOR = new Color(255, 218, 185);
    
    private GameLogic gameLogic;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JPanel gamePanel;
    private JPanel controlPanel;
    
    public TicTacToeGUI() {
        gameLogic = new GameLogic();
        setupUI();
        setNames();
    }
    
    private void setupUI() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Create main components
        createGamePanel();
        createControlPanel();
        
        // Add components to frame
        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // Set minimum size and make responsive
        setMinimumSize(new Dimension(400, 500));
        pack();
        setLocationRelativeTo(null);
        
        // Add window resize listener
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int size = Math.min(gamePanel.getWidth() / 3, gamePanel.getHeight() / 3);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        buttons[i][j].setPreferredSize(new Dimension(size, size));
                    }
                }
                gamePanel.revalidate();
            }
        });
    }
    
    private void createGamePanel() {
        gamePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanel.setBackground(new Color(70, 70, 70));
        
        buttons = new JButton[3][3];
        Font buttonFont = new Font("Arial", Font.BOLD, 40);
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(buttonFont);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                
                final int row = i;
                final int col = j;
                
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (buttons[row][col].getText().isEmpty() && !gameLogic.isGameEnded()) {
                            buttons[row][col].setBackground(HOVER_COLOR);
                        }
                    }
                    
                    public void mouseExited(MouseEvent e) {
                        if (buttons[row][col].getText().isEmpty()) {
                            buttons[row][col].setBackground(Color.WHITE);
                        }
                    }
                });
                
                buttons[i][j].addActionListener(e -> handleButtonClick(row, col));
                gamePanel.add(buttons[i][j]);
            }
        }
    }
    
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Status label
        statusLabel = new JLabel("Player 1's turn (X)", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Score label
        scoreLabel = new JLabel("Score - Player 1: 0 | Player 2: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Reset button
        JButton resetButton = new JButton("New Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> resetGame());
        
        // Add components to control panel
        controlPanel.add(statusLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(scoreLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(resetButton);
    }
    
    private void handleButtonClick(int row, int col) {
        if (gameLogic.makeMove(row, col)) {
            buttons[row][col].setText(String.valueOf(gameLogic.getCurrentPlayer().getSymbol()));
            
            if (gameLogic.isGameEnded()) {
                if (gameLogic.isBoardFull() && gameLogic.getWinningCells()[0] == 0 && gameLogic.getWinningCells()[1] == 0) {
                    handleDraw();
                } else {
                    handleWin();
                }
            } else {
                updateStatus();
            }
        }
    }
    
    private void handleWin() {
        // Highlight winning cells
        int[] winningCells = gameLogic.getWinningCells();
        for (int i = 0; i < 6; i += 2) {
            buttons[winningCells[i]][winningCells[i+1]].setBackground(WIN_COLOR);
        }
        
        // Update status and score
        Player winner = gameLogic.getCurrentPlayer();
        statusLabel.setText(winner.getName() + " wins!");
        updateScore();
        
        // Show win dialog
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                winner.getName() + " wins! Play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
        });
    }
    
    private void handleDraw() {
        // Change all buttons to draw color
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setBackground(DRAW_COLOR);
            }
        }
        
        statusLabel.setText("It's a draw!");
        
        // Show draw dialog
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "It's a draw! Play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
        });
    }
    
    private void resetGame() {
        gameLogic.resetGame();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
        
        updateStatus();
    }
    
    private void updateStatus() {
        Player currentPlayer = gameLogic.getCurrentPlayer();
        statusLabel.setText(currentPlayer.getName() + "'s turn (" + currentPlayer.getSymbol() + ")");
    }
    
    private void updateScore() {
        scoreLabel.setText(String.format("Score - %s: %d | %s: %d",
            gameLogic.getPlayer1().getName(),
            gameLogic.getPlayer1().getScore(),
            gameLogic.getPlayer2().getName(),
            gameLogic.getPlayer2().getScore()
        ));
    }
    
    private void setNames() {
        SwingUtilities.invokeLater(() -> {
            String name1 = JOptionPane.showInputDialog(this, "Enter name for Player 1 (X):", "Player 1");
            String name2 = JOptionPane.showInputDialog(this, "Enter name for Player 2 (O):", "Player 2");
            
            if (name1 != null && !name1.trim().isEmpty()) {
                gameLogic.getPlayer1().setName(name1.trim());
            }
            if (name2 != null && !name2.trim().isEmpty()) {
                gameLogic.getPlayer2().setName(name2.trim());
            }
            
            updateStatus();
            updateScore();
        });
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new TicTacToeGUI().setVisible(true);
        });
    }
} 