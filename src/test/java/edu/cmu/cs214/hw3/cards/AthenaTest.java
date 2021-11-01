package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class AthenaTest {
    private God Athena;
    private God Muggle;
    private Game game;

    @Before
    public void init() throws Exception {
        game = new Game();
        game.initGame("freya", "yoyo");
        Athena = new Athena();
        Muggle = new Muggle();
        game.getCurrentPlayer().setGod(Athena);
        game.getOpponentPlayer().setGod(Muggle);
        game.pickStartingPosition(new int[]{1, 2});
        game.pickStartingPosition(new int[]{2, 3});
        game.pickStartingPosition(new int[]{3, 2});
        game.pickStartingPosition(new int[]{0, 3});
        game.chooseWorker(new int[] {2, 3});
    }

    @Test
    public void testDoMoveWithoutPower() {
        Cell curPos = game.getBoard().getCell(2, 3);
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(curPos);

        Cell moveTo = game.getBoard().getCell(2, 4);
        Athena.doMove(worker, moveTo, game);

        assertEquals(worker.getCurPosition(), moveTo);
    }

    @Test
    public void testDoMoveWithPower() {
        Cell curPos = game.getBoard().getCell(2, 3);
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(curPos);

        Cell moveTo = game.getBoard().getCell(3, 3);
        moveTo.addLevel();
        Athena.doMove(worker, moveTo, game);

        assertEquals(worker.getCurPosition(), moveTo);
        assertTrue(Athena.ifUsePowerToOpponent());
    }

    @Test
    public void testApplyAthenaPower() {
        Cell curPos = game.getBoard().getCell(2, 3);
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(curPos);

        Cell moveTo = game.getBoard().getCell(3, 3);
        moveTo.addLevel();
        Athena.doMove(worker, moveTo, game);
        game.takeTurns();
        game.chooseWorker(new int[]{3, 2});
        List<Cell> possibleCells = game.computeMovableCells();

        assertEquals(worker.getCurPosition(), moveTo);
        assertTrue(Athena.ifUsePowerToOpponent());
        assertFalse(possibleCells.contains(moveTo));
    }
}
