package edu.cmu.cs214.hw3.models;

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

    @Test
    public void testAddLevel1() {
        cell.addLevel();
        assertEquals(1, cell.getHeight());
    }

    @Test
    public void testAddLevel4() {
        for (int i = 0; i < 4; i++) {
            cell.addLevel();
        }
        assertEquals(4, cell.getHeight());
        assertTrue(cell.isCompleted());
    }

    @Test
    public void testAddInvalidLevel() {
        for (int i = 0; i < 5; i++) {
            cell.addLevel();
        }
        assertEquals(4, cell.getHeight());
        assertTrue(cell.isCompleted());
    }

    @Test
    public void testIsClimbableWithIncompletedCell() {
        Cell from = new Cell(0, 0);
        from.addLevel();


        for (int i = 0; i < 2; i++) {
            cell.addLevel();
        }

        assertTrue(cell.isClimbable(from));

        cell.addLevel();
        assertFalse(cell.isClimbable(from));
    }

    @Test
    public void testIsClimbableWithCompletedCell() {
        Cell from = new Cell(0, 0);
        for (int i = 0; i < 3; i++) {
            from.addLevel();
        }

        for (int i = 0; i < 4; i++) {
            cell.addLevel();
        }

        assertFalse(cell.isClimbable(from));
    }

    @Test
    public void testIsEqualWithSameObj() {
        assertTrue(cell.isEqual(cell));
    }

    @Test
    public void testIsEqualWithSameValue() {
        Cell cellCopy = new Cell(1, 2);
        assertTrue(cell.isEqual(cellCopy));
    }

    @Test
    public void testIsEqualWithDiffValue() {
        Cell cellCopy = new Cell(2, 1);
        assertFalse(cell.isEqual(cellCopy));
    }
}