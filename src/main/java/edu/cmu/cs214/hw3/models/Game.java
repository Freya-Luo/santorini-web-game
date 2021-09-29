package edu.cmu.cs214.hw3.models;

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

    /**
     * Set the two player for the game.
     * @param playerA First player
     * @param playerB Second player
     * @return True if Two players are provided and can successfully be set; false otherwise.
     */
    public boolean setPlayers(Player playerA, Player playerB) {
        if(playerA == null || playerB == null) {
            return false;
        }

        players[0] = playerA;
        players[1] = playerB;
        return true;
    }

    /**
     * Get the player by his name.
     * @param name Player's name
     * @return The player with this name; null if name does not match.
     */
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
     * Make 2 players to take turns after a round of valid moving and building.
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
