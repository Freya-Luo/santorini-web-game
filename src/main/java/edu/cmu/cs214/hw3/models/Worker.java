package edu.cmu.cs214.hw3.models;
import edu.cmu.cs214.hw3.utils.WorkerType;

public class Worker {
    private final WorkerType type;
    private final Player player;
    private Cell curPosition;
    private Cell prePosition;

    public Worker(WorkerType type, Player player) {
        this.type = type;
        this.player = player;
        this.curPosition = null;
        this.prePosition = null;
    }

    public WorkerType getType() {
        return type;
    }

    public Cell getCurPosition() {
        return curPosition;
    }

    public Cell getPrePosition() { return prePosition; }

    /**
     * If building fails, reset the workers to the previous position.
     *
     * @apiNote In principle, worker cannot move back to the previous position.
     * However, this function is currently served to align with the logic of
     * the mock interactions between user and system.
     * It should be reconfigured/removed when GUI interface is integrated.
     */
    public void revertToPrePosition() {
        curPosition.setFree();
        curPosition = prePosition;
        curPosition.setOccupied();
    }

    /**
     * Move to a new position, mark that cell as occupied.
     * Free his previous position.
     *
     * @param newPosition Cell worker is going to move to.
     * @return True if he can successfully move.
     */
    public boolean setCurPosition(Cell newPosition) {
        if(newPosition.isOccupied()){
            return false;
        }
        // Set the starting position.
        if (curPosition != null) {
            prePosition = curPosition;
            prePosition.setFree();
        }
        curPosition = newPosition;
        curPosition.setOccupied();
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Worker (Player) wins if he climbs up to a level-3 tower.
     */
    public void checkIfWin() {
        if(!curPosition.isCompleted() && curPosition.getHeight() == 3) {
            player.setIsWinner();
        }
    }
}
