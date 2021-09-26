package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    private Cell cell;

    @Before
    public void init() {
        cell = new Cell(1, 2);
    }

    @Test
    public void testPosition() {
        int x = cell.getX();
        int y = cell.getY();

        assertEquals(x, 1);
        assertEquals(y, 2);
    }

    @Test
    public void testToggleOccupied() {
        cell.setOccupied();
        assertTrue(cell.isOccupied());

        cell.setFree();
        assertFalse(cell.isOccupied());
    }
}
