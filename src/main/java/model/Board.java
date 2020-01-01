package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.BoardListener;

/**
 * This class represents the game Board for Tic Tac Toe. It is the main
 * component of the model in the MVC pattern.
 * 
 * This class uses a dynamically scalable implementation to determine if it is
 * in a winning state. All that needs to be done is to change the SIZE field to
 * the specified height and length.
 * 
 * @author John Breton
 * @version December 15th, 2019
 */
public class Board implements Serializable {
    // Used for object serialization.
    private static final long serialVersionUID = 1327864657251043003L;
    // The length and width of the Tic Tac Toe game.
    public static final int SIZE = 3;
    // Stores the text representation of the game.
    private char[][] grid;
    // Determines the current player's turn (True for X, false for O).
    private boolean turn;

    private transient List<BoardListener> boardListeners;
    private int moveCount, lastX, lastY;

    /**
     * Construct a new empty Board.
     */
    public Board() {
        grid = new char[SIZE][SIZE];
        resetBoard();
        boardListeners = new ArrayList<>();
        turn = true;
    }

    /**
     * Clear the grid of any values. 
     * Empty grid positions are represented with the ' ' character.
     */
    public void resetBoard() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                grid[x][y] = ' ';
            }
        }
        turn = true;
        moveCount = 0;
    }

    /**
     * Make a move on the board, if it's valid.
     * 
     * @param x The x coordinate of the move
     * @param y The y coordinate of the move
     */
    public void makeMove(int x, int y) {
        if (x >= SIZE || y >= SIZE) {}
        else if (grid[x][y] == ' ') {
            grid[x][y] = turn ? 'x' : 'o';
            turn = !turn;
            // Used to simplify winning board calculations in boardState()
            // Does make the class less cohesive at the cost of efficiency
            lastX = x;
            lastY = y;
            moveCount++;
        }
        notifyListeners();
    }

    /**
     * Return the current state of the Board. A Board can be in a winning state, a
     * draw state, or a not won state.
     * 
     * @return A GameEvent representing the current state of the board.
     */
    public GameEvent boardState() {
//      // Check the row and column of the last move
//      if (((grid[lastX][0]) == grid[lastX][1] && grid[lastX][0] == grid[lastX][2])
//              || (grid[0][lastY] == grid[1][lastY] && grid[0][lastY] == grid[2][lastY]))
//          return new GameEvent(this, GameEvent.Status.WINNER, turn);
//
//      // Check a diagonal (if a move was just played along one)
//      if (((lastX == lastY) && (grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2])) || 
//              (lastX == SIZE - 1 - lastY) && (grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0]))
//          return new GameEvent(this, GameEvent.Status.WINNER, turn);
        boolean flag = true;
        if (moveCount > 4) {
            // Check the row of the last move
            for (int y = 1; y < SIZE; y++)
                if (grid[lastX][0] != grid[lastX][y])
                    flag = false;
            if (flag == true)
                return new GameEvent(this, GameEvent.Status.WINNER, turn);

            flag = true;
            // Check the column of the last move
            for (int x = 1; x < SIZE; x++)
                if (grid[0][lastY] != grid[x][lastY])
                    flag = false;
            if (flag == true)
                return new GameEvent(this, GameEvent.Status.WINNER, turn);

            // Check the top left to bottom right diagonal (if a move was played along it)
            if (lastX == lastY) {
                flag = true;
                for (int i = 1; i < SIZE; i++)
                    if (grid[0][0] != grid[i][i])
                        flag = false;
                if (flag == true)
                    return new GameEvent(this, GameEvent.Status.WINNER, turn);
            }

            // Check the top right to bottom left diagonal (if a move was played along it)
            if (lastX == SIZE - 1 - lastY) {
                flag = true;
                for (int i = 1; i < SIZE; i++)
                    if (grid[0][SIZE - 1] != grid[i][SIZE - 1 - i])
                        flag = false;
                if (flag == true)
                    return new GameEvent(this, GameEvent.Status.WINNER, turn);
            }
        }
        // The game is a draw if all of the tiles are filled
        if (moveCount == SIZE * SIZE)
            return new GameEvent(this, GameEvent.Status.DRAW, turn);

        return new GameEvent(this, GameEvent.Status.NO_WINNER, turn);
    }

    /**
     * Return the character at the specified position
     * 
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return The character located at the passed x and y coordinates
     */
    public char getCharacter(int x, int y) {
        return grid[x][y];
    }
    
    /**
     * Get the current player's turn
     * 
     * @return The current player's turn (true for X, false for O)
     */
    public boolean getCurrentTurn() {
        return turn;
    }

    /**
     * Add a listener to this Board.
     *
     * @param boardListener The listener to add
     */
    public void addListener(BoardListener boardListener) {
        if (boardListeners == null) {
            boardListeners = new ArrayList<>();
        }
        if (!boardListeners.contains(boardListener)) {
            boardListeners.add(boardListener);
        }
    }

    /**
     * Notify all listeners that the Board has changed.
     */
    private void notifyListeners() {
        boardListeners.forEach(BoardListener::handleBoardChange);
    }
}