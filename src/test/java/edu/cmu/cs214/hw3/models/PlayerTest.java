package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.utils.WorkerType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;
    private Worker workerA;
    private Worker workerB;

    @Before
    public void init() {
        player = new Player("Maxence");
        workerA = player.getWorkerByType(WorkerType.TYPE_A);
        workerB = player.getWorkerByType(WorkerType.TYPE_B);
    }

    @Test
    public void testInitPlayer() {
        assertThrows(IllegalArgumentException.class, () -> new Player(""));
    }

    @Test
    public void testGetWorkerByType() {
        Worker worker = player.getWorkerByType(WorkerType.TYPE_TEST);

        assertNull(worker);
    }

    @Test
    public void testGetAllWorkers() {
        Worker[] workers = player.getAllWorkers();

        assertNotNull(workerA);
        assertNotNull(workerB);
        assertEquals(workers[0], workerA);
        assertEquals(workers[1], workerB);
    }

    @Test
    public void  testGetWorkerByPositionWithValidPos() {
        workerA.setCurPosition(new Cell(1, 2));
        workerB.setCurPosition(new Cell(3, 4));

        assertEquals(workerA, player.getWorkerByPosition(new Cell(1, 2)));
        assertEquals(workerB, player.getWorkerByPosition(new Cell(3, 4)));
    }

    @Test
    public void  testGetWorkerByPositionWithInvalidPos() {
        workerA.setCurPosition(new Cell(1, 2));
        workerB.setCurPosition(new Cell(2, 2));

        assertNull(player.getWorkerByPosition(new Cell(3, 4)));
    }
}
