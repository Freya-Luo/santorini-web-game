package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.controller.GameController;
import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Player;
import edu.cmu.cs214.hw3.models.Worker;
import edu.cmu.cs214.hw3.utils.Action;
import edu.cmu.cs214.hw3.utils.MockGameLoader;
import edu.cmu.cs214.hw3.utils.Phase;
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
    public void init() {
        System.setOut(new PrintStream(output));

        game = new Game();
        board = game.getBoard();
        gameController = new GameController(game);
    }

    @After
    public void restore() {
        System.setOut(originalOut);
    }

    // Test if game can successfully start with fewer players
    @Test
    public void testGameSetupWithFewerPlayer() {
        MockGameLoader wrongSetupLoader = new MockGameLoader(new File("mockSantorini/mockFewerPlayerSetupTest.csv"));
        String[] names = wrongSetupLoader.loadMockPlayerNamesFromFile();

        boolean canInit = gameController.initGame(names[0], names[1]);

        assertFalse(canInit);
        assertSame(game.getPhase(), Phase.PREPARING);
        assertThat(output.toString(), is("Sorry, game needs at least 2 players to start.\r\n"));
    }

    // Test if game can successfully start with conflicted workers starting positions
    @Test
    public void testGameSetupWithWorkerConflict() throws IOException {
        MockGameLoader wrongSetupLoader = new MockGameLoader(new File("mockSantorini/mockWorkerConflictSetupTest.csv"));
        List<Action> wrongSetup = wrongSetupLoader.loadMockSetupFromFile();
        String[] names = wrongSetupLoader.loadMockPlayerNamesFromFile();

        boolean canInit = gameController.initGame(names[0], names[1]);
        boolean canPickPositions = true;
        for(Action setup: wrongSetup) {
            canPickPositions = gameController.pickStartingPositions(setup.getName(), setup.getType(), setup.getStartPos());
            if(!canPickPositions) break;
        }

        assertTrue(canInit);
        assertFalse(canPickPositions);
        assertSame(game.getPhase(), Phase.PREPARING);
        assertThat(output.toString(), is("Sorry, worker cannot stands on an occupied space.\r\n"));
    }

    // Test if game can successfully start with fewer workers
    @Test
    public void testGameSetupWithFewerWorker() throws IOException {
        MockGameLoader wrongSetupLoader = new MockGameLoader(new File("mockSantorini/mockFewerWorkerSetupTest.csv"));
        List<Action> wrongSetup = wrongSetupLoader.loadMockSetupFromFile();
        String[] names = wrongSetupLoader.loadMockPlayerNamesFromFile();

        boolean canInit = gameController.initGame(names[0], names[1]);
        boolean canPickPositions = true;
        for(Action setup: wrongSetup) {
            canPickPositions = gameController.pickStartingPositions(setup.getName(), setup.getType(), setup.getStartPos());
        }
        boolean isReady = gameController.readyGo();

        assertTrue(canInit);
        assertTrue(canPickPositions);
        assertFalse(isReady);
        assertSame(game.getPhase(), Phase.PREPARING);
        assertThat(output.toString(), is("Each player has to pick up starting positions for 2 workers.\r\n"));
    }

    // Test if game can successfully start normal setup actions
    @Test
    public void testGameWithCorrectSetup() throws IOException{
        MockGameLoader correctSetupLoader = new MockGameLoader(new File("mockSantorini/mockCorrectSetupTest.csv"));
        List<Action> correctSetup = correctSetupLoader.loadMockSetupFromFile();
        String[] names = correctSetupLoader.loadMockPlayerNamesFromFile();

        boolean canInit = gameController.initGame(names[0], names[1]);
        boolean canPickPositions = false;
        for(Action setup: correctSetup) {
            canPickPositions = gameController.pickStartingPositions(setup.getName(), setup.getType(), setup.getStartPos());
            if(!canPickPositions) break;
        }
        boolean isReady = gameController.readyGo();

        Player[] players = game.getPlayers();
        Player pMax = game.getPlayerByName("Max");
        Player pAxel = game.getPlayerByName("Axel");

        Worker wMaxA = pMax.getWorkerByType(WorkerType.TYPE_A);
        Worker wMaxB = pMax.getWorkerByType(WorkerType.TYPE_B);
        Worker wAxelA = pAxel.getWorkerByType(WorkerType.TYPE_A);
        Worker wAxelB = pAxel.getWorkerByType(WorkerType.TYPE_B);

        assertTrue(canInit);
        assertTrue(canPickPositions);
        assertTrue(isReady);
        assertSame(game.getPhase(), Phase.RUNNING);
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
