package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.cards.Athena;
import edu.cmu.cs214.hw3.cards.God;
import edu.cmu.cs214.hw3.controller.Controller;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.utils.Action;
import edu.cmu.cs214.hw3.utils.MockGameLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Santorini {
    // Game setup
    private static final Game SANTORINI = new Game();
    private static final Controller controller = new Controller(SANTORINI);

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
        boolean canInit = controller.initGame(names[0], names[1]);
        if(!canInit) return;

        God god = new Athena();
        // Players picking starting position for workers
        for(Action setup: mockSetup) {
            boolean canPickPositions = controller.pickStartingPosition(setup.getType(), setup.getStartPos());
            if(!canPickPositions) return;
        }
        System.out.println(SANTORINI.getCurrentPlayer().getName());
        // Players take turns to move and build
        for(Action round: mockRounds) {
            controller.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn());
        }
    }
}