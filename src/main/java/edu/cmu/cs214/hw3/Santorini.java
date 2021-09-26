package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.controller.ActionController;
import edu.cmu.cs214.hw3.controller.GameController;
import edu.cmu.cs214.hw3.utils.Phase;
import edu.cmu.cs214.hw3.utils.MockGameLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Santorini {
    // Game setup
    private static final Game SANTORINI = new Game();
    private static final Board ISLAND_BOARD = SANTORINI.getBoard();
    private static final ActionController ACTION_CONTROLLER = new ActionController(ISLAND_BOARD);
    private static final GameController GAME_CONTROLLER = new GameController(SANTORINI);

    private Santorini() {
        // Disable instantiating this class.
        throw new UnsupportedOperationException();
    }


    public static void main(String[] args) throws IOException {
        MockGameLoader loader = new MockGameLoader(new File("mockSantorini/mockSteps.csv"));
        List<Action> mockSetup = loader.loadMockSetupFromFile();
        List<Action> mockRounds = loader.loadMockRoundsFromFile();

        // Choose players and Game preparation
        boolean canInit = GAME_CONTROLLER.initGame(mockSetup);
        if(!canInit) return;

        // Players picking starting position for workers
        boolean canPickPositions = GAME_CONTROLLER.pickStartingPositions(mockSetup);
        if(!canPickPositions) return;

        // Players take turns to move and build
        GAME_CONTROLLER.runGame(mockRounds, ACTION_CONTROLLER);
    }
}