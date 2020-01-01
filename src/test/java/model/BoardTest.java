package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Board class.
 * 
 * @author John Breton
 * @version December 15th, 2019
 */
public class BoardTest {
    
    private Board board;
    
    @Before
    public void setUp() {
        board = new Board();
    }
    
    @Test
    public void testBoardConstructor() {
        assertNotNull(board);
        // The Board should initially be empty
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                assertEquals(board.getCharacter(x, y), ' ');
            }
        }
        // The initial turn should be X (which is true)
        assertTrue(board.getCurrentTurn());
    }
    
    @Test
    public void testMakeMove() {
        // x at 0 0
        board.makeMove(0, 0);
        // Now it is o's turn
        assertFalse(board.getCurrentTurn());
        // o at 0 1
        board.makeMove(0, 1); 
        // Now it is x's turn
        assertTrue(board.getCurrentTurn());
        // Invalid move (can't happen in the GUI)
        board.makeMove(100, 100);
        // x did not make a valid move, so it's still their turn
        assertTrue(board.getCurrentTurn());     
        // There's already an O at 0 1, so this should not do anything
        board.makeMove(0, 1);
        assertEquals(board.getCharacter(0, 1), 'o');
        // Again, no move was made so it should still be x's turn
        assertTrue(board.getCurrentTurn());
    }
    
    @Test
    public void testResetBoard() {
        // Make a few moves
        board.makeMove(0, 0);                           
        board.makeMove(1, 1);
        board.makeMove(2, 2);
        assertNotEquals(board.getCharacter(0, 0), ' ');
        assertNotEquals(board.getCharacter(0, 0), ' ');
        assertNotEquals(board.getCharacter(2, 2), ' ');
        board.resetBoard();
        // Ensure the board is now empty
        for (int x = 0; x < Board.SIZE; x++) {          
            for (int y = 0; y < Board.SIZE; y++) {
                assertEquals(board.getCharacter(x, y), ' ');
            }
        }
    }
    
    @Test
    public void testBoardState() {
        // Put the board in a winning state
        board.makeMove(0, 0);                           
        board.makeMove(0, 1);
        board.makeMove(1, 1);
        board.makeMove(1, 2);
        board.makeMove(2, 2);
        assertEquals(board.boardState().getStatus(), GameEvent.Status.WINNER);
        // Reset the board.
        board.resetBoard();
        
        // Put the board in a draw state
        board.makeMove(0, 0);
        board.makeMove(1, 1);
        board.makeMove(2, 2);
        board.makeMove(2, 1);
        board.makeMove(0, 1);
        board.makeMove(0, 2);
        board.makeMove(2, 0);
        board.makeMove(1, 0);
        board.makeMove(1, 2);
        assertEquals(board.boardState().getStatus(), GameEvent.Status.DRAW);
        // Reset the board.
        board.resetBoard();
        assertEquals(board.boardState().getStatus(), GameEvent.Status.NO_WINNER);
    }
}
