package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.controller.ActionController;
import edu.cmu.cs214.hw3.controller.GameController;
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
        String[] names = loader.loadMockPlayerNamesFromFile();
        boolean canInit = GAME_CONTROLLER.initGame(names[0], names[1]);
        if(!canInit) return;

        // Players picking starting position for workers
        for(Action setup: mockSetup) {
            boolean canPickPositions = GAME_CONTROLLER.pickStartingPosition(setup.getName(), setup.getType(), setup.getStartPos());
            if(!canPickPositions) return;
        }

        // All initializations have been done, game is ready to start
        boolean isReady = GAME_CONTROLLER.readyGo();
        if(!isReady) {
            return;
        }

        // Players take turns to move and build
        for(Action round: mockRounds) {
            GAME_CONTROLLER.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn(), ACTION_CONTROLLER);
        }
    }
}