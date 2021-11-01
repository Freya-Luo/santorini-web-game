package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.cards.God;
import edu.cmu.cs214.hw3.utils.WorkerType;

public class Player {
    private String name;
    private final Worker workerA;
    private final Worker workerB;
    private boolean isWinner;
    private God god;

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
        this.workerA = new Worker(WorkerType.TYPE_A, this);
        this.workerB = new Worker(WorkerType.TYPE_B, this);
        this.isWinner = false;
    }

    public String getName() {
        return name;
    }

    public God getGod() { return this.god; }

    public void setGod(God god) {
        this.god = god;
    }

    public Worker[] getAllWorkers() {
        return new Worker[] {workerA, workerB};
    }

    public Worker getWorkerByType(WorkerType type) {
       if (type == WorkerType.TYPE_A) {
           return workerA;
       } else if (type == WorkerType.TYPE_B) {
           return workerB;
       }
       return null;
    }

    public Worker getWorkerByPosition(Cell pos) {
        if (workerA.getCurPosition() != null
                && workerA.getCurPosition().isEqual(pos)) {
            return workerA;
        }
        if (workerB.getCurPosition() != null
                && workerB.getCurPosition().isEqual(pos)) {
            return workerB;
        }
        return null;
    }

    public boolean isWinner() {return isWinner;}

    public void setIsWinner() {
        isWinner = true;
    }
}
