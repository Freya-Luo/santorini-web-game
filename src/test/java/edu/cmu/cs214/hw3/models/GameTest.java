package edu.cmu.cs214.hw3.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;
    private Player playerA;
    private Player playerB;

    @Before
    public void init() {
        game = new Game();
        playerA = new Player("yoyo");
        playerB = new Player("freya");
    }

    @Test
    public void testSetValidPlayers() {
        boolean canSetPlayers = game.setPlayers(playerB, playerA);

        assertTrue(canSetPlayers);
        assertEquals(game.getPlayers()[0], playerB);
        assertEquals(game.getPlayers()[1], playerA);
    }

    @Test
    public void testSetWithTwoInvalidPlayers() {
        boolean canSetPlayers = game.setPlayers(null, null);
        assertFalse(canSetPlayers);
    }

    @Test
    public void testSetWithOneInvalidPlayer() {
        boolean canSetPlayers = game.setPlayers(playerB, null);
        assertFalse(canSetPlayers);
    }

    @Test
    public void testGetPlayerByName() {
        game.setPlayers(playerB, playerA);
        Player player1 = game.getPlayerByName("freya");
        Player player2 = game.getPlayerByName("yoya");

        assertEquals(playerB, player1);
        assertNull(player2);
    }

    @Test
    public void testTakeTurn() {
        game.setPlayers(playerB, playerA);
        game.setCurrentPlayer(playerA);

        game.takeTurns();
        assertEquals(playerB, game.getCurrentPlayer());

        game.takeTurns();
        assertEquals(playerA, game.getCurrentPlayer());
    }

    @Test
    public void testTakeTurnWithoutCurrentPlayer() {
        game.setPlayers(playerB, playerA);

        game.takeTurns();
        assertNull(game.getCurrentPlayer());
    }

    @Test
    public void testHasWinner() {
        game.setPlayers(playerB, playerA);
        assertFalse(game.hasWinner());

        playerB.setIsWinner();
        assertTrue(game.hasWinner());
    }
}
