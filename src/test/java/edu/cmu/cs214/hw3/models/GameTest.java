package edu.cmu.cs214.hw3.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;

    @Before
    public void init() {
        game = new Game();
    }

//    @Test
//    public void testGetPlayerByName() {
//        game.setPlayers(playerB, playerA);
//        Player player1 = game.getPlayerByName("freya");
//        Player player2 = game.getPlayerByName("yoya");
//
//        assertEquals(playerB, player1);
//        assertNull(player2);
//    }


    @Test
    public void testTakeTurnWithoutCurrentPlayer() {
        game.takeTurns();
        assertNull(game.getCurrentPlayer());
    }

    @Test
    public void testHasWinner() {
        assertFalse(game.hasWinner());
        Player playerB = game.getPlayerByName("freya");

        playerB.setIsWinner();
        assertTrue(game.hasWinner());
    }
}
