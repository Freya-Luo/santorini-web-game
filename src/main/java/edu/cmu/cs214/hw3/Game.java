package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.Phase;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private Player[] players;
    private Player currentPlayer;
    private Phase phase;

    public Game() {
        this.board = new Board();
        this.players = new Player[2];
        this.phase = Phase.PREPARING;
        this.currentPlayer = null;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player playerA, Player playerB) {
        players[0] = playerA;
        players[1] = playerB;
    }

    public Player getPlayerByName(String name) {
        for (Player player: players) {
            if(name.equals(player.getName())) {
                return player;
            }
        }
        return null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) { currentPlayer = player; }

    public void takeTurn() {
        if (currentPlayer == players[0]) {
            currentPlayer = players[1];
        } else {
            currentPlayer = players[0];
        }
    }

    public Board getBoard() {
        return board;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase newPhase) {
        phase = newPhase;
    }
}
