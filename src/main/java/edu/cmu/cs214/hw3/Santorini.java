package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.Action;
import edu.cmu.cs214.hw3.utils.Phase;
import edu.cmu.cs214.hw3.utils.WorkerType;
import edu.cmu.cs214.hw3.utils.mockStepsLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Santorini {
    // Game setup
    private static final Game SANTORINI = new Game();
    private static final Board ISLAND_BOARD = SANTORINI.getBoard();

    private Santorini() {
        // Disable instantiating this class.
        throw new UnsupportedOperationException();
    }
    private static void chooseMove(Worker worker, Cell moveTo) {
        // Worker choose adjacent unoccupied cell to move to
        List<Cell> neighbors = ISLAND_BOARD.getNeighbors(worker.getPosition());
        List<Cell> movableCells = worker.getMovableCells(neighbors);
        if(movableCells.contains(moveTo)) {
            worker.setPosition(moveTo);
        }
        // If worker is at the top of 3-level tower, he wins!
        if(worker.isWin()) {
            return;
        }
    }

    private static void chooseBuild(Worker worker, Cell buildOn) {
        // Worker choose adjacent unoccupied cell to build block/dome
        List<Cell> neighbors = ISLAND_BOARD.getNeighbors(worker.getPosition());
        List<Cell> buildableCells = worker.getBuildableCells(neighbors);
        if(buildableCells.contains(buildOn)) {
            buildOn.getTower().addLevel();
        }
    }

    public static void main(String[] args) throws IOException {
        // Choose players and Game preparation
        Player playerA = new Player("Freya");
        Player playerB = new Player("Yoyo");
        SANTORINI.setPlayers(playerA, playerB);
        SANTORINI.setCurrentPlayer(playerA);
        SANTORINI.setPhase(Phase.RUNNING);

        // Game start - Players picking starting position for workers
        SANTORINI.setCurrentPlayer(playerA);
        SANTORINI.setPhase(Phase.RUNNING);

        playerA.getWorkerByType(WorkerType.TYPE_A).setPosition(ISLAND_BOARD.getCell(2, 3));
        playerA.getWorkerByType(WorkerType.TYPE_B).setPosition(ISLAND_BOARD.getCell(3, 1));

        SANTORINI.takeTurn();
        playerB.getWorkerByType(WorkerType.TYPE_A).setPosition(ISLAND_BOARD.getCell(3, 3));
        playerB.getWorkerByType(WorkerType.TYPE_B).setPosition(ISLAND_BOARD.getCell(0, 2));


        // Players take turns to move and build
        List<Action> mockActions = new mockStepsLoader().loadMockStepsFromFile(new File("mockSantorini/mockSteps.csv"));
        for (Action action : mockActions) {
            SANTORINI.takeTurn();
            Worker currentWorker = SANTORINI.getCurrentPlayer().getWorkerByType(action.getType());
            chooseMove(currentWorker, ISLAND_BOARD.getCell(action.getMoveTo()[0], action.getMoveTo()[1]));
            if (SANTORINI.hasWinner()) {
                System.out.println("Congratulation! " + SANTORINI.getCurrentPlayer().getName() + " is the winner!");
                break;
            } else {
                chooseBuild(currentWorker, ISLAND_BOARD.getCell(action.getBuildOn()[0], action.getBuildOn()[1]));
            }
        }

    }

}