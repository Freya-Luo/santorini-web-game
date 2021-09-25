package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private final WorkerType type;
    private final Player player;
    private Cell position;

    public Worker(WorkerType type, Player player) {
        this.type = type;
        this.player = player;
        this.position = null;
    }

    public WorkerType getType() {
        return type;
    }

    public Cell getPosition() {
        return position;
    }

    /**
     * This function
     * @param newPosition
     */
    public void setPosition(Cell newPosition) {
        if(position == null) {
            position = newPosition;
        }
        else if(position.getX() != newPosition.getX() || position.getY() != newPosition.getY()) {
            position.setFree();
            position = newPosition;
        }
        position.setOccupied();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean checkIsWin() {
        if(!position.getTower().isCompleted() && position.getTower().getLevel() == 3) {
            player.setIsWinner();
            return true;
        }
        return false;
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
            if (!cell.isOccupied() && cell.getTower().isClimbable(position)) {
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
