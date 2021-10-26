package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Cell;

public class ConfigureMetadata {
    private Board board;

    public ConfigureMetadata(Board board) {
        this.board = board;
    }

    public void initCellMetadata() {
        for(Cell[] cellRow: board.getAllCells()) {
            for(Cell cell: cellRow) {
                String link =  "/game?x=" + cell.getX() + "&y=" + cell.getY();
                cell.setMetadata("", link, "base");
            }
        }
    }

    private void changeCellURL(String oldURL, String newURL) {
        for(Cell[] cellRow: board.getAllCells()) {
            for(Cell cell: cellRow) {
                String link =  cell.getLink().replace(oldURL, newURL);
                cell.setMetadata("", link, "base");
            }
        }
    }

    public void matchPickStartingPositionURL() {
        changeCellURL("/game?", "/pickingStartingPosition?");
    }

    public void matchRoundURL() {
        changeCellURL("/pickingStartingPosition?", "/round?");
    }

    public void matchChooseWorker() {

    }
}
