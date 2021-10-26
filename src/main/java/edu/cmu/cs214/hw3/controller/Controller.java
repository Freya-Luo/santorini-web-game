package edu.cmu.cs214.hw3.controller;

import edu.cmu.cs214.hw3.cards.God;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Player;
import edu.cmu.cs214.hw3.models.Worker;
import edu.cmu.cs214.hw3.utils.ConfigureMetadata;
import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.List;

public class Controller {
    private final Game GAME;
    private ConfigureMetadata configurator;

    public Controller(Game game) {
        this.GAME = game;
        this.configurator = new ConfigureMetadata(GAME.getBoard());
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
        configurator.initCellMetadata();
        return true;
    }

    /**
     * Initialize the god powers for the two players.
     *
     * @param godNameA Name of the god chosen by the first player
     * @param godNameB Name of the god chosen by the second player
     * @return True if gods are successfully initialized; False otherwise
     */
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

            configurator.matchPickStartingPositionURL();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Pick a starting position for worker, which can only stand on an unoccupied cell.
     *
     * @param position Starting position for the current worker
     * @return True if worker can be placed on this position; false otherwise
     */
    public boolean pickStartingPosition(int[] position) {
        GAME.setIsRunning();

        boolean success = true;
        Player currentPlayer = GAME.getCurrentPlayer();
        Player opponentPlayer = GAME.getNextPlayer();

        Worker workerA = currentPlayer.getWorkerByType(WorkerType.TYPE_A);
        Worker workerB = currentPlayer.getWorkerByType(WorkerType.TYPE_B);
        Worker workerOA = opponentPlayer.getWorkerByType(WorkerType.TYPE_A);
        Worker workerOB = opponentPlayer.getWorkerByType(WorkerType.TYPE_B);

        // Validation check for only allowing picking four workers in total
        if(workerA.getCurPosition() != null && workerB.getCurPosition() != null
            && workerOA.getCurPosition() != null && workerOB.getCurPosition() != null) {
            System.out.println("All workers are set. Game is ready to go! Enjoy!");
            return true;
        }

        if(workerA.getCurPosition() == null) {
            success = workerA.setCurPosition(GAME.getBoard().getCell(position[0], position[1]));
        } else if (workerB.getCurPosition() == null) {
            success = workerB.setCurPosition(GAME.getBoard().getCell(position[0], position[1]));
        }

        if(!success) {
            System.out.println("Sorry, worker cannot stands on an occupied space.");
            return false;
        }

        if (workerA.getCurPosition() != null && workerB.getCurPosition() != null) {
            GAME.takeTurns();
            if (workerOA.getCurPosition() != null && workerOB.getCurPosition() != null) {
                configurator.matchRoundURL();
            }
        }
        return true;
    }


    /**
     * Two players take turns to move worker and build tower.
     * This method will reject invalid move or build. The next round can only be hit if
     * last round of another player is valid.
     *
     * @param curPos Position of the current chosen worker
     * @param movePos Position the worker is going to move to
     * @param buildPos Position the worker is going to build block/dome on
     * @return True if the game is finished and a winner is generated. False if this round
     * has invalid action (either move or build) or no winner is generated.
     */
    public boolean hitRound(int[] curPos, int[] movePos, int[] buildPos) {
        if (!GAME.getIsRunning()) return false;

        God currentGod = GAME.getCurrentPlayer().getGod();
        God opponentGod = GAME.getNextPlayer().getGod();

        Cell curCell = GAME.getBoard().getCell(curPos[0], curPos[1]);
        Worker worker = GAME.getCurrentPlayer().getWorkerByPosition(curCell);
        if(worker == null) {
            System.out.println("Please choosing a valid worker to start moving!");
            return false;
        }

        Cell moveTo = GAME.getBoard().getCell(movePos[0], movePos[1]);
        // Check if move is valid and update game
        boolean moveSuccess = canMove(worker, moveTo);
        if(!moveSuccess) {
            // If moving fails, choose another cell to move to
            System.out.println("Oops! You (" + GAME.getCurrentPlayer().getName() +
                    ") cannot move to this cell [" + movePos[0] + ", " +
                    movePos[1] +"].");
            return false;
        }
        currentGod.doMove(worker, moveTo, GAME);

        worker.checkIfWin();
        if(getWinner() != null) return true;

        Cell buildOn = GAME.getBoard().getCell(buildPos[0], buildPos[1]);
        // Check if build is valid and update game
        boolean canBuild = canBuild(worker, buildOn);
        if(!canBuild) {
            // If moving fails, choose another cell to move to
            System.out.println("Sorry! You (" + GAME.getCurrentPlayer().getName() +
                    ") cannot build on this cell [" + buildPos[0] + ", "
                    + buildPos[1] + "].");
            return false;
        }
        currentGod.doBuild(buildOn);

        if(getWinner() != null) return true;

        GAME.takeTurns();
        return true;
    }


    /**
     * The precondition of building is worker moves successfully
     * @param worker current worker
     * @param moveTo targeted move to position
     * @return True if worker can successfully move to that position; False otherwise
     */
    public boolean canMove(Worker worker, Cell moveTo) {
        God currentGod = GAME.getCurrentPlayer().getGod();
        God opponentGod = GAME.getNextPlayer().getGod();
        List<Cell> movableCells = currentGod.getMovableCells(worker, GAME);
        // Apply opponent's power from last round
        movableCells = opponentGod.applyOpponentPowerToMove(movableCells, worker);

        if(!movableCells.contains(moveTo)) return false;
        return true;
    }

    /**
     * The precondition of building is worker moves successfully
     * @param worker current worker
     * @param buildOn targeted build on position
     * @return True if worker can successfully build on that position; False otherwise
     */
    public boolean canBuild(Worker worker, Cell buildOn) {
        God currentGod = GAME.getCurrentPlayer().getGod();
        God opponentGod = GAME.getNextPlayer().getGod();

        List<Cell> neighbors = GAME.getBoard().getNeighbors(worker.getCurPosition());
        List<Cell> buildableCells = currentGod.getBuildableCells(neighbors);
        // Apply opponent's power from last round
        buildableCells = opponentGod.applyOpponentPowerToBuild(buildableCells);

        if(buildableCells.size() == 0) {
            GAME.getNextPlayer().setIsWinner();
            return false;
        }

        if(!buildableCells.contains(buildOn)) return false;
        return true;
    }



    public Player getWinner() {
        Player winner = GAME.getWinner();
        if (winner != null){
            GAME.setIsFinished();
            System.out.println("Congratulation! " + winner.getName() + " is the winner!");
        }
        return winner;
    }
}
