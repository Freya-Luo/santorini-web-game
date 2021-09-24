package edu.cmu.cs214.hw3;

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

    public Boolean isOccupied() { return isOccupied; }

    public void setOccupied() {
        isOccupied = true;
    }

    public void setFree() {
        isOccupied = false;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public Tower getTower() {
        return tower;
    }
}
