package controller;

import model.Board;

/**
 * This class represents the game controller for Tic Tac Toe. It is the main
 * component of the controller in the MVC pattern.
 * 
 * Although it could be argued that a game as simple as Tic Tac Toe does not
 * need a controller, the controller serves to decouple the model and view. This
 * in turn leads to less smelly code, and an overall more cohesive design.
 * 
 * @author John Breton
 * @version December 15th, 2019
 */
public class GameController {

    private final Board board;

    /**
     * Construct a new GameController.
     * 
     * @param board The Board this GameController will be broadcasting to.
     */
    public GameController(Board board) {
        this.board = board;
    }

    /**
     * Register when a grid button has been clicked. Passes the information along to
     * the Board to make a move at the passed coordinates.
     * 
     * @param x The x coordinate of the button that has been clicked.
     * @param y The y coordinate of the button that has been clicked.
     */
    public void registerClick(int x, int y) {
        board.makeMove(x, y);
    }
}
