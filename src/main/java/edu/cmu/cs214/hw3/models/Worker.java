package edu.cmu.cs214.hw3.models;
import edu.cmu.cs214.hw3.utils.WorkerType;

public class Worker {
    private final WorkerType type;
    private final Player player;
    private Cell curPosition;

    public Worker(WorkerType type, Player player) {
        this.type = type;
        this.player = player;
        this.curPosition = null;
    }

    public WorkerType getType() {
        return type;
    }

    public Cell getCurPosition() {
        return curPosition;
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
            curPosition.setFree();
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
        // If the player wins by god power
        if (getPlayer().isWinner()) return;
        if(!curPosition.isCompleted() && curPosition.getHeight() == 3) {
            player.setIsWinner();
        }
    }
}
