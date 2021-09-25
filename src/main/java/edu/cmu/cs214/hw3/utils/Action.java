package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.Worker;

public class Action {
    private WorkerType type;
    private int[] moveTo;
    private int[] buildOn;

    public Action(WorkerType type, int[] moveTo, int[] buildOn) {
        this.type = type;
        this.moveTo = moveTo;
        this.buildOn = buildOn;
    }

    public WorkerType getType() { return type; }

    public int[] getMoveTo() { return moveTo;}

    public int[] getBuildOn() { return buildOn; }
}
