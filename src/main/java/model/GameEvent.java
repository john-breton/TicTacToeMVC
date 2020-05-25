package model;

import java.util.EventObject;

/**
 * 
 * @author John Breton
 * @version December 15th, 2019
 */
public class GameEvent extends EventObject {
    
    /**
     * An enumeration representing the possible states of the game.
     */
    public enum Status {
        WINNER, DRAW, NO_WINNER
    }
    
    private final Status status;
    private final boolean turn;
    
    /**
     * Construct a new GameEvent.
     * 
     * @param source The source of the event (should be Board)
     * @param status The status of the event
     * @param turn The turn of the event (true for X, false for O).
     */
    public GameEvent(Object source, Status status, boolean turn) {
        super(source);
        if (status != null) {
            this.status = status;
        } else {
            this.status = Status.NO_WINNER;
        }
        this.turn = turn;
    }
    
    /**
     * Get the status of the event.
     * 
     * @return The Status of the event.
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * Get the turn of the event.
     * 
     * @return The current player's turn (true for X, false for O)
     */
    public boolean getTurn() {
        return turn;
    }
}
