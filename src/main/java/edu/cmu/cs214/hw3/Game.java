package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.utils.Phase;

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

    // The game needs at least 2 players to start.
    public boolean setPlayers(Player playerA, Player playerB) {
        if(playerA == null || playerB == null) {
            return false;
        }

        players[0] = playerA;
        players[1] = playerB;
        return true;
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

    /**
     * Make 2 players to take turn after one set of valid moving and building.
     */
    public void takeTurns() {
        if(currentPlayer == null) return;

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

    /**
     * Check if the winner is generated.
     * @return Game is finished if return true, false otherwise.
     */
    public boolean hasWinner() {
        for(Player player: players) {
            if(player.isWinner()) {
                return true;
            }
        }
        return false;
    }
}
