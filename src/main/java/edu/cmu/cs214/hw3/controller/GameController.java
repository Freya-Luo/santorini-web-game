package edu.cmu.cs214.hw3.controller;

import edu.cmu.cs214.hw3.Game;
import edu.cmu.cs214.hw3.Board;
import edu.cmu.cs214.hw3.Player;
import edu.cmu.cs214.hw3.Worker;
import edu.cmu.cs214.hw3.Action;

import edu.cmu.cs214.hw3.utils.Phase;

import java.util.List;

public class GameController {
    private final Game game;
    private final Board board;
    private Player playerA = null;
    private Player playerB = null;

    public GameController(Game game) {
        this.game = game;
        this.board = game.getBoard();
    }

    public boolean initGame(List<Action> setups) {
        if(game.getPhase() != Phase.PREPARING) return false;

        for(Action setup: setups) {
            if (playerA == null) {
                playerA = new Player(setup.getName());
            } else if (playerB == null && !setup.getName().equals(playerA.getName())) {
                playerB = new Player(setup.getName());
            }
        }

        boolean canSetPlayer = game.setPlayers(playerA, playerB);
        if(!canSetPlayer) {
            System.out.println("Sorry, game needs at least 2 players to start.");
            return false;
        }

        game.setCurrentPlayer(playerA);
        return true;
    }

    public boolean pickStartingPositions(List<Action> setups) {
        final int setupSteps = 4;

        for(Action setup: setups) {
            boolean success = false;
            if(setup.getName().equals(playerA.getName())) {
                game.setCurrentPlayer(playerA);

                success = playerA.getWorkerByType(setup.getType())
                        .setCurPosition(board.getCell(setup.getStartPos()[0], setup.getStartPos()[1]));
            } else if (setup.getName().equals(playerB.getName())) {
                game.setCurrentPlayer(playerB);

                success = playerB.getWorkerByType(setup.getType())
                        .setCurPosition(board.getCell(setup.getStartPos()[0], setup.getStartPos()[1]));
            }
            if(!success) {
                System.out.println("Sorry, worker cannot stands on an occupied space.");
                return false;
            }
        }

        if(setups.size() < setupSteps) {
            System.out.println("Each player has to pick up starting positions for 2 workers.");
            return false;
        }
        game.setPhase(Phase.RUNNING);
        return true;
    }

    public boolean runGame(List<Action> rounds, ActionController actionController) {
        if(game.getPhase() != Phase.RUNNING) {
            System.out.println("Please wait, game is preparing...");
            return false;
        }

        boolean moveSuccess = true;
        boolean buildSuccess = true;

        for (Action action : rounds) {
            if(moveSuccess && buildSuccess) {
                game.takeTurn();
            }
            Worker currentWorker = game.getCurrentPlayer().getWorkerByType(action.getType());
            moveSuccess = actionController.chooseMove(currentWorker, board.getCell(action.getMoveTo()[0], action.getMoveTo()[1]));
            if(!moveSuccess) {
                System.out.println("Oops! You (" + game.getCurrentPlayer().getName() +
                        ") cannot move to this cell [" + action.getMoveTo()[0] + ", " +
                        action.getMoveTo()[1] +"].");
                continue;
            }

            if (game.hasWinner()) {
                System.out.println("Congratulation! " + game.getCurrentPlayer().getName() + " is the winner!");
                game.setPhase(Phase.DONE);
                return true;
            }

            buildSuccess = actionController.chooseBuild(currentWorker, board.getCell(action.getBuildOn()[0], action.getBuildOn()[1]));
            if(!buildSuccess) {
                System.out.println("Sorry! You (" + game.getCurrentPlayer().getName() +
                        ") cannot build on this cell [" + action.getBuildOn()[0] + ", "
                        + action.getBuildOn()[1] + "].");
            }
        }
        System.out.println("Waiting for players actions...");
        return false;
    }
}
