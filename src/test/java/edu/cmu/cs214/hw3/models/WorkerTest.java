package edu.cmu.cs214.hw3.models;
import edu.cmu.cs214.hw3.utils.WorkerType;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class WorkerTest {

    public static class WorkerStatusTest {
        private Player player = new Player("Freya");
        private Worker worker;
        private Cell target1;
        private Cell target2;

        @Before
        public void init() {
            worker = new Worker(WorkerType.TYPE_A, player);
            target1 = new Cell(2, 1);
            target2 = new Cell(4, 0);

            worker.setCurPosition(target1);
            worker.setCurPosition(target2);
        }

        @Test
        public void testSetCurPosition() {
            assertArrayEquals(worker.getCurPosition().getGeogPair(), target2.getGeogPair());
            assertArrayEquals(worker.getPrePosition().getGeogPair(), target1.getGeogPair());
            assertFalse(worker.getPrePosition().isOccupied());
            assertTrue(worker.getCurPosition().isOccupied());
        }

        @Test
        public void testRevertToPrePosition() {
            worker.revertToPrePosition();

            assertArrayEquals(worker.getCurPosition().getGeogPair(), target1.getGeogPair());
            assertTrue(worker.getCurPosition().isOccupied());
            assertFalse(target2.isOccupied());
        }

        @Test
        public void testCheckIsWinOnLevel3() {
            for(int i = 0; i < 3; i++) {
                worker.getCurPosition().getTower().addLevel();
            }

            worker.checkIfWin();
            assertTrue(worker.getPlayer().isWinner());
        }

        @Test
        public void testCheckIsWinBelowLevel3() {
            for(int i = 0; i < 2; i++) {
                worker.getCurPosition().getTower().addLevel();
            }

            worker.checkIfWin();
            assertFalse(worker.getPlayer().isWinner());
        }
    }

    public static class MovableAndBuildableCellsTest{
        private Player player = new Player("Freya");
        private Worker worker;
        private List<Cell> neighbors;

        @Before
        public void init() {
            worker = new Worker(WorkerType.TYPE_A, player);
            Cell target1 = new Cell(2, 4);
            worker.setCurPosition(target1);

            Board board = new Board();
            neighbors = board.getNeighbors(target1);

            // Set an occupied cell - cannot move to and build on
            board.getCell(3, 3).setOccupied();

            // Set a level 1 tower - can move up to
            board.getCell(1, 3).getTower().addLevel();

            // Set a level 2 tower - cannot move up to
            for(int i = 0; i < 2; i++) {
                board.getCell(1, 4).getTower().addLevel();
            }
            // Set Dome - a complete tower, cannot build on
            for(int i = 0; i < 4; i++) {
                board.getCell(3, 4).getTower().addLevel();
            }
        }
        @Test
        public void testGetMovableCells() {
            List<Cell> movableCells = worker.getMovableCells(neighbors);
            List<int[]> movableCellsPositions = new ArrayList<>();
            for (Cell cell: movableCells) {
                movableCellsPositions.add(new int[]{cell.getX(), cell.getY()});
            }

            assertEquals(2, movableCellsPositions.size());
            assertThat(movableCellsPositions, containsInAnyOrder(equalTo(new int[]{1, 3}), equalTo(new int[]{2, 3})));
        }

        @Test
        public void testGetBuildableCells() {
            List<Cell> buildableCells = worker.getBuildableCells(neighbors);
            List<int[]> buildableCellsPositions = new ArrayList<>();
            for (Cell cell: buildableCells) {
                buildableCellsPositions.add(new int[]{cell.getX(), cell.getY()});
            }

            assertEquals(3, buildableCellsPositions.size());
            assertThat(buildableCellsPositions, containsInAnyOrder(
                    equalTo(new int[]{1, 3}),
                    equalTo(new int[]{2, 3}),
                    equalTo(new int[]{1, 4})
            ));
        }
    }
}
