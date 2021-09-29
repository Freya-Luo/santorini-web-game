package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.utils.WorkerType;

public class Player {
    private String name;
    private final Worker[] workers;
    private boolean isWinner;

    /**
     * This constructor will check if the name is empty. If it is,
     * then an error will be thrown to indicate that a player must
     * have a valid name.
     *
     * @param name Name to identify the player
     */
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

    /**
     * Set a non-empty name to the player.
     * @param newName New name
     * @return True if name is valid and player's name is changed, false otherwise.
     */
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
