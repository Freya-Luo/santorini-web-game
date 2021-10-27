package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

/**
 * This RoundAction class closely depends on the MockGameLoader I created, which also
 * serves to simplify testing. Also, by following the writeup of HW2, this class is
 * not tested in this homework as all the public methods are getters. These contain
 * trivial or non-essential functionalities.
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

    public  List<Cell> getRoundPossibleMoves() {
        return this.roundPossibleMoves;
    }

    public  List<Cell> getRoundPossibleBuilds() {
        return this.roundPossibleBuilds;
    }
}
