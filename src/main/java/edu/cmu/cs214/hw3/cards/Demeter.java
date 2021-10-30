package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

public class Demeter extends God {
    // Keep a private field to record if it can build a second time
    private Cell oldPosition = null;
    private boolean notAskYet = true;
    private boolean isAnsYes = false;

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
        oldPosition = buildOn;

        // If player choose "Yes", then set for the next round
        if (!notAskYet && !isAnsYes) {
            oldPosition = null;
        }
    }

    @Override
    public boolean canAdditionalBuild() {
        // Can only make choice once
        if (isAnsYes) {
            // set for the next round
            notAskYet = true;
            isAnsYes = false;
            return false;
        }

        if (notAskYet) {
            notAskYet = false;
        }
        return true;
    }

    @Override
    public void setAns(String ans) {
        this.isAnsYes = ans.equals("Yes");
        // Can only make choice once
        if (!isAnsYes) {
            oldPosition = null;
            notAskYet = true;
        }
    }

}
