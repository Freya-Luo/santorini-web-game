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

    /**
     * Finds the possible cells that worker can build block/dome on. It will remove the last build position
     * as a worker cannot build on the same position twice in a turn.
     *
     * @param worker Chosen worker
     * @param game Current game
     * @return A list of possible cells
     */
    @Override
    public List<Cell> getBuildableCells(Worker worker, Game game) {
        List<Cell> buildableCells = super.getBuildableCells(worker, game);

        if (oldPosition != null) {
            buildableCells.remove(oldPosition);
        }
        return buildableCells;
    }

    /**
     * Build on the current position
     * @param buildOn Position the worker is going to build on
     */
    @Override
    public void doBuild(Cell buildOn) {
        buildOn.addLevel();
        oldPosition = buildOn;

        // If player choose "Yes", then set for the next round
        if (!notAskYet && isAnsYes) {
            oldPosition = null;
        }
    }

    /**
     * Check if this god has the power to do an additional build
     * @return
     */
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
