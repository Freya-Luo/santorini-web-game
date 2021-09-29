package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.controller.ActionController;
import edu.cmu.cs214.hw3.controller.GameController;
import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import edu.cmu.cs214.hw3.utils.Action;
import edu.cmu.cs214.hw3.utils.MockGameLoader;
import edu.cmu.cs214.hw3.utils.WorkerType;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SantoriniRoundsIT {
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PrintStream originalOut = System.out;
    private Game game;
    private Board board;

    private GameController gameController;
    private ActionController actionController;
    private MockGameLoader setupLoader = new MockGameLoader(new File("mockSantorini/mockCorrectSetupTest.csv"));
    private List<Action> mockSetup = setupLoader.loadMockSetupFromFile();
    private String[] names = setupLoader.loadMockPlayerNamesFromFile();

    public SantoriniRoundsIT() throws IOException {}

    @Before
    public void init() {
        System.setOut(new PrintStream(output, true));

        game = new Game();
        board = game.getBoard();
        gameController = new GameController(game);
        actionController = new ActionController(board);

        setupLoader = new MockGameLoader(new File("mockSantorini/mockCorrectSetupTest.csv"));
        gameController.initGame(names[0], names[1]);
        for(Action setup: mockSetup) {
            gameController.pickStartingPositions(setup.getName(), setup.getType(), setup.getStartPos());
        }
        gameController.readyGo();
    }

    @After
    public void restore() {
        System.setOut(originalOut);
    }

    // Test if game can detect invalid move action and reject it
    @Test
    public void testRunGameWithInvalidMove() throws IOException {
        MockGameLoader roundLoader = new MockGameLoader(new File("mockSantorini/mockInvalidMoveRoundsTest.csv"));
        List<Action> rounds = roundLoader.loadMockRoundsFromFile();

        boolean canFinishGame = true;
        for(Action round: rounds) {
            canFinishGame = gameController.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn(), actionController);
        }

        Worker wMaxA = game.getPlayerByName("Max").getWorkerByType(WorkerType.TYPE_A);
        Worker wAxelA = game.getPlayerByName("Axel").getWorkerByType(WorkerType.TYPE_A);
        Worker wAxelB = game.getPlayerByName("Axel").getWorkerByType(WorkerType.TYPE_B);

        assertFalse(canFinishGame);
        assertThat(wMaxA.getCurPosition().getGeogPair(), is(new int[]{0, 4}));
        assertThat(wAxelA.getCurPosition().getGeogPair(), is(new int[]{2, 4}));
        assertThat(wAxelB.getCurPosition().getGeogPair(), is(new int[]{1, 3}));
        assertEquals(2, board.getCell(1, 4).getTower().getLevel());
        assertEquals(2, board.getCell(0, 3).getTower().getLevel());
        assertThat(output.toString(), is("""
                Oops! You (Max) cannot move to this cell [0, 3].\r
                Oops! You (Max) cannot move to this cell [1, 4].\r
                Oops! You (Max) cannot move to this cell [1, 3].\r
                """));
    }

    // Test if game can detect invalid build action and reject it
    @Test
    public void testRunGameWithInvalidBuild() throws IOException {
        MockGameLoader roundLoader = new MockGameLoader(new File("mockSantorini/mockInvalidBuildRoundsTest.csv"));
        List<Action> rounds = roundLoader.loadMockRoundsFromFile();

        boolean canFinishGame = true;
        for(Action round: rounds) {
            canFinishGame = gameController.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn(), actionController);
        }

        Worker wMaxA = game.getPlayerByName("Max").getWorkerByType(WorkerType.TYPE_A);
        Worker wAxelB = game.getPlayerByName("Axel").getWorkerByType(WorkerType.TYPE_B);

        assertFalse(canFinishGame);
        assertThat(wMaxA.getCurPosition().getGeogPair(), is(new int[]{1, 3}));
        assertThat(wAxelB.getCurPosition().getGeogPair(), is(new int[]{1, 4}));
        assertEquals(1, board.getCell(1, 4).getTower().getLevel());
        assertEquals(1, board.getCell(0, 3).getTower().getLevel());
        assertEquals(2,  board.getCell(0, 4).getTower().getLevel());
        assertThat(output.toString(), is("""
                Sorry! You (Axel) cannot build on this cell [1, 3].\r
                """));
    }

    // Test if game can successfully end with a winner after rejecting all invalid actions
    @Test
    public void testBRunGameWithWinner() throws IOException {
        MockGameLoader roundLoader = new MockGameLoader(new File("mockSantorini/mockRoundsWithWinnerTest.csv"));
        List<Action> rounds = roundLoader.loadMockRoundsFromFile();

        boolean canFinishGame = true;
        for(Action round: rounds) {
            canFinishGame = gameController.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn(), actionController);
        }

        assertTrue(canFinishGame);
        assertThat(output.toString(), is("Congratulation! Axel is the winner!\r\n"));
    }
}
