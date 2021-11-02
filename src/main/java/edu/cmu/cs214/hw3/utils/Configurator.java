package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Cell;

/**
 * This file changes the cell link after certain steps/status of the game. It only works for the implementation
 * of GUI, and does not relate to anything of the domain core design.
 */

public class Configurator {
    private Board board;

    public Configurator(Board board) {
        this.board = board;
        initCells();
    }

    public void initCells() {
        for(Cell[] cellRow: board.getAllCells()) {
            for(Cell cell: cellRow) {
                String link =  "/game?x=" + cell.getX() + "&y=" + cell.getY();
                cell.setLink(link);
                cell.setAvatar("");
                cell.setCssClass("");
            }
        }
    }

    private void changeCellURL(String oldURL, String newURL) {
        for(Cell[] cellRow: board.getAllCells()) {
            for(Cell cell: cellRow) {
                String link =  cell.getLink().replace(oldURL, newURL);
                cell.setLink(link);
            }
        }
    }

    public void matchPickStartingPositionURL() {
        changeCellURL("/game?", "/pickingStartingPosition?");
    }

    public void matchChooseWorkerURL() {
        changeCellURL("/pickingStartingPosition?", "/round?");
    }

    public void matchRoundMoveURL() {
        changeCellURL("?", "/move?");
    }

    public void matchRoundBuildURL() {
        changeCellURL("/move?", "/build?");
    }

    public void matchTakeTurnURL() {
        changeCellURL("/build?", "?");
    }

}
