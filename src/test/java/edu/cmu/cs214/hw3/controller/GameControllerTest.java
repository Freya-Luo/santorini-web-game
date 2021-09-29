package edu.cmu.cs214.hw3.controller;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.utils.Phase;
import edu.cmu.cs214.hw3.utils.WorkerType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class GameControllerTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private GameController controller;
    private ActionController actionController;
    private Game game;

    @Before
    public void init() {
        System.setOut(new PrintStream(output));

        game = new Game();
        controller = new GameController(game);
        actionController = new ActionController(game.getBoard());
    }
    @After
    public void restore() {
        System.setOut(originalOut);
    }

    @Test
    public void testInitGameWithTwoPlayers() {
        boolean success = controller.initGame("freya", "yoyo");
        String[] names = new String[]{game.getPlayers()[0].getName(), game.getPlayers()[1].getName()};

        assertTrue(success);
        assertArrayEquals(names, new String[]{"freya", "yoyo"});
        assertEquals("freya", game.getCurrentPlayer().getName());
    }

    @Test
    public void testInitGameWithFewerPlayers() {
        boolean success = controller.initGame("freya", null);

        assertFalse(success);
        assertThat(output.toString(), is("Sorry, game needs at least 2 players to start.\r\n"));
    }

    @Test
    public void testPickStartingPositionWithUnoccupied() {
        controller.initGame("freya", "yoyo");

        boolean success = controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{2, 1});

        assertTrue(success);
    }

    @Test
    public void testPickStartingPositionWithOccupied() {
        controller.initGame("freya", "yoyo");
        game.getBoard().getCell(1, 2).setOccupied();

        boolean success = controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{1, 2});

        assertFalse(success);
        assertThat(output.toString(), is("Sorry, worker cannot stands on an occupied space.\r\n"));
    }

    @Test
    public void testReadyGoWithWorkersAllSetup() {
        controller.initGame("freya", "yoyo");
        controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{3, 1});
        controller.pickStartingPosition("freya", WorkerType.TYPE_B, new int[]{2, 2});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_A, new int[]{1, 0});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_B, new int[]{3, 3});
        boolean isReady = controller.readyGo();

        assertTrue(isReady);
    }

    @Test
    public void testReadyGoWithNotAllWorkersSetup() {
        controller.initGame("freya", "yoyo");
        controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{3, 1});
        controller.pickStartingPosition("freya", WorkerType.TYPE_B, new int[]{2, 2});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_B, new int[]{3, 3});

        boolean isReady = controller.readyGo();

        assertFalse(isReady);
        assertThat(output.toString(), is("Each player has to pick up starting positions for 2 workers.\r\n"));
    }

    @Test
    public void testHitRoundWithInValidMove() {
        Board board = game.getBoard();
        controller.initGame("freya", "yoyo");
        controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{3, 1});
        controller.pickStartingPosition("freya", WorkerType.TYPE_B, new int[]{2, 2});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_A, new int[]{1, 0});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_B, new int[]{3, 3});
        controller.readyGo();
        board.getCell(4, 1).setOccupied();

        boolean success = controller.hitRound(WorkerType.TYPE_A, new int[]{4, 1}, new int[]{3, 2}, actionController);
        assertFalse(success);
        assertThat(output.toString(), is("Oops! You (freya) cannot move to this cell [4, 1].\r\n"));
    }

    @Test
    public void testHitRoundWithInValidBuild() {
        Board board = game.getBoard();
        controller.initGame("freya", "yoyo");
        controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{3, 1});
        controller.pickStartingPosition("freya", WorkerType.TYPE_B, new int[]{2, 2});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_A, new int[]{1, 0});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_B, new int[]{3, 3});
        controller.readyGo();
        for(int i = 0; i < 4; i++) {
            board.getCell(4, 0).getTower().addLevel();
        }

        boolean success = controller.hitRound(WorkerType.TYPE_A, new int[]{4, 1}, new int[]{4, 0}, actionController);

        assertFalse(success);
        assertThat(output.toString(), is("Sorry! You (freya) cannot build on this cell [4, 0].\r\n"));
    }

    @Test
    public void testHitRoundWithValidActions() {
        Board board = game.getBoard();
        controller.initGame("freya", "yoyo");
        controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{3, 1});
        controller.pickStartingPosition("freya", WorkerType.TYPE_B, new int[]{2, 2});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_A, new int[]{1, 0});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_B, new int[]{3, 3});
        controller.readyGo();
        board.getCell(3, 1).setOccupied();
        for(int i = 0; i < 4; i++) {
            board.getCell(4, 0).getTower().addLevel();
        }

        boolean success = controller.hitRound(WorkerType.TYPE_A, new int[]{4, 1}, new int[]{4, 2}, actionController);

        assertFalse(success);
        assertThat(output.toString(), is(""));
    }

    @Test
    public void testHitRoundWithWinner() {
        Board board = game.getBoard();
        controller.initGame("freya", "yoyo");
        controller.pickStartingPosition("freya", WorkerType.TYPE_A, new int[]{3, 1});
        controller.pickStartingPosition("freya", WorkerType.TYPE_B, new int[]{2, 2});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_A, new int[]{1, 0});
        controller.pickStartingPosition("yoyo", WorkerType.TYPE_B, new int[]{3, 3});
        controller.readyGo();
        game.getPlayerByName("freya").setIsWinner();

        boolean success = controller.hitRound(WorkerType.TYPE_A, new int[]{4, 1}, new int[]{4, 2}, actionController);

        assertTrue(success);
        assertThat(output.toString(), is("Congratulation! freya is the winner!\r\n"));
    }
}
