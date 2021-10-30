package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

public class Demeter extends God {
    // Keep a private field to record if it can build a second time
    private Cell oldPosition = null;
    private boolean notAskYet = true;
    private String ans = "No";

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

        // Only If player choose "Yes" && oldPosition is null, then set the oldPosition
        if (!notAskYet && this.ans.equals("Yes")) {
            oldPosition = null;
        }
    }

    @Override
    public boolean canAdditionalBuild() {
        // Has already made choice in this turn, can only make choice once
        if (ans.equals("Yes")) {
            // set for the next round
            notAskYet = true;
            ans = "No";
            return false;
        }

        if (notAskYet) {
            notAskYet = false;
            return true;
        }
        return false;
    }

    @Override
    public void setAns(String ans) {
        this.ans = ans;
        if (ans.equals("No")) {
            oldPosition = null;
            notAskYet = true;
            this.ans = "No";
        }
    }

}
