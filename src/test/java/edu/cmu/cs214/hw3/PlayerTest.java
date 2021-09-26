package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.WorkerType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void init() {
        player = new Player("Maxence");
    }

    @Test
    public void testInitPlayer() {
        assertThrows(IllegalArgumentException.class, () -> new Player(""));
    }

    @Test
    public void testSetName() {
        boolean resA = player.setName("");
        boolean resB = player.setName("Freya Luo");

        assertFalse(resA);
        assertTrue(resB);
    }

    @Test
    public void testGetWorkers() {
        Worker workerB = player.getWorkerByType(WorkerType.TYPE_B);
        Worker[] workers = player.getAllWorkers();

        assertNotNull(workerB);
        assertEquals(workers[1], workerB);
    }
}
