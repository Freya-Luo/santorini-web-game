package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.controller.GameController;
import edu.cmu.cs214.hw3.utils.MockGameLoader;
import edu.cmu.cs214.hw3.utils.WorkerType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;


public class SantoriniSetupIT {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Game game;
    private Board board;

    private static GameController gameController;

    @Before
    public void init() throws IOException{
        System.setOut(new PrintStream(output));

        game = new Game();
        board = game.getBoard();
        gameController = new GameController(game);
    }

    @After
    public void restore() {
        System.setOut(originalOut);
    }

    @Test
    public void testGameSetupWithLessPlayer() throws IOException {
        MockGameLoader wrongSetupLoader = new MockGameLoader(new File("mockSantorini/mockLessPlayerSetupTest.csv"));
        List<Action> wrongSetup = wrongSetupLoader.loadMockSetupFromFile();

        boolean canInit = gameController.initGame(wrongSetup);
        boolean canPickPositions = gameController.pickStartingPositions(wrongSetup);

        assertFalse(canInit);
        assertFalse(canPickPositions);
        assertThat(output.toString(), is("Sorry, game needs at least 2 players to start.\r\n" +
                "Each player has to pick up starting positions for 2 workers.\r\n"));
    }

    @Test
    public void testGameSetupWithLessWorker() throws IOException {
        MockGameLoader wrongSetupLoader = new MockGameLoader(new File("mockSantorini/mockLessWorkerSetupTest.csv"));
        List<Action> wrongSetup = wrongSetupLoader.loadMockSetupFromFile();

        boolean canInit = gameController.initGame(wrongSetup);
        boolean canPickPositions = gameController.pickStartingPositions(wrongSetup);

        assertTrue(canInit);
        assertFalse(canPickPositions);
        assertThat(output.toString(), is("Each player has to pick up starting positions for 2 workers.\r\n"));
    }

    @Test
    public void testGameSetupWithWorkerConflict() throws IOException {
        MockGameLoader wrongSetupLoader = new MockGameLoader(new File("mockSantorini/mockWorkerConflictSetupTest.csv"));
        List<Action> wrongSetup = wrongSetupLoader.loadMockSetupFromFile();

        boolean canInit = gameController.initGame(wrongSetup);
        boolean canPickPositions = gameController.pickStartingPositions(wrongSetup);

        assertTrue(canInit);
        assertFalse(canPickPositions);
        assertThat(output.toString(), is("Sorry, worker cannot stands on an occupied space.\r\n"));
    }

    @Test
    public void testGameWithCorrectSetup() throws IOException{
        MockGameLoader correctSetupLoader = new MockGameLoader(new File("mockSantorini/mockCorrectSetupTest.csv"));
        List<Action> correctSetup = correctSetupLoader.loadMockSetupFromFile();

        boolean canInit = gameController.initGame(correctSetup);
        boolean canPickPositions = gameController.pickStartingPositions(correctSetup);

        Player[] players = game.getPlayers();
        Player pMax = game.getPlayerByName("Max");
        Player pAxel = game.getPlayerByName("Axel");

        Worker wMaxA = pMax.getWorkerByType(WorkerType.TYPE_A);
        Worker wMaxB = pMax.getWorkerByType(WorkerType.TYPE_B);
        Worker wAxelA = pAxel.getWorkerByType(WorkerType.TYPE_A);
        Worker wAxelB = pAxel.getWorkerByType(WorkerType.TYPE_B);

        assertTrue(canInit);
        assertTrue(canPickPositions);
        assertEquals(2, players.length);
        assertThat(players, arrayContainingInAnyOrder(equalTo(pMax), equalTo(pAxel)));
        assertThat(wMaxA.getCurPosition().getGeogPair(), is(new int[]{2, 3}));
        assertThat(wMaxB.getCurPosition().getGeogPair(), is(new int[]{3, 1}));
        assertThat(wAxelA.getCurPosition().getGeogPair(), is(new int[]{3, 3}));
        assertThat(wAxelB.getCurPosition().getGeogPair(), is(new int[]{0, 2}));

        assertTrue(board.getCell(2, 3).isOccupied());
        assertTrue(board.getCell(3, 1).isOccupied());
        assertTrue(board.getCell(3, 3).isOccupied());
        assertTrue(board.getCell(0, 2).isOccupied());

        assertEquals(game.getCurrentPlayer(), pAxel);
    }
}
