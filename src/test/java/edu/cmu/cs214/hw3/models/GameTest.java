package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.cards.Athena;
import edu.cmu.cs214.hw3.cards.Demeter;
import edu.cmu.cs214.hw3.cards.God;
import edu.cmu.cs214.hw3.utils.WorkerType;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(Enclosed.class)
public class GameTest {
    public static class TestInitGameAndGods {
        private Game game;

        @Before
        public void init() {
            game = new Game();
        }

        @Test
        public void testInitGameWithValidNames() {
            game.initGame("freya", "yoyo");

            assertEquals("freya", game.getCurrentPlayer().getName().substring(1));
        }

        @Test
        public void testInitGameWithInValidNames() {
            boolean success = game.initGame("freya", null);

            assertFalse(success);
            assertThat(game.getMessage().trim(), is("Sorry, game needs at least 2 players to start."));
        }

        @Test
        public void testChooseGodWithValidGods() throws Exception {
            game.initGame("freya", "yoyo");
            game.chooseGod("Athena", "Demeter");

            God godA = game.getCurrentPlayer().getGod();
            God godB = game.getOpponentPlayer().getGod();

            assertTrue(godA instanceof Athena);
            assertTrue(godB instanceof Demeter);
        }

        @Test(expected = InstantiationException.class)
        public void testChooseGodWithInValidGods() throws Exception {
            game.initGame("freya", "yoyo");
            game.chooseGod("Athena", "God");
        }
    }

    public static class TestPickingStartingPosition {
        private Game game;
        private Worker workerFA;
        private Worker workerFB;
        private Worker workerYA;
        private Worker workerYB;

        @Before
        public void init() {
            game = new Game();
            game.initGame("freya", "yoyo");
            workerFA = game.getCurrentPlayer().getWorkerByType(WorkerType.TYPE_A);
            workerFB = game.getCurrentPlayer().getWorkerByType(WorkerType.TYPE_B);
            workerYA = game.getOpponentPlayer().getWorkerByType(WorkerType.TYPE_A);
            workerYB = game.getOpponentPlayer().getWorkerByType(WorkerType.TYPE_B);

        }

        @Test
        public void testPickingStartingPositionWithFourWorkers() {
            game.pickStartingPosition(new int[]{1, 2});
            game.pickStartingPosition(new int[]{2, 3});
            game.pickStartingPosition(new int[]{3, 2});
            game.pickStartingPosition(new int[]{0, 3});
            boolean success = game.pickStartingPosition(new int[]{4, 2});

            assertFalse(success);
            assertTrue(workerFA.getCurPosition().isEqual(new Cell(1, 2)));
            assertTrue(workerFB.getCurPosition().isEqual(new Cell(2, 3)));
            assertTrue(workerYA.getCurPosition().isEqual(new Cell(3, 2)));
            assertTrue(workerYB.getCurPosition().isEqual(new Cell(0, 3)));
            assertThat(game.getMessage().trim(), is("All workers are set. game is ready to go! Enjoy!"));
        }

        @Test
        public void testPickingStartingPositionWithOccupiedCell() {
            game.pickStartingPosition(new int[]{1, 2});
            game.pickStartingPosition(new int[]{2, 3});
            boolean success = game.pickStartingPosition(new int[]{1, 2});

            assertFalse(success);
            assertTrue(workerFA.getCurPosition().isEqual(new Cell(1, 2)));
            assertTrue(workerFB.getCurPosition().isEqual(new Cell(2, 3)));
            assertNull(workerYA.getCurPosition());
            assertThat(game.getMessage().trim(), is("Sorry, worker cannot stands on an occupied space."));
        }

        @Test
        public void testPickingStartingPositionWithTakeTurns() {
            assertEquals("freya", game.getCurrentPlayer().getName().substring(1));
            game.pickStartingPosition(new int[]{1, 2});
            game.pickStartingPosition(new int[]{2, 3});

            assertEquals("yoyo", game.getCurrentPlayer().getName().substring(1));
            assertFalse(game.getIsRunning());

            game.pickStartingPosition(new int[]{3, 2});
            game.pickStartingPosition(new int[]{0, 3});

            assertEquals("freya", game.getCurrentPlayer().getName().substring(1));
            assertTrue(game.getIsRunning());
        }
    }

    public static class TestRound {
        private Game game;

        @Before
        public void init() throws Exception {
            game = new Game();
            game.initGame("freya", "yoyo");
            game.chooseGod("Demeter", "Athena");
            game.pickStartingPosition(new int[]{4, 1});
            game.pickStartingPosition(new int[]{2, 3});
            game.pickStartingPosition(new int[]{3, 2});
            game.pickStartingPosition(new int[]{0, 2});
            game.chooseWorker(new int[] {2, 3});

            Board board = game.getBoard();
            board.getCell(3, 3).setOccupied();
            board.getCell(1, 3).addLevel();
            for(int i = 0; i < 2; i++) {
                board.getCell(1, 4).addLevel();
            }
            for(int i = 0; i < 4; i++) {
                board.getCell(2, 2).addLevel();
            }
        }

        @Test
        public void testChooseWorkerWithCurrentPlayer() {
            game.chooseWorker(new int[] {2, 3});
            Worker currentWorker = game.getRoundAction().getRoundWorker();

            Worker workerFB = game.getCurrentPlayer().getWorkerByType(WorkerType.TYPE_B);
            assertEquals(currentWorker, workerFB);
        }

        @Test
        public void testChooseWorkerWithOpponentWorker() {
            boolean success = game.chooseWorker(new int[] {3, 2});

            assertFalse(success);
            assertThat(game.getMessage().trim(), is("Please choosing a worker to start moving!"));
        }

        @Test
        public void testChooseWorkerWithInvalidPosition() {
            boolean success = game.chooseWorker(new int[] {3, 3});

            assertFalse(success);
            assertThat(game.getMessage().trim(), is("Please choosing a worker to start moving!"));
        }

        @Test
        public void testComputableMovableCellsWithoutOpponentPower() {
            Board board = game.getBoard();
            List<Cell> movableCells = game.computeMovableCells();

            assertEquals(4, movableCells.size());
            assertTrue(movableCells.contains(board.getCell(1, 2)));
            assertTrue(movableCells.contains(board.getCell(1, 3)));
            assertTrue(movableCells.contains(board.getCell(2, 4)));
            assertTrue(movableCells.contains(board.getCell(3, 4)));
        }

        @Test
        public void testComputableMovableCellsWithOpponentPower() {
            Board board = game.getBoard();
            game.getOpponentPlayer().getGod().setUsePowerToOpponent();
            List<Cell> movableCells = game.computeMovableCells();

            assertEquals(3, movableCells.size());
            assertTrue(movableCells.contains(board.getCell(1, 2)));
            assertTrue(movableCells.contains(board.getCell(2, 4)));
            assertTrue(movableCells.contains(board.getCell(3, 4)));
        }

        @Test
        public void testRoundMoveWithValidMove() {
            Board board = game.getBoard();
            game.chooseWorker(new int[] {2, 3});
            Worker currentWorker = game.getRoundAction().getRoundWorker();

            game.computeMovableCells();
            boolean success = game.roundMove(new int[]{1, 3});

            assertTrue(success);
            assertTrue(currentWorker.getCurPosition().isEqual(new Cell(1, 3)));
            assertTrue(board.getCell(1, 3).isOccupied());
            assertFalse(board.getCell(2, 3).isOccupied());
            assertEquals(1, currentWorker.getCurPosition().getHeight());
        }

        @Test
        public void testRoundMoveWithInValidMove() {
            Board board = game.getBoard();
            game.chooseWorker(new int[] {2, 3});
            Worker currentWorker = game.getRoundAction().getRoundWorker();

            game.computeMovableCells();
            boolean success = game.roundMove(new int[]{1, 4});

            assertFalse(success);
            assertTrue(currentWorker.getCurPosition().isEqual(new Cell(2, 3)));
            assertFalse(board.getCell(1, 4).isOccupied());
            assertTrue(board.getCell(2, 3).isOccupied());
            assertEquals(game.getMessage().trim(), "Oops! You (freya) cannot move to this cell [1, 4].");
        }

        @Test
        public void testComputableBuildableCells() {
            Board board = game.getBoard();
            List<Cell> buildableCells = game.computeBuildableCells();

            assertEquals(5, buildableCells.size());
            assertTrue(buildableCells.contains(board.getCell(1, 2)));
            assertTrue(buildableCells.contains(board.getCell(1, 3)));
            assertTrue(buildableCells.contains(board.getCell(2, 4)));
            assertTrue(buildableCells.contains(board.getCell(3, 4)));
            assertTrue(buildableCells.contains(board.getCell(1, 4)));
        }

        @Test
        public void testRoundBuildWithValidBuild() {
            Board board = game.getBoard();
            game.computeBuildableCells();
            boolean success = game.roundBuild(new int[]{1, 3});

            assertTrue(success);
            assertEquals(2, board.getCell(1, 3).getHeight());
        }

        @Test
        public void testRoundBuildWithInValidBuild() {
            Board board = game.getBoard();
            game.computeBuildableCells();
            boolean success = game.roundBuild(new int[]{2, 2});

            assertFalse(success);
            assertEquals(4, board.getCell(2, 2).getHeight());
        }

        @Test
        public void testTakeTurn() {
            assertEquals("freya", game.getCurrentPlayer().getName().substring(1));
            game.takeTurns();
            assertEquals("yoyo", game.getCurrentPlayer().getName().substring(1));
        }

        @Test
        public void testCheckWinnerByMoveToTop() {
            Cell curPos = game.getBoard().getCell(2,3);
            for(int i = 0; i < 3; i++) {
                curPos.addLevel();
            }
            game.checkWinner();
            Player winner = game.getWinner();

            assertEquals("freya", winner.getName().substring(1));
            assertEquals(game.getMessage().trim(), "Congratulation! freya is the winner!");
        }

        @Test
        public void testCheckWinnerByFailedBuild() {
            Board board = game.getBoard();
            board.getCell(1, 2).setOccupied();
            board.getCell(1, 3).setOccupied();
            board.getCell(1, 4).setOccupied();
            board.getCell(2, 4).setOccupied();
            board.getCell(3, 4).setOccupied();
            game.computeBuildableCells();
            game.checkWinner();
            Player winner = game.getWinner();

            assertEquals("yoyo", winner.getName().substring(1));
            assertEquals(game.getMessage().trim(), "Congratulation! yoyo is the winner!");
        }

        @Test
        public void testCheckWinnerWithoutWinner() {
            game.checkWinner();
            Player winner = game.getWinner();
            assertNull(winner);
        }
    }
}
