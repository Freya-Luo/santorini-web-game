package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Cell;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class BoardTest {
    private Board board;

    @Before
    public void init() {
        board = new Board();
    }

    @Test
    public void testGetCellWithValidXY() {
        Cell cell = board.getCell(0, 4);
        assertThat(cell, instanceOf(Cell.class));
    }

    @Test
    public void testGetCellWithOneInvalid() {
        Cell cell = board.getCell(2, 5);
        assertNull(cell);
    }

    @Test
    public void testGetCellWithBothInvalid() {
        Cell cell = board.getCell(-1, -2);
        assertNull(cell);
    }

    @Test
    public void testGetNeighbors() {
        Cell cell = board.getCell(0, 4);

        List<Cell> neighbors = board.getNeighbors(cell);
        List<int[]> neighborsPositions = new ArrayList<>();
        for (Cell n: neighbors) {
            neighborsPositions.add(new int[]{n.getX(), n.getY()});
        }

        assertEquals(3, neighborsPositions.size());
        assertThat(neighborsPositions, containsInAnyOrder(
                equalTo(new int[]{0, 3}),
                equalTo(new int[]{1, 3}),
                equalTo(new int[]{1, 4})
        ));
    }
}
