package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Cell;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfiguratorTest {
    private static Configurator configurator;
    private static Cell randomCell;

    @BeforeClass
    public static void init() {
        Board board = new Board();
        randomCell = board.getCell(2, 3);
        configurator = new Configurator(board);
    }

    @Test
    public void testAInitializeURL() {
        assertEquals(randomCell.getLink(), "/game?x=2&y=3");
    }

    @Test
    public void testBMatchPickingStartingPositionURL() {
        configurator.matchPickStartingPositionURL();
        assertEquals(randomCell.getLink(), "/pickingStartingPosition?x=2&y=3");
    }

    @Test
    public void testCMatchChooseWorkerURL() {
        configurator.matchChooseWorkerURL();
        assertEquals(randomCell.getLink(), "/round?x=2&y=3");
    }

    @Test
    public void testDRoundMoveURL() {
        configurator.matchRoundMoveURL();
        assertEquals(randomCell.getLink(), "/round/move?x=2&y=3");
    }

    @Test
    public void testERoundBuildURL() {
        configurator.matchRoundBuildURL();
        assertEquals(randomCell.getLink(), "/round/build?x=2&y=3");
    }

    @Test
    public void testFTakeTurnURL() {
        configurator.matchTakeTurnURL();
        assertEquals(randomCell.getLink(), "/round?x=2&y=3");
    }
}
