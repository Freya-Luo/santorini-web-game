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

    /**
     * If the chosen position is movable, then worker moves to it.
     * @param worker Worker is currently picked
     * @param moveTo Position the worker is going to move to
     * @return True if he can successfully move, false otherwise
     */
    public boolean chooseMove(Worker worker, Cell moveTo) {
        // Worker choose adjacent unoccupied cell to move to
        List<Cell> neighbors = board.getNeighbors(worker.getCurPosition());
        List<Cell> movableCells = worker.getMovableCells(neighbors);
        if(movableCells.contains(moveTo)) {
            worker.setCurPosition(moveTo);
            return true;
        }
        return false;
    }

    /**
     * If the chosen position is buildable, then worker builds block/dome on it.
     * @param worker Worker is currently picked
     * @param buildOn Position the worker is going to build the block/dome on
     * @return True if he can successfully build, false otherwise
     */
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
