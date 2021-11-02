package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

/**
 * This file records the essential information in each round (move and build).
 */
public class RoundAction {
    private Worker roundWorker;
    private List<Cell> roundPossibleMoves;
    private List<Cell> roundPossibleBuilds;

    public void setRoundWorker(Worker worker) { this.roundWorker = worker; }

    public void setRoundPossibleMoves(List<Cell> moves) { this.roundPossibleMoves = moves; }

    public void setRoundPossibleBuilds(List<Cell> builds) { this.roundPossibleBuilds = builds; }

    public Worker getRoundWorker() {
        return this.roundWorker;
    }

    public List<Cell> getRoundPossibleMoves() {
        return this.roundPossibleMoves;
    }

    public List<Cell> getRoundPossibleBuilds() {
        return this.roundPossibleBuilds;
    }
}
