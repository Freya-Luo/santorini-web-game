package edu.cmu.cs214.hw3.controller;

import edu.cmu.cs214.hw3.cards.God;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Player;
import edu.cmu.cs214.hw3.models.Worker;
import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.List;

public class Controller {
    private final Game GAME;

    public Controller(Game game) {
        this.GAME = game;
    }

    /**
     * Initialize the game with two players and choose a starting player.
     *
     * @param nameA Name of the first player
     * @param nameB Name of the second player
     * @return True if game can be successfully initialized, false if players are missing
     */
    public boolean initGame(String nameA, String nameB) {
        if(nameA == null || nameB == null) {
            System.out.println("Sorry, game needs at least 2 players to start.");
            return false;
        }

        Player playerA = new Player(nameA);
        Player playerB = new Player(nameB);

        GAME.setPlayers(playerA, playerB);
        GAME.setCurrentPlayer(playerA);
        return true;
    }

    public boolean chooseGod(String godNameA, String godNameB) {
        String basePath = "edu.cmu.cs214.hw3.cards.";
        God godA, godB;

        try {
            String classNameA = basePath + godNameA;
            String classNameB = basePath + godNameB;
            godA = (God) Class.forName(classNameA).getDeclaredConstructor().newInstance();
            godB= (God) Class.forName(classNameB).getDeclaredConstructor().newInstance();
            GAME.getCurrentPlayer().setGod(godA);
            GAME.getNextPlayer().setGod(godB);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Pick a starting position for worker, which can only stand on an unoccupied cell.
     *
     * @param type Type of the current chosen worker
     * @param position Starting position for the current worker
     * @return True if worker can be placed on this position; false otherwise
     */
    public boolean pickStartingPosition(WorkerType type, int[] position) {
        boolean success;

        Player currentPlayer = GAME.getCurrentPlayer();
        success = currentPlayer.getWorkerByType(type)
                .setCurPosition(GAME.getBoard().getCell(position[0], position[1]));

        if(!success) {
            System.out.println("Sorry, worker cannot stands on an occupied space.");
            return false;
        }

        boolean setBothWorkers = true;
        for(Worker worker: currentPlayer.getAllWorkers()) {
            if (worker.getCurPosition() == null) {
                setBothWorkers = false;
                break;
            }
        }
        if (setBothWorkers) GAME.takeTurns();
        return true;
    }


    /**
     * Two players take turns to move worker and build tower.
     * This method will reject invalid move or build. The next round can only be hit if
     * last round of another player is valid.
     *
     * @param type Type of the current chosen worker
     * @param movePos Position the worker is going to move to
     * @param buildPos Position the worker is going to build block/dome on
     * @return True if the game is finished and a winner is generated. False if this round
     * has invalid action (either move or build) or no winner is generated.
     */
    public boolean hitRound(WorkerType type, int[] movePos, int[] buildPos) {
        Player currentPlayer = GAME.getCurrentPlayer();
        boolean moveSuccess = canMove(type, movePos);
        if(!moveSuccess) {
            // If moving fails, choose another cell to move to
            System.out.println("Oops! You (" + currentPlayer.getName() +
                    ") cannot move to this cell [" + movePos[0] + ", " +
                    movePos[1] +"].");
            return false;
        }

        // If worker is at the top of 3-level tower, he wins!
        // Set current player to be a winner
        currentPlayer.getWorkerByType(type).checkIfWin();
        if (GAME.hasWinner()) {
            System.out.println("Congratulation! " + currentPlayer.getName() + " is the winner!");
            return true;
        }

        boolean canBuild = canBuild(type, buildPos);
        if(!canBuild) {
            // If moving fails, choose another cell to move to
            System.out.println("Sorry! You (" + currentPlayer.getName() +
                    ") cannot build on this cell [" + buildPos[0] + ", "
                    + buildPos[1] + "].");
            return false;
        }

        if (GAME.hasWinner()) {
            System.out.println("Opps! " + currentPlayer.getName() + " loses the game.");
            return true;
        }

        GAME.takeTurns();
        return true;
    }

    /**
     * The precondition of building is worker moves successfully
     * @param type current worker type
     * @param movePos targeted move to position
     * @return True if worker can successfully move to that position; False otherwise
     */
    public boolean canMove(WorkerType type, int[] movePos) {
        Cell moveTo = GAME.getBoard().getCell(movePos[0], movePos[1]);
        Worker worker = GAME.getCurrentPlayer().getWorkerByType(type);

        God currentGod = GAME.getCurrentPlayer().getGod();
        God opponentGod = GAME.getNextPlayer().getGod();
        List<Cell> movableCells = currentGod.getMovableCells(worker, GAME);
        // Apply opponent's power from last round
        movableCells = opponentGod.applyOpponentPowerToMove(movableCells, worker);

        if(!movableCells.contains(moveTo)) return false;

        currentGod.doMove(worker, moveTo, GAME);
        return true;
    }

    /**
     * The precondition of building is worker moves successfully
     * @param type current worker type
     * @param buildPos targeted build on position
     * @return True if worker can successfully build on that position; False otherwise
     */
    public boolean canBuild(WorkerType type, int[] buildPos) {
        Cell buildOn = GAME.getBoard().getCell(buildPos[0], buildPos[1]);
        Worker currentWorker = GAME.getCurrentPlayer().getWorkerByType(type);

        God currentGod = GAME.getCurrentPlayer().getGod();
        God opponentGod = GAME.getNextPlayer().getGod();
        List<Cell> neighbors = GAME.getBoard().getNeighbors(currentWorker.getCurPosition());
        List<Cell> buildableCells = currentGod.getBuildableCells(neighbors);
        // Apply opponent's power from last round
        buildableCells = opponentGod.applyOpponentPowerToBuild(buildableCells);

        if(!buildableCells.contains(buildOn)){
            GAME.getNextPlayer().setIsWinner();
            return false;
        }
        currentGod.doBuild(buildOn);
        return true;
    }
}
