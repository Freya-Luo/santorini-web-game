package edu.cmu.cs214.hw3;

import org.junit.*;
import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void setup() {
        player = new Player("Maxence");
    }

    @Test
    public void testSetName() {
        boolean resA = player.setName("");
        boolean resB = player.setName("Freya Luo");

        assertFalse(resA);
        assertTrue(resB);
    }

}
