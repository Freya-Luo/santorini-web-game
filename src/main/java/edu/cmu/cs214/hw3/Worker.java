package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private final WorkerType type;
    private final Player player;
    private Cell curPosition;
    private Cell prePosition;

    public Worker(WorkerType type, Player player) {
        this.type = type;
        this.player = player;
        this.curPosition = null;
        this.prePosition = null;
    }

    public WorkerType getType() {
        return type;
    }

    public Cell getCurPosition() {
        return curPosition;
    }

    public void revertToPrePosition() {
        curPosition.setFree();
        curPosition = prePosition;
        curPosition.setOccupied();
    }

    public void setCurPosition(Cell newPosition) {
        if(curPosition == null) {
            curPosition = newPosition;
        }
        else if(curPosition.getX() != newPosition.getX() || curPosition.getY() != newPosition.getY()) {
            prePosition = curPosition;
            prePosition.setFree();
            curPosition = newPosition;
        }
        curPosition.setOccupied();
    }

    public Player getPlayer() {
        return player;
    }

    public void checkIsWin() {
        if(!curPosition.getTower().isCompleted() && curPosition.getTower().getLevel() == Tower.TOP) {
            player.setIsWinner();
        }
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
    public List<Cell> getMovableCells(List<Cell> neighbors) {
        List<Cell> movableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied() && cell.getTower().isClimbable(curPosition)) {
                movableCells.add(cell);
            }
        }
        return movableCells;
    }

    /**
     * This function checks the eight neighbors of the worker's current position and
     * finds the possible cells that worker can build block/dome on.
     *
     * A cell is unoccupied or its tower is incomplete is considered as buildable.
     * @param neighbors The list of eight neighboring cells.
     * @return The list of possible cells that worker can build on.
     */
    public List<Cell> getBuildableCells(List<Cell> neighbors) {
        List<Cell> buildableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied() && !cell.getTower().isCompleted()) {
                buildableCells.add(cell);
            }
        }
        return buildableCells;
    }
}
