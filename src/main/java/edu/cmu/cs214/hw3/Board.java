package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.Direction;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int numsOfRows = 5;
    private static final int numsOfCols = 5;
    private Cell[][] islandBoard;

    public Board() {
        this.islandBoard = new Cell[numsOfRows][numsOfCols];

        for(int i = 0; i < numsOfCols; i++) {
            for(int j = 0; j < numsOfCols; j++) {
                this.islandBoard[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCell(int x, int y) {
        if(0 <= x && x < numsOfRows && 0 <= y && y < numsOfCols) {
            return islandBoard[x][y];
        }
        return null;
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();

        for(Direction dir: Direction.values()) {
            int nX = cell.getX() + dir.getDir()[0];
            int nY = cell.getY() + dir.getDir()[1];

            Cell neighbor;
            if ((neighbor = getCell(nX, nY)) != null) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}
