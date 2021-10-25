package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;

import java.util.ArrayList;
import java.util.List;

public class Demeter extends God {
    // Keep a private field to record if can build a second time
    private Cell oldPosition = null;

    @Override
    public List<Cell> getBuildableCells(List<Cell> neighbors) {
        List<Cell> buildableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied()) {
                buildableCells.add(cell);
            }
        }

        if (oldPosition != null) {
            buildableCells.remove(oldPosition);
        }
        return buildableCells;
    }

    @Override
    public void doBuild(Cell buildOn) {
        buildOn.addLevel();
        if (oldPosition == null) {
            oldPosition = buildOn;
        } else {
            oldPosition = null;
        }

    }
}
