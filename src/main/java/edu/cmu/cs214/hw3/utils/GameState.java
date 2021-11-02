package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Player;

import java.util.Arrays;

/**
 * This file updates the game status for rendering game in the web page. It only works for the implementation
 * of GUI, and does not relate to anything of the domain core design.
 */
public class GameState {

    private final String message;
    private final Cell[][] cells;
    private final String phase;

    private GameState(String message, Cell[][] cells, String phase) {
        this.message = message;
        this.cells = cells;
        this.phase = phase;
    }

    public static GameState forGame(Game game) {
        Cell[][] cells = getCells(game);
        String message = getMessage(game);
        String phase = getPhase(game);
        return new GameState(message, cells, phase);
    }

    public String getMessage() {
        return this.message;
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public String getPhase() {return this.phase;}

    @Override
    public String toString() {
        return "GameState[" +
                "message=" + this.message + ", " +
                "cells=" + Arrays.toString(this.cells) + ']';
    }

    private static String getMessage(Game game) {
        return game.getMessage();
    }

    private static String getPhase(Game game) {
        return game.getPhase();
    }

    private static Cell[][] getCells(Game game) {
        Board board = game.getBoard();
        Cell[][] cells = board.getAllCells();

        if(game.getCurrentPlayer() != null) {
            Player playerA = game.getCurrentPlayer().getName().charAt(0) == 'A'
                    ? game.getCurrentPlayer() : game.getOpponentPlayer();
            Player playerB = game.getCurrentPlayer().getName().charAt(0) == 'B'
                    ? game.getCurrentPlayer() : game.getOpponentPlayer();

            for (int x = 0; x < cells.length; x++) {
                for (int y = 0; y < cells[0].length; y++) {
                    Cell cell = board.getCell(x, y);
                    // First, clear the previous style
                    cell.clearStyle();
                    // Second, check each cell new style
                    if (playerA.getWorkerByPosition(cell) != null) {
                        cell.setAvatar("X");
                    } else if (playerB.getWorkerByPosition(cell) != null) {
                        cell.setAvatar("O");
                    }
                    int height = cell.getHeight();
                    switch (height) {
                        case 1: cell.setCssClass("orange"); break;
                        case 2: cell.setCssClass("green"); break;
                        case 3: cell.setCssClass("yellow"); break;
                        case 4: cell.setCssClass("pink"); break;
                        default: break;
                    }

                }
            }
        }

        return cells;
    }

}

