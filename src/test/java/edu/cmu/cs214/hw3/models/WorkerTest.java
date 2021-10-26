//package edu.cmu.cs214.hw3.models;
//import edu.cmu.cs214.hw3.utils.WorkerType;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.experimental.runners.Enclosed;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.containsInAnyOrder;
//import static org.hamcrest.Matchers.equalTo;
//import static org.junit.Assert.*;
//
//@RunWith(Enclosed.class)
//public class WorkerTest {
//
//    public static class WorkerStatusTest {
//        private Player player = new Player("Freya");
//        private Worker worker;
//        private Cell target1;
//        private Cell target2;
//
//        @Before
//        public void init() {
//            worker = new Worker(WorkerType.TYPE_A, player);
//            target1 = new Cell(2, 1);
//            target2 = new Cell(4, 0);
//
//            worker.setCurPosition(target1);
//            worker.setCurPosition(target2);
//        }
//
//        @Test
//        public void testSetCurPosition() {
//            assertTrue(worker.getCurPosition().isEqual(target2));
//            assertTrue(worker.getCurPosition().isOccupied());
//        }
//
//
//        @Test
//        public void testCheckIsWinOnLevel3() {
//            for(int i = 0; i < 3; i++) {
//                worker.getCurPosition().addLevel();
//            }
//
//            worker.checkIfWin();
//            assertTrue(worker.getPlayer().isWinner());
//        }
//
//        @Test
//        public void testCheckIsWinBelowLevel3() {
//            for(int i = 0; i < 2; i++) {
//                worker.getCurPosition().addLevel();
//            }
//
//            worker.checkIfWin();
//            assertFalse(worker.getPlayer().isWinner());
//        }
//
//        @Test
//        public void testCheckIsWinWithCompletedTower() {
//            for(int i = 0; i < 4; i++) {
//                worker.getCurPosition().addLevel();
//            }
//
//            worker.checkIfWin();
//            assertFalse(worker.getPlayer().isWinner());
//        }
//    }
//
//    public static class MovableAndBuildableCellsTest{
//        private Player player = new Player("Freya");
//        private Worker worker;
//        private List<Cell> neighbors;
//
//        @Before
//        public void init() {
//            worker = new Worker(WorkerType.TYPE_A, player);
//            Cell target1 = new Cell(2, 4);
//            worker.setCurPosition(target1);
//
//            Board board = new Board();
//            neighbors = board.getNeighbors(target1);
//
//            // Set an occupied cell - cannot move to and build on
//            board.getCell(3, 3).setOccupied();
//
//            // Set a level 1 tower - can move up to
//            board.getCell(1, 3).addLevel();
//
//            // Set a level 2 tower - cannot move up to
//            for(int i = 0; i < 2; i++) {
//                board.getCell(1, 4).addLevel();
//            }
//
//            // Set a level 3 tower - can set dome
//            for(int i = 0; i < 3; i++) {
//                board.getCell(2, 3).addLevel();
//            }
//            // Set Dome - a complete tower, cannot build on
//            for(int i = 0; i < 4; i++) {
//                board.getCell(3, 4).addLevel();
//            }
//        }
//    }
//}
