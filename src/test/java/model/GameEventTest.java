package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author John Breton
 * @version December 15th, 2019
 */
public class GameEventTest {
    GameEvent testEvent, nullEvent;
    
    @Before
    public void setup() {
        testEvent = new GameEvent(this, GameEvent.Status.WINNER, true);
        nullEvent = new GameEvent(this, null, false);
    }
    
    @Test
    public void testGetStatus() {
        assertEquals(testEvent.getStatus(), GameEvent.Status.WINNER);
        // We default to NO_WINNER if the status passed is null
        assertEquals(nullEvent.getStatus(), GameEvent.Status.NO_WINNER);
    }

    @Test
    public void testGetTurn() {
        assertTrue(testEvent.getTurn());
        assertFalse(nullEvent.getTurn());
    }
}
