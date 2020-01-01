package model;

/**
 * This interface represents the behaviour that a board listener should have.
 *
 * @author John Breton
 * @version December 15th, 2019
 */
@FunctionalInterface
public interface BoardListener {
	// Called when the Board changes. Updates GameView accordingly.
    void handleBoardChange();
}