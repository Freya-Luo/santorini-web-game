package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private final Worker[] workers;
    private boolean isWinner;

    public Player(String name) {
        this.name = name;
        this.workers = new Worker[]{new Worker(WorkerType.TYPE_A, this), new Worker(WorkerType.TYPE_B, this)};
        this.isWinner = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public Worker[] getAllWorkers() {
        return workers;
    }

    public Worker getWorkerByType(WorkerType type) {
        for(Worker worker : workers) {
            if(worker.getType() == type) {
                return worker;
            }
        }
        return null;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setIsWinner() {
        isWinner = true;
    }
}
