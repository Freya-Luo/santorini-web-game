package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private Player playerA;
    private Player playerB;
    private Player currentPlayer;

    public Game() {
        this.board = new Board();
        this.currentPlayer = null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) { currentPlayer = player; }

    /**
     * Get the player by his name.
     * @param name Player's name
     * @return The player with this name; null if name does not match.
     */
    public Player getPlayerByName(String name) {

            if(name.equals(playerA.getName())) {
                return playerA;
            } else if (name.equals(playerB.getName())) {
                return playerB;
            }

        return null;
    }

    /**
     * Make 2 players to take turns after a round of valid moving and building.
     */
    public void takeTurns() {
        if(currentPlayer == null) return;

        currentPlayer = (currentPlayer == playerA) ? playerB : playerA;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Check if the winner is generated.
     * @return Game is finished if return true, false otherwise.
     */
    public boolean hasWinner() {
        return playerA.isWinner() || playerB.isWinner();
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

        setCurrentPlayer(playerA);
        return true;
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

        Player currentPlayer = getCurrentPlayer();
        success = currentPlayer.getWorkerByType(type)
                .setCurPosition(board.getCell(position[0], position[1]));

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
        if (setBothWorkers) takeTurns();
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

        boolean moveSuccess = canMoveWorker(type, movePos);
        if(!moveSuccess) {
            // If moving fails, choose another cell to move to
            System.out.println("Oops! You (" + getCurrentPlayer().getName() +
                    ") cannot move to this cell [" + movePos[0] + ", " +
                    movePos[1] +"].");
            return false;
        }

        // If worker is at the top of 3-level tower, he wins!
        // Set current player to be a winner
        currentPlayer.getWorkerByType(type).checkIfWin();
        if (hasWinner()) {
            System.out.println("Congratulation! " + getCurrentPlayer().getName() + " is the winner!");
            return true;
        }

        boolean canBuild = canWorkerBuild(type, buildPos);
        if(!canBuild) {
            // If moving fails, choose another cell to move to
            System.out.println("Sorry! You (" + getCurrentPlayer().getName() +
                    ") cannot build on this cell [" + buildPos[0] + ", "
                    + buildPos[1] + "].");
            return false;
        }

        takeTurns();
        return true;
    }

    /**
     * This function checks the eight neighbors of the worker's current position and
     * finds the possible cells that worker can move to.
     *
     * A cell is unoccupied, or it's level is not 2 or more levels higher than worker's
     * level is considered as movable.
     * @param neighbors The list of eight neighboring cells.
     * @return The list of possible cells that worker can move to.
     */
    private List<Cell> getMovableCells(Cell workerPos, List<Cell> neighbors) {
        List<Cell> movableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied() && cell.isClimbable(workerPos) ) {
                movableCells.add(cell);
            }
        }
        return movableCells;
    }

    /**
     * The precondition of building is worker moves successfully
     * @param type current worker type
     * @param movePos targeted move to position
     * @return True if worker can successfully move to that position; False otherwise
     */
    public boolean canMoveWorker(WorkerType type, int[] movePos) {
        Cell moveTo = board.getCell(movePos[0], movePos[1]);
        Cell workerPos = currentPlayer.getWorkerByType(type).getCurPosition();

        List<Cell> neighbors = board.getNeighbors(workerPos);
        List<Cell> movableCells = getMovableCells(workerPos, neighbors);

        if(!movableCells.contains(moveTo)) return false;

        currentPlayer.getWorkerByType(type).setCurPosition(moveTo);
        return true;
    }

    /**
     * This function checks the eight neighbors of the worker's current position and
     * finds the possible cells that worker can build block/dome on.
     *
     * A cell is unoccupied is considered as buildable.
     * @param neighbors The list of eight neighboring cells.
     * @return The list of possible cells that worker can build on.
     */
    private List<Cell> getBuildableCells(List<Cell> neighbors) {
        List<Cell> buildableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied()) {
                buildableCells.add(cell);
            }
        }
        return buildableCells;
    }

    /**
     * The precondition of building is worker moves successfully
     * @param type current worker type
     * @param buildPos targeted build on position
     * @return True if worker can successfully build on that position; False otherwise
     */
    public boolean canWorkerBuild(WorkerType type, int[] buildPos) {
        Cell buildOn = board.getCell(buildPos[0], buildPos[1]);
        Worker currentWorker = currentPlayer.getWorkerByType(type);

        List<Cell> neighbors = board.getNeighbors(currentWorker.getCurPosition());
        List<Cell> buildableCells = getBuildableCells(neighbors);
        if(!buildableCells.contains(buildOn)){
            currentWorker.revertToPrePosition();
            return false;
        }
        buildOn.addLevel();
        return true;
    }
}
