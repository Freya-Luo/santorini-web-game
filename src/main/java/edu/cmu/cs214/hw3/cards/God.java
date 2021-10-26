package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.ArrayList;
import java.util.List;

public abstract class God {
    private boolean powerToMove = false;
    private boolean powerToBuild = false;

    public void setUsePowerToMove() {
        powerToMove = true;
    }

    public void setNotUsePowerToMove() {
        powerToMove = false;
    }

    public boolean ifUsePowerToMove() {
        return powerToMove;
    }

    public void setUsePowerToBuild() {
        powerToBuild = true;
    }

    public void setNotUsePowerToBuild() {
        powerToBuild = false;
    }

    public boolean ifUsePowerToBuild() {
        return powerToBuild;
    }

    /**
     * This function checks the eight neighbors of the worker's current position and
     * finds the possible cells that worker can move to.
     *
     * A cell is unoccupied, or it's level is not 2 or more levels higher than worker's
     * level is considered as movable.
     * @param worker The list of eight neighboring cells.
     * @param game Current game
     * @return The list of possible cells that worker can move to.
     */
    public List<Cell> getMovableCells(Worker worker, Game game) {
        Cell workerPos = worker.getCurPosition();
        List<Cell> neighbors = game.getBoard().getNeighbors(workerPos);
        List<Cell> movableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied() && cell.isClimbable(workerPos) ) {
                movableCells.add(cell);
            }
        }
        return movableCells;
    }

    /**
     * This function checks the eight neighbors of the worker's current position and
     * finds the possible cells that worker can build block/dome on.
     *
     * A cell is unoccupied is considered as buildable.
     * @param neighbors The list of eight neighboring cells.
     * @return The list of possible cells that worker can build on.
     */
    public List<Cell> getBuildableCells(List<Cell> neighbors) {
        List<Cell> buildableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied()) {
                buildableCells.add(cell);
            }
        }
        return buildableCells;
    }


    public void doMove(Worker worker, Cell moveTo, Game game) {
        worker.setCurPosition(moveTo);
    }

    public void doBuild(Cell buildOn) {
        buildOn.addLevel();
    }

    public List<Cell> applyOpponentPowerToMove(List<Cell> movableCells, Worker worker) {
        return movableCells;
    }

    public List<Cell> applyOpponentPowerToBuild(List<Cell> buildableCells) {
        return buildableCells;
    }
}
