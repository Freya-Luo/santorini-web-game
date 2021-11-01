package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DemeterTest {
    private God Demeter;
    private God Muggle;
    private Game game;

    @Before
    public void init() throws Exception {
        game = new Game();
        game.initGame("freya", "yoyo");
        Demeter = new Demeter();
        Muggle = new Muggle();
        game.getCurrentPlayer().setGod(Demeter);
        game.getOpponentPlayer().setGod(Muggle);
        game.pickStartingPosition(new int[]{1, 2});
        game.pickStartingPosition(new int[]{2, 3});
        game.pickStartingPosition(new int[]{3, 2});
        game.pickStartingPosition(new int[]{0, 3});
        game.chooseWorker(new int[] {2, 3});
    }

    @Test
    public void testGetBuildableCells() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell buildOn = game.getBoard().getCell(2, 4);
        Demeter.doBuild(buildOn);
        List<Cell> possibleCells = Demeter.getBuildableCells(worker, game);

        assertEquals(buildOn.getHeight(), 1);
        assertFalse(possibleCells.contains(buildOn));
    }

    @Test
    public void testDoBuildOnce() {
        Cell buildOn = game.getBoard().getCell(2, 4);
        Demeter.doBuild(buildOn);
        assertEquals(buildOn.getHeight(), 1);
    }

    @Test
    public void testDoBuildTwice() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));
        Cell buildOn = game.getBoard().getCell(2, 4);
        Demeter.doBuild(buildOn);
        Demeter.canAdditionalBuild();
        Demeter.setAns("Yes");
//        Demeter.doBuild(buildOn);

        List<Cell> possibleCells = Demeter.getBuildableCells(worker, game);

        assertEquals(buildOn.getHeight(), 1);
        assertFalse(possibleCells.contains(buildOn));
    }

    @Test
    public void testDoBuildInNextRound() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));
        Cell buildOn = game.getBoard().getCell(2, 4);
        Demeter.doBuild(buildOn);
        Demeter.canAdditionalBuild();
        Demeter.setAns("No");
        Demeter.doBuild(game.getBoard().getCell(3, 3));

        List<Cell> possibleCells = Demeter.getBuildableCells(worker, game);

        assertEquals(buildOn.getHeight(), 1);
        assertTrue(possibleCells.contains(buildOn));
    }

}
