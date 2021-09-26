package edu.cmu.cs214.hw3;

public class Tower {
    private int level;
    private boolean hasDome;
    public static final int TOP = 3;

    public Tower() {
        this.level = 0;
        this.hasDome = false;
    }

    public int getLevel() { return level; }

    public void addLevel() {
        if(level == TOP) {
            setDome();
        } else if (level < TOP) {
            level += 1;
        }
    }

    private void setDome() {
        if (level == TOP) {
            level += 1;
            hasDome = true;
        }
    }

    public boolean isCompleted() {
        return hasDome;
    }

    /**
     * This functions checks if worker can successfully climb up to
     * this tower from his current position.
     *
     * The worker can only move up a maximum of one level higher and tower is not completed.
     * @param from The worker's current position.
     * @return True if worker can climb up to the tower; False otherwise.
     */
    public boolean isClimbable(Cell from) {
        if(isCompleted()) return false;
        return level - from.getTower().getLevel() <= 1;
    }
}
