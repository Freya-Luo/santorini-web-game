package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.utils.Action;
import edu.cmu.cs214.hw3.utils.MockGameLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Santorini {
    // Game setup
    private static final Game SANTORINI = new Game();
    private static final Board ISLAND_BOARD = SANTORINI.getBoard();

    private Santorini() {
        // Disable instantiating this class.
        throw new UnsupportedOperationException();
    }

    // The mock procedure inside main is basically the same as that in the integration tests.
    public static void main(String[] args) throws IOException {
        MockGameLoader loader = new MockGameLoader(new File("mockSantorini/mockSteps.csv"));
        List<Action> mockSetup = loader.loadMockSetupFromFile();
        List<Action> mockRounds = loader.loadMockRoundsFromFile();

        // Choose players and Game preparation
        String[] names = loader.loadMockPlayerNamesFromFile();
        boolean canInit = SANTORINI.initGame(names[0], names[1]);
        if(!canInit) return;

        // Players picking starting position for workers
        for(Action setup: mockSetup) {
            boolean canPickPositions = SANTORINI.pickStartingPosition(setup.getType(), setup.getStartPos());
            if(!canPickPositions) return;
        }
        System.out.println(SANTORINI.getCurrentPlayer().getName());
        // Players take turns to move and build
        for(Action round: mockRounds) {
            SANTORINI.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn());
        }
    }
}