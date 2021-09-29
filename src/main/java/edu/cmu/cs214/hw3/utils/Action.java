package edu.cmu.cs214.hw3.utils;

/**
 * This Action class closely depends on the MockGameLoader I created, which also
 * serves to simplify testing. Also, by following the writeup of HW2, this class is
 * not tested in this homework as all the public methods are getters. These contain
 * trivial or non-essential functionalities.
 */
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
