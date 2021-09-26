package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private String name;
    private final Worker[] workers;
    private boolean isWinner;

    public Player(String name) {
        if(name.equals("")) {
            throw new IllegalArgumentException("Player cannot have an empty name.");
        }
        this.name = name;
        this.workers = new Worker[]{new Worker(WorkerType.TYPE_A, this), new Worker(WorkerType.TYPE_B, this)};
        this.isWinner = false;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String newName) {
        if (!newName.equals("")) {
            name = newName;
            return true;
        }
        return false;
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
