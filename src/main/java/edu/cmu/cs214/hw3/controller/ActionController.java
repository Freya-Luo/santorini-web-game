package edu.cmu.cs214.hw3.controller;

import edu.cmu.cs214.hw3.Board;
import edu.cmu.cs214.hw3.Cell;
import edu.cmu.cs214.hw3.Worker;

import java.util.List;

public class ActionController {
    private final Board board;

    public ActionController(Board board){
        this.board = board;
    }

    public boolean chooseMove(Worker worker, Cell moveTo) {
        // Worker choose adjacent unoccupied cell to move to
        List<Cell> neighbors = board.getNeighbors(worker.getCurPosition());
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

    public boolean chooseBuild(Worker worker, Cell buildOn) {
        // Worker choose adjacent unoccupied cell to build block/dome
        List<Cell> neighbors = board.getNeighbors(worker.getCurPosition());
        List<Cell> buildableCells = worker.getBuildableCells(neighbors);
        if(buildableCells.contains(buildOn)) {
            buildOn.getTower().addLevel();
            return true;
        }
        worker.revertToPrePosition();
        return false;
    }
}
