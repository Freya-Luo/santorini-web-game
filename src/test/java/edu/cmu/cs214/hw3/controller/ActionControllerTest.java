package edu.cmu.cs214.hw3.controller;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Player;
import edu.cmu.cs214.hw3.models.Worker;
import edu.cmu.cs214.hw3.utils.WorkerType;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ActionControllerTest {
    private Board board = new Board();
    private ActionController controller = new ActionController(board);
    private Worker worker;

    @Before
    public void init() {
        worker = new Worker(WorkerType.TYPE_A, new Player("freya"));
        worker.setCurPosition(board.getCell(2, 3));

        board.getCell(2, 2).setOccupied();

        for(int i = 0; i < 2; i++) {
            board.getCell(1, 2).getTower().addLevel();
        }
        
        for(int i = 0; i < 2; i++) {
            board.getCell(3, 2).getTower().addLevel();
        }

        for(int i = 0; i < 3; i++) {
            board.getCell(1, 3).getTower().addLevel();
        }

        for(int i = 0; i < 4; i++) {
            board.getCell(2, 4).getTower().addLevel();
        }
    }

    @Test
    public void testChooseMoveWithUnoccupied() {
        boolean success = controller.chooseMove(worker, board.getCell(3, 3));

        assertTrue(success);
    }

    @Test
    public void testChooseMoveWithOccupied() {
        boolean success = controller.chooseMove(worker, board.getCell(2, 2));

        assertFalse(success);
    }

    @Test
    public void testChooseMoveWithClimbable() {
        boolean success = controller.chooseMove(worker, board.getCell(3, 3));

        assertTrue(success);
    }

    @Test
    public void testChooseMoveWithUnclimbable() {
        boolean success = controller.chooseMove(worker, board.getCell(3, 2));

        assertFalse(success);
    }
    

    @Test
    public void testChooseMoveWithCompletedTower() {
        boolean success = controller.chooseMove(worker, board.getCell(2, 4));

        assertFalse(success);
    }

    @Test
    public void testChooseBuildWithUnoccupied() {
        boolean success = controller.chooseBuild(worker, board.getCell(1, 2));

        assertTrue(success);
    }

    @Test
    public void testChooseBuildWithOccupied() {
        boolean success = controller.chooseBuild(worker, board.getCell(2, 2));

        assertFalse(success);
    }

    @Test
    public void testChooseBuildWithUncompletedTower() {
        boolean success = controller.chooseBuild(worker, board.getCell(1, 3));

        assertTrue(success);
    }

    @Test
    public void testChooseBuildWithCompletedTower() {
        boolean success = controller.chooseBuild(worker, board.getCell(2, 4));

        assertFalse(success);
    }
}
