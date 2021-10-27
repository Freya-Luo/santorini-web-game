package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.cards.God;
import edu.cmu.cs214.hw3.utils.ConfigureMetadata;
import edu.cmu.cs214.hw3.utils.RoundAction;
import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.List;

public class Game {
    private final Board board;
    private Player playerA;
    private Player playerB;
    private Player currentPlayer;
    private boolean isRunning = false;
    private RoundAction roundAction;
    private ConfigureMetadata configurator;

    public Game() {
        this.board = new Board();
        this.configurator = new ConfigureMetadata(board);
    }

    public Player getOpponentPlayer() {
        return currentPlayer == playerA ? playerB : playerA;
    }

    public Board getBoard() {
        return this.board;
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

        playerA = new Player(nameA);
        playerB = new Player(nameB);

        currentPlayer = playerA;
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
            currentPlayer.setGod(godA);
            getOpponentPlayer().setGod(godB);

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
        isRunning = true;

        boolean success = true;
        Worker workerA = currentPlayer.getWorkerByType(WorkerType.TYPE_A);
        Worker workerB = currentPlayer.getWorkerByType(WorkerType.TYPE_B);
        Worker workerOA = getOpponentPlayer().getWorkerByType(WorkerType.TYPE_A);
        Worker workerOB = getOpponentPlayer().getWorkerByType(WorkerType.TYPE_B);

        // Validation check for only allowing picking four workers in total
        if(workerA.getCurPosition() != null && workerB.getCurPosition() != null
            && workerOA.getCurPosition() != null && workerOB.getCurPosition() != null) {
            System.out.println("All workers are set. game is ready to go! Enjoy!");
            return true;
        }

        if(workerA.getCurPosition() == null) {
            success = workerA.setCurPosition(board.getCell(position[0], position[1]));
        } else if (workerB.getCurPosition() == null) {
            success = workerB.setCurPosition(board.getCell(position[0], position[1]));
        }

        if(!success) {
            System.out.println("Sorry, worker cannot stands on an occupied space.");
            return false;
        }

        if (workerA.getCurPosition() != null && workerB.getCurPosition() != null) {
            takeTurns();
            if (workerOA.getCurPosition() != null && workerOB.getCurPosition() != null) {
                configurator.matchRoundURL();
            }
        }
        return true;
    }

    public Worker chooseWorker(int[] curPos) {
        if (!isRunning) return null;

        Cell curCell = board.getCell(curPos[0], curPos[1]);
        Worker worker = currentPlayer.getWorkerByPosition(curCell);
        if(worker == null) {
            System.out.println("Please choosing a worker to start moving!");
            return null;
        }
        roundAction.setRoundWorker(worker);
        return worker;
    }

    public List<Cell> computeMovableCells() {
        Worker roundWorker = roundAction.getRoundWorker();
        God currentGod = currentPlayer.getGod();
        God opponentGod = getOpponentPlayer().getGod();

        List<Cell> movableCells = currentGod.getMovableCells(roundWorker, this);
        // Apply opponent's power from last round
        movableCells = opponentGod.applyOpponentPowerToMove(movableCells, roundWorker);

        roundAction.setRoundPossibleMoves(movableCells);
        return movableCells;
    }

    public boolean roundMove(int[] movePos) {
        Worker roundWorker = roundAction.getRoundWorker();
        List<Cell> possibleMoves = roundAction.getRoundPossibleMoves();
        Cell moveTo = board.getCell(movePos[0], movePos[1]);

        if(possibleMoves.size() == 0 || !possibleMoves.contains(moveTo)) {
            // If moving fails, choose another cell to move to
            System.out.println("Oops! You (" + currentPlayer.getName() +
                    ") cannot move to this cell [" + movePos[0] + ", " +
                    movePos[1] +"].");
            return false;
        }

        God currentGod = currentPlayer.getGod();
        currentGod.doMove(roundWorker, moveTo, this);
        return true;
    }

    public List<Cell> computeBuildableCells() {
        Worker roundWorker = roundAction.getRoundWorker();
        God currentGod = currentPlayer.getGod();
        God opponentGod = getOpponentPlayer().getGod();

        List<Cell> buildableCells = currentGod.getBuildableCells(roundWorker, this);
        // Apply opponent's power from last round
        buildableCells = opponentGod.applyOpponentPowerToBuild(buildableCells);

        roundAction.setRoundPossibleBuilds(buildableCells);
        return buildableCells;
    }

    public boolean roundBuild(int[] buildPos) {
        List<Cell> possibleBuilds = roundAction.getRoundPossibleBuilds();
        Cell buildOn = board.getCell(buildPos[0], buildPos[1]);

        if(possibleBuilds.size() == 0 || !possibleBuilds.contains(buildOn)) {
            // If moving fails, choose another cell to move to
            System.out.println("Sorry! You (" + currentPlayer.getName() +
                    ") cannot build on this cell [" + buildPos[0] + ", "
                    + buildPos[1] + "].");
            return false;
        }

        God currentGod = currentPlayer.getGod();
        currentGod.doBuild(buildOn);
        return true;
    }

    public void takeTurns() {
        if (currentPlayer == null) return;
        currentPlayer = (currentPlayer == playerA) ? playerB : playerA;
    }


    public Player getWinner() {
        Player winner = null;
        if (playerA.isWinner())
            winner =  playerA;
        else if (playerB.isWinner())
            winner =  playerB;

        if (winner != null){
            isRunning = false;
            System.out.println("Congratulation! " + winner.getName() + " is the winner!");
        }
        return winner;
    }
//    public boolean hitRound(int[] curPos, int[] movePos, int[] buildPos) {
//
//        Cell moveTo = game.getBoard().getCell(movePos[0], movePos[1]);
//        // Check if move is valid and update game
//        boolean moveSuccess = canMove(worker, moveTo);
//        if(!moveSuccess) {
//            // If moving fails, choose another cell to move to
//            System.out.println("Oops! You (" + game.getCurrentPlayer().getName() +
//                    ") cannot move to this cell [" + movePos[0] + ", " +
//                    movePos[1] +"].");
//            return false;
//        }
//        currentGod.doMove(worker, moveTo, game);
//
//        worker.checkIfWin();
//        if(getWinner() != null) return true;
//
//        Cell buildOn = game.getBoard().getCell(buildPos[0], buildPos[1]);
//        // Check if build is valid and update game
//        boolean canBuild = canBuild(worker, buildOn);
//        if(!canBuild) {
//            // If moving fails, choose another cell to move to
//            System.out.println("Sorry! You (" + game.getCurrentPlayer().getName() +
//                    ") cannot build on this cell [" + buildPos[0] + ", "
//                    + buildPos[1] + "].");
//            return false;
//        }
//        currentGod.doBuild(buildOn);
//
//        if(getWinner() != null) return true;
//
//        game.takeTurns();
//        return true;
//    }



//    public boolean canMove(Worker worker, Cell moveTo) {
//        God currentGod = game.getCurrentPlayer().getGod();
//        God opponentGod = game.getNextPlayer().getGod();
//        List<Cell> movableCells = currentGod.getMovableCells(worker, game);
//        // Apply opponent's power from last round
//        movableCells = opponentGod.applyOpponentPowerToMove(movableCells, worker);
//
//        if(!movableCells.contains(moveTo)) return false;
//        return true;
//    }


//    public boolean canBuild(Worker worker, Cell buildOn) {
//        God currentGod = game.getCurrentPlayer().getGod();
//        God opponentGod = game.getNextPlayer().getGod();
//
//        List<Cell> neighbors = game.getBoard().getNeighbors(worker.getCurPosition());
//        List<Cell> buildableCells = currentGod.getBuildableCells(neighbors);
//        // Apply opponent's power from last round
//        buildableCells = opponentGod.applyOpponentPowerToBuild(buildableCells);
//
//        if(buildableCells.size() == 0) {
//            game.getNextPlayer().setIsWinner();
//            return false;
//        }
//
//        if(!buildableCells.contains(buildOn)) return false;
//        return true;
//    }
}
