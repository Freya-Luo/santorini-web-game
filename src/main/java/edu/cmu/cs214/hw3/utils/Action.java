package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.Worker;

public class Action {
    private WorkerType type;
    private String name;
    private int[] startPos = null;
    private int[] moveTo = null;
    private int[] buildOn = null;

    public Action(String name, WorkerType type, int[] startPos) {
        this.name = name;
        this.type = type;
        this.startPos = startPos;
    }

    public Action(WorkerType type, int[] moveTo, int[] buildOn) {
        this.type = type;
        this.moveTo = moveTo;
        this.buildOn = buildOn;
    }

    public String getName() { return name; }

    public int[] getStartPos() { return startPos; }

    public WorkerType getType() { return type; }

    public int[] getMoveTo() { return moveTo;}

    public int[] getBuildOn() { return buildOn; }
}
