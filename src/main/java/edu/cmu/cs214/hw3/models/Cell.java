package edu.cmu.cs214.hw3.models;

public class Cell {
    private final int x;
    private final int y;
    private boolean isOccupied;
    private Tower tower;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.isOccupied = false;
        this.tower = new Tower();
    }

    /**
     * Check if a cell is occupied.
     * A cell is considered as occupied either has a worker or a completed tower.
     *
     * @return True if cell is not occupied, false otherwise.
     */
    public Boolean isOccupied() { return isOccupied || tower.isCompleted(); }

    public void setOccupied() {
        isOccupied = true;
    }

    public void setFree() {
        isOccupied = false;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int[] getGeogPair() {
        return new int[]{x, y};
    }

    public Tower getTower() {
        return tower;
    }
}
