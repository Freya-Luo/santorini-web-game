package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PanTest {
    private God Pan;
    private God Muggle;
    private Game game;

    @Before
    public void init() throws Exception {
        game = new Game();
        game.initGame("freya", "yoyo");
        Pan = new Pan();
        Muggle = new Muggle();
        game.getCurrentPlayer().setGod(Pan);
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
        curPos.addLevel();
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(curPos);

        Cell moveTo = game.getBoard().getCell(2, 4);
        Pan.doMove(worker, moveTo, game);
        game.checkWinner();

        assertEquals(worker.getCurPosition(), moveTo);
        assertNull(game.getWinner());
    }

    @Test
    public void testDoMoveWithPower() {
        Cell curPos = game.getBoard().getCell(2, 3);
        curPos.addLevel();
        curPos.addLevel();
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(curPos);

        Cell moveTo = game.getBoard().getCell(3, 3);
        Pan.doMove(worker, moveTo, game);
        game.checkWinner();

        assertEquals(worker.getCurPosition(), moveTo);
        assertEquals(game.getCurrentPlayer(), game.getWinner());
    }
}
