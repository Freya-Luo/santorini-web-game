package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.Phase;
import edu.cmu.cs214.hw3.utils.WorkerType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Santorini {
    // Game setup
    private static final Game santorini = new Game();
    private static final Board island = santorini.getBoard();

    private Santorini() {
        // Disable instantiating this class.
        throw new UnsupportedOperationException();
    }
    private static void oneMoveAndBuild(Worker worker, Cell moveTo, Cell buildOn) {
        santorini.takeTurn();
        // Worker choose adjacent unoccupied cell to move to
        List<Cell> neighbors = island.getNeighbors(worker.getPosition());
        List<Cell> movableCells = worker.getMovableCells(neighbors);
        if(movableCells.contains(moveTo)) {
            worker.setPosition(moveTo);
        }
        // If worker is at the top of 3-level tower, he wins!
        if(worker.checkIsWin()) {
            return;
        }
        // Worker choose adjacent unoccupied cell to build block/dome
        neighbors = island.getNeighbors(worker.getPosition());
        List<Cell> buildableCells = worker.getBuildableCells(neighbors);
        if(buildableCells.contains(buildOn)) {
            buildOn.getTower().addLevel();
        }
    }

    private static boolean checkWinner() {
        for(Player player: santorini.getPlayers()) {
            if(player.isWinner()) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        // Choose players and Game preparation
        Player playerA = new Player("Freya");
        Player playerB = new Player("Yoyo");
        santorini.setPlayers(playerA, playerB);
        santorini.setCurrentPlayer(playerA);
        santorini.setPhase(Phase.RUNNING);

        // Game start - Players picking starting position for workers
        santorini.setCurrentPlayer(playerA);
        santorini.setPhase(Phase.RUNNING);

        Worker workerA_A = playerA.getWorkerByType(WorkerType.TYPE_A);
        workerA_A.setPosition(island.getCell(2, 3));
        Worker workerA_B = playerA.getWorkerByType(WorkerType.TYPE_B);
        workerA_B.setPosition(island.getCell(3, 1));

        santorini.takeTurn();
        Worker workerB_A = playerB.getWorkerByType(WorkerType.TYPE_A);
        workerB_A.setPosition(island.getCell(3, 3));
        Worker workerB_B = playerB.getWorkerByType(WorkerType.TYPE_B);
        workerB_B.setPosition(island.getCell(0, 2));


        // Players take turns to move and build
        // while(!checkWinner()) {
            oneMoveAndBuild(workerA_A, island.getCell(1, 4), island.getCell(0, 4));
            // ...
        //}
    }

}