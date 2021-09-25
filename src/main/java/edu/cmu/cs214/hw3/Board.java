package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.Direction;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int ROWSNUMBER = 5;
    private static final int COLSNUMBER = 5;
    private final Cell[][] islandBoard;

    public Board() {
        this.islandBoard = new Cell[ROWSNUMBER][COLSNUMBER];

        for(int i = 0; i < ROWSNUMBER; i++) {
            for(int j = 0; j < COLSNUMBER; j++) {
                this.islandBoard[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCell(int x, int y) {
        if(0 <= x && x < ROWSNUMBER && 0 <= y && y < COLSNUMBER) {
            return islandBoard[x][y];
        }
        return null;
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();

        for(Direction dir: Direction.values()) {
            int nX = cell.getX() + dir.getDir()[0];
            int nY = cell.getY() + dir.getDir()[1];

            Cell neighbor = getCell(nX, nY);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}
