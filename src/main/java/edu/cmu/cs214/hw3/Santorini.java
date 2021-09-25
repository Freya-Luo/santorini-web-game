package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.Action;
import edu.cmu.cs214.hw3.utils.Phase;
import edu.cmu.cs214.hw3.utils.WorkerType;
import edu.cmu.cs214.hw3.utils.MockGameLoader;

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
    private static boolean chooseMove(Worker worker, Cell moveTo) {
        // Worker choose adjacent unoccupied cell to move to
        List<Cell> neighbors = ISLAND_BOARD.getNeighbors(worker.getCurPosition());
        List<Cell> movableCells = worker.getMovableCells(neighbors);
        if(movableCells.contains(moveTo)) {
            worker.setCurPosition(moveTo);
            // If worker is at the top of 3-level tower, he wins!
            // Set current player is a winner.
            worker.checkIsWin();
            return true;
        }
        return false;
    }

    private static boolean chooseBuild(Worker worker, Cell buildOn) {
        // Worker choose adjacent unoccupied cell to build block/dome
        List<Cell> neighbors = ISLAND_BOARD.getNeighbors(worker.getCurPosition());
        List<Cell> buildableCells = worker.getBuildableCells(neighbors);
        if(buildableCells.contains(buildOn)) {
            buildOn.getTower().addLevel();
            return true;
        }
        worker.revertToPrePosition();
        return false;
    }

    public static void main(String[] args) throws IOException {
        MockGameLoader loader = new MockGameLoader(new File("mockSantorini/mockSteps.csv"));
        List<Action> mockSetup = loader.loadMockSetupFromFile();

        // Choose players and Game preparation
        Player playerA = new Player(mockSetup.get(0).getName());
        Player playerB = new Player(mockSetup.get(2).getName());
        SANTORINI.setPlayers(playerA, playerB);
        SANTORINI.setCurrentPlayer(playerA);
        SANTORINI.setPhase(Phase.RUNNING);

        // Players picking starting position for workers
        for(Action setup: mockSetup) {
            if(setup.getName().equals(playerA.getName())) {
                SANTORINI.setCurrentPlayer(playerA);
                playerA.getWorkerByType(setup.getType())
                        .setCurPosition(ISLAND_BOARD.getCell(setup.getStartPos()[0], setup.getStartPos()[1]));
            } else if (setup.getName().equals(playerB.getName())) {
               SANTORINI.setCurrentPlayer(playerB);
                playerB.getWorkerByType(setup.getType())
                        .setCurPosition(ISLAND_BOARD.getCell(setup.getStartPos()[0], setup.getStartPos()[1]));
            }
        }

        // Players take turns to move and build
        List<Action> mockRounds = loader.loadMockRoundsFromFile();
        boolean moveSuccess = true;
        boolean buildSuccess = true;
        for (Action action : mockRounds) {
            if(moveSuccess && buildSuccess) {
                SANTORINI.takeTurn();
            }
            Worker currentWorker = SANTORINI.getCurrentPlayer().getWorkerByType(action.getType());
            moveSuccess = chooseMove(currentWorker, ISLAND_BOARD.getCell(action.getMoveTo()[0], action.getMoveTo()[1]));
            if(!moveSuccess) {
                System.out.println("Oops! You (" + SANTORINI.getCurrentPlayer().getName() +
                        ") cannot move to this cell [" + action.getMoveTo()[0] + ", " +
                        action.getMoveTo()[1] +"].");
                continue;
            }

            if (SANTORINI.hasWinner()) {
                System.out.println("Congratulation! " + SANTORINI.getCurrentPlayer().getName() + " is the winner!");
                return;
            }

            buildSuccess = chooseBuild(currentWorker, ISLAND_BOARD.getCell(action.getBuildOn()[0], action.getBuildOn()[1]));
            if(!buildSuccess) {
                System.out.println("Sorry! You (" + SANTORINI.getCurrentPlayer().getName() +
                        ") cannot build on this cell [" + action.getBuildOn()[0] + ", "
                        + action.getBuildOn()[1] + "].");
            }
        }
    }
}