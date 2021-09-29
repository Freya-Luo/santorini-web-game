package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Tower;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TowerTest {
    private Tower tower;

    @Before
    public void init() {
        tower = new Tower();
    }

    @Test
    public void testAddLevel1() {
        tower.addLevel();
        assertEquals(1, tower.getLevel());
    }

    @Test
    public void testAddLevel4() {
        for(int i = 0; i < 4; i++) {
            tower.addLevel();
        }
        assertEquals(4, tower.getLevel());
        assertTrue(tower.isCompleted());
    }

    @Test
    public void testAddInvalidLevel() {
        for(int i = 0; i < 5; i++) {
            tower.addLevel();
        }
        assertEquals(4, tower.getLevel());
        assertTrue(tower.isCompleted());
    }

    @Test
    public void testIsClimbableWithIncompletedTower() {
        Cell from = new Cell(0, 0);
        from.getTower().addLevel();


        for(int i = 0; i < 2; i++) {
            tower.addLevel();
        }

        assertTrue(tower.isClimbable(from));

        tower.addLevel();
        assertFalse(tower.isClimbable(from));
    }

    @Test
    public void testIsClimbableWithCompletedTower() {
        Cell from = new Cell(0, 0);
        for(int i = 0; i < 3; i++) {
            from.getTower().addLevel();
        }

        for(int i = 0; i < 4; i++) {
            tower.addLevel();
        }

        assertFalse(tower.isClimbable(from));
    }

}
