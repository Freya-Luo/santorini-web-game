package edu.cmu.cs214.hw3.controller;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Player;
import edu.cmu.cs214.hw3.models.Worker;

import edu.cmu.cs214.hw3.utils.Phase;
import edu.cmu.cs214.hw3.utils.WorkerType;

public class GameController {
    private final Game game;
    private final Board board;
    private Player playerA = null;
    private Player playerB = null;
    private boolean isValidRound = true;

    public GameController(Game game) {
        this.game = game;
        this.board = game.getBoard();
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

        game.setPlayers(playerA, playerB);
        game.setCurrentPlayer(playerA);
        return true;
    }

    /**
     * Pick a starting position for worker, which can only stand on an unoccupied cell.
     *
     * @param name Name of the current player
     * @param type Type of the current chosen worker
     * @param position Starting position for the current worker
     * @return True if worker can be placed on this position; false otherwise
     */
    public boolean pickStartingPosition(String name, WorkerType type, int[] position) {
         boolean success;

         Player currentPlayer = game.getPlayerByName(name);
         game.setCurrentPlayer(currentPlayer);
         success = currentPlayer.getWorkerByType(type)
                        .setCurPosition(board.getCell(position[0], position[1]));

         if(!success) {
             System.out.println("Sorry, worker cannot stands on an occupied space.");
             return false;
         }
         return true;
    }

    /**
     * Checks if all 4 workers are correctly placed on the board.
     * @return True if the game is ready to run, false otherwise.
     */
    public boolean readyGo() {
        boolean isReady = true;
        for(Worker worker : playerA.getAllWorkers()) {
            if(worker.getCurPosition() == null) {
                isReady = false;
                break;
            }
        }
        for(Worker worker : playerB.getAllWorkers()) {
            if(worker.getCurPosition() == null) {
                isReady = false;
                break;
            }
        }
        if(!isReady) {
            System.out.println("Each player has to pick up starting positions for 2 workers.");
        } else  {
            game.setPhase(Phase.RUNNING);
        }
        return isReady;
    }

    /**
     * Two players take turns to move worker and build tower.
     * This method will reject invalid move or build. The next round can only be hit if
     * last round of another player is valid.
     *
     * @param type Type of the current chosen worker
     * @param moveTo Position the worker is going to move to
     * @param buildOn Position the worker is going to build block/dome on
     * @param actionController ActionController that executes the current action
     * @return True if the game is finished and a winner is generated. False if this round
     * has invalid action (either move or build) or no winner is generated.
     */
    public boolean hitRound(WorkerType type, int[] moveTo, int[] buildOn, ActionController actionController) {
        if(game.getPhase() != Phase.RUNNING) {
            System.out.println("Please wait, game is preparing...");
            return false;
        }

        //If last round is valid, take turns
        if(isValidRound) {
            game.takeTurns();
        }

        Worker currentWorker = game.getCurrentPlayer().getWorkerByType(type);
        boolean moveSuccess = actionController.chooseMove(currentWorker, board.getCell(moveTo[0], moveTo[1]));
        // If moving fails, choose another cell to move to
        if(!moveSuccess) {
            System.out.println("Oops! You (" + game.getCurrentPlayer().getName() +
                    ") cannot move to this cell [" + moveTo[0] + ", " +
                    moveTo[1] +"].");
            isValidRound = false; // invalid round
            return false;
        }

        // If worker is at the top of 3-level tower, he wins!
        // Set current player to be a winner
        currentWorker.checkIfWin();
        if (game.hasWinner()) {
            System.out.println("Congratulation! " + game.getCurrentPlayer().getName() + " is the winner!");
            game.setPhase(Phase.DONE);
            return true;
        }

        // The precondition of building is worker moves successfully
        boolean buildSuccess = actionController.chooseBuild(currentWorker, board.getCell(buildOn[0], buildOn[1]));
        if(!buildSuccess) {
            System.out.println("Sorry! You (" + game.getCurrentPlayer().getName() +
                    ") cannot build on this cell [" + buildOn[0] + ", "
                        + buildOn[1] + "].");
            currentWorker.revertToPrePosition();
            isValidRound = false; // invalid round
            return false;
        }
        // Mark this round is valid
        isValidRound = true;
        return false;
    }
}
