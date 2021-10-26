package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.controller.Controller;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.utils.Action;
import edu.cmu.cs214.hw3.utils.MockGameLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import fi.iki.elonen.NanoHTTPD;

/**
 * The URLs in this game are set as following:
 * 1) "/initGame?nameA=&nameB=" => initGame(nameA, nameB)
 * 2) "/chooseGod?godA=&godB=" => chooseGod(godA, godB)
 * 3) "/pickStartingPosition?type=&x=&y=" => type-> WorkerType, x, y -> int[] pos => pickStartingPosition(type, pos)
 * 4) "/round/move?type=&x=&y=" => type-> WorkerType, x, y -> int[] pos
 *     => hitRound(actionType, type, int[] moveTo)
 * 5) "/hasWinner" => getWinner()
 * 6) if not, redirect to "/round/build?type=&x=&y=" => type-> WorkerType, x, y -> int[] pos
 *     => hitRound(actionType, type, int[] buildOn)
 */
public final class Santorini{
    // Game setup
    private static final Game SANTORINI = new Game();
    private static final Controller CONTROLLER = new Controller(SANTORINI);

//    public static void main(String[] args) {
//        try {
//            new App();
//        } catch (IOException ioe) {
//            System.err.println("Couldn't start server:\n" + ioe);
//        }
//    }

    // The mock procedure inside main is basically the same as that in the integration tests.
    public static void main(String[] args) throws IOException {
        MockGameLoader loader = new MockGameLoader(new File("mockSantorini/mockSteps.csv"));
        List<Action> mockSetup = loader.loadMockSetupFromFile();
        List<Action> mockRounds = loader.loadMockRoundsFromFile();

        // Choose players and Game preparation
        String[] names = loader.loadMockPlayerNamesFromFile();
        boolean canInit = CONTROLLER.initGame(names[0], names[1]);
        if(!canInit) return;

        CONTROLLER.chooseGod("Athena", "Pan");
        // Players picking starting position for workers
        for(Action setup: mockSetup) {
            boolean canPickPositions = CONTROLLER.pickStartingPosition(setup.getStartPos());
            if(!canPickPositions) return;
        }
        System.out.println(SANTORINI.getCurrentPlayer().getName());
        // Players take turns to move and build
//        for(Action round: mockRounds) {
//            CONTROLLER.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn());
//        }
    }
}