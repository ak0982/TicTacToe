package com.game;

public class GameLogic {
    private char[][] board;
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private boolean gameEnded;
    private int[] winningCells;

    public GameLogic() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        player1 = new Player("Player 1", 'X');
        player2 = new Player("Player 2", 'O');
        currentPlayer = player1;
        gameEnded = false;
        winningCells = new int[6]; // stores row1,col1,row2,col2,row3,col3 of winning line
    }

    public boolean makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ' || gameEnded) {
            return false;
        }

        board[row][col] = currentPlayer.getSymbol();
        
        if (checkWin(row, col)) {
            gameEnded = true;
            currentPlayer.incrementScore();
            return true;
        }

        if (isBoardFull()) {
            gameEnded = true;
            return true;
        }

        switchPlayer();
        return true;
    }

    private boolean checkWin(int lastRow, int lastCol) {
        char symbol = currentPlayer.getSymbol();
        
        // Check row
        if (board[lastRow][0] == symbol && board[lastRow][1] == symbol && board[lastRow][2] == symbol) {
            winningCells[0] = lastRow; winningCells[1] = 0;
            winningCells[2] = lastRow; winningCells[3] = 1;
            winningCells[4] = lastRow; winningCells[5] = 2;
            return true;
        }
        
        // Check column
        if (board[0][lastCol] == symbol && board[1][lastCol] == symbol && board[2][lastCol] == symbol) {
            winningCells[0] = 0; winningCells[1] = lastCol;
            winningCells[2] = 1; winningCells[3] = lastCol;
            winningCells[4] = 2; winningCells[5] = lastCol;
            return true;
        }
        
        // Check diagonals
        if (lastRow == lastCol || lastRow + lastCol == 2) {
            // Main diagonal
            if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
                winningCells[0] = 0; winningCells[1] = 0;
                winningCells[2] = 1; winningCells[3] = 1;
                winningCells[4] = 2; winningCells[5] = 2;
                return true;
            }
            // Anti-diagonal
            if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
                winningCells[0] = 0; winningCells[1] = 2;
                winningCells[2] = 1; winningCells[3] = 1;
                winningCells[4] = 2; winningCells[5] = 0;
                return true;
            }
        }
        
        return false;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = player1;
        gameEnded = false;
        winningCells = new int[6];
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public char[][] getBoard() {
        return board;
    }

    public int[] getWinningCells() {
        return winningCells;
    }
} 