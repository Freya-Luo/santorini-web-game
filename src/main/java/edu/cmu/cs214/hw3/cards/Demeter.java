package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

public class Demeter extends God {
    // Keep a private field to record if it can build a second time
    private Cell oldPosition = null;

    @Override
    public List<Cell> getBuildableCells(Worker worker, Game game) {
        List<Cell> buildableCells = super.getBuildableCells(worker, game);

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

    @Override
    public boolean canAdditionalBuild() {
        return true;
    }
}
