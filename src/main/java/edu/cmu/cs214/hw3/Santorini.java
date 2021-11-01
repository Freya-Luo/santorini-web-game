package edu.cmu.cs214.hw3;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.Template;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.utils.GameState;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.Map;

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
public final class Santorini extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new Santorini();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private final Game game;
    private Template template;

    public Santorini() throws IOException {
        super(8080);

        this.game = new Game();
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("eq", ConditionalHelpers.eq);
        this.template = handlebars.compile("main");

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    private int[] parsePos(String x, String y) {
        return new int[]{Integer.parseInt(x), Integer.parseInt(y)};
    }

    @Override
    public Response serve(IHTTPSession session) {

        try {
            String uri = session.getUri();
            Map<String, String> params = session.getParms();
            this.game.setMessage("");
            if (uri.equals("/initGame")) {
                this.game.initGame(params.get("nameA"), params.get("nameB"));
            }
            else if (uri.equals("/chooseGod")) {
               this.game.chooseGod(params.get("godA"), params.get("godB"));
            }
            else if (uri.equals("/pickingStartingPosition")) {
                int[] pos = parsePos(params.get("x"), params.get("y"));
                this.game.pickStartingPosition(pos);
            }
            else if (uri.equals("/round")) {
                int[] curPos = parsePos(params.get("x"), params.get("y"));
                this.game.chooseWorker(curPos);
                if (this.game.getRoundAction().getRoundWorker() != null) {
                    this.game.computeMovableCells();
                }
            }
            else if (uri.equals("/round/move")) {
                int[] moveTo = parsePos(params.get("x"), params.get("y"));
                boolean success = this.game.roundMove(moveTo);
                if (success) {
                    this.game.checkWinner();
                    if (this.game.getWinner() == null) {
                        this.game.computeBuildableCells();
                        this.game.checkWinner();
                    }
                }
            }
            else if (uri.equals("/round/build")) {
                if (params.get("again") != null) {
                    // If worker do not want to do an additional build
                    if(params.get("again").equals("No")) {
                        this.game.getCurrentPlayer().getGod().setAns(params.get("again"));
                        this.game.takeTurns();
                    } else {
                        // If worker want to do an additional build
                        this.game.getCurrentPlayer().getGod().setAns(params.get("again"));
                        this.game.computeBuildableCells();
                        this.game.checkWinner();
                    }
                } else {
                    int[] buildOn = parsePos(params.get("x"), params.get("y"));
                    boolean success = this.game.roundBuild(buildOn);
                    // check if worker can do additional build
                    if (success) {
                        boolean canAdditionalBuild = this.game.getCurrentPlayer().getGod().canAdditionalBuild();
                        if (canAdditionalBuild) {
                           this.game.setMessage("Build Again?");
                        } else {
                            this.game.takeTurns();
                        }
                    }
                }
            }

            // Extract the view-specific data from the game and apply it to the template.
            GameState status = GameState.forGame(this.game);
            String tHTML = this.template.apply(status);
            return newFixedLengthResponse(tHTML);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}