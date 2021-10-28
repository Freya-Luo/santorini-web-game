package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.models.Game;
//import edu.cmu.cs214.hw3.utils.RoundAction;
//import edu.cmu.cs214.hw3.utils.MockGameLoader;

import java.io.IOException;

/**
 * The URLs in this game are set as following:
 * 1) "/initGame?nameA=&nameB=" => initGame(nameA, nameB)
 * 2) "/chooseGod?godA=&godB=" => chooseGod(godA, godB)
 * 3) "/pickStartingPosition?x=&y=" => x, y -> int[] pos => pickStartingPosition(pos)
 * 4) "/round?x=&y=" => x, y -> int[] curPos => chooseWorker(curPos) -> computeMovableCells() (check if size == 0, redirect to choose another one)
 * 5) "/round/move?x=&y=" => x, y -> int[] movePos => roundMove(movePos) -> if fail (choose another cell, otherwise continue)
 *  => call getWinner(), if not -> computeBuildableCells() (check if size == 0, lose)
 * 7) "/round/build?x=&y=" => x, y -> int[] buildPos => roundBuild(buildPos) if fail (redirect to another cell), otherwise
 *  => check if canAdditionalBuild() -> no, takeTurns() => yes render UI
 *     7.1) again => yes -> 7); no -> takeTurns()
 */
public final class Santorini{
    // Game setup
    //private static final Board SANTORINI = new Board();
    private static final Game GAME = new Game();

//    public static void main(String[] args) {
//        try {
//            new App();
//        } catch (IOException ioe) {
//            System.err.println("Couldn't start server:\n" + ioe);
//        }
//    }

    // The mock procedure inside main is basically the same as that in the integration tests.
    public static void main(String[] args) throws IOException {
//        MockGameLoader loader = new MockGameLoader(new File("mockSantorini/mockSteps.csv"));
//        List<RoundAction> mockSetup = loader.loadMockSetupFromFile();
//        List<RoundAction> mockRounds = loader.loadMockRoundsFromFile();
//
//        // Choose players and Game preparation
//        String[] names = loader.loadMockPlayerNamesFromFile();
//        boolean canInit = GAME.initGame(names[0], names[1]);
//        if(!canInit) return;
//
//        GAME.chooseGod("Athena", "Pan");
//        // Players picking starting position for workers
//        for(RoundAction setup: mockSetup) {
//            //boolean canPickPositions = GAME.pickStartingPosition(setup.getStartPos());
//            //if(!canPickPositions) return;
//        }
//        System.out.println(SANTORINI.getCurrentPlayer().getName());
//
//        for(RoundAction round: mockRounds) {
//            //GAME.hitRound(round.getType(), round.getMoveTo(), round.getBuildOn());
//        }
    }
}