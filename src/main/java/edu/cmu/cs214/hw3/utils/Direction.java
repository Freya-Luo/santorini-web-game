package edu.cmu.cs214.hw3.utils;

public enum Direction {
    E(0, 1), W(0, -1),
    N(-1, 0), S(1, 0),
    NE(-1, 1), SE(1, 1),
    NW(-1, -1), SW(1, -1);

    private int[] dir;

    Direction(int x, int y) {
        this.dir = new int[]{x, y};
    }

    public int[] getDir() {
        return dir;
    }
}
