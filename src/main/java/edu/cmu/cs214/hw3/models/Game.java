package edu.cmu.cs214.hw3.models;


public class Game {
    private final Board board;
    private Player playerA;
    private Player playerB;
    private Player currentPlayer;
    private boolean isRunning = false;

    public Game() {
        this.board = new Board();
        this.currentPlayer = null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer() {
        return currentPlayer == playerA ? playerB : playerA;
    }

    public void setPlayers(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    /**
     * Get the player by his name.
     *
     * @param name Player's name
     * @return The player with this name; null if name does not match.
     */
    public Player getPlayerByName(String name) {
        if (name.equals(playerA.getName())) {
            return playerA;
        } else if (name.equals(playerB.getName())) {
            return playerB;
        }

        return null;
    }

    public Player[] getAllPlayers() {
        return new Player[]{playerA, playerB};
    }

    /**
     * Make 2 players to take turns after a round of valid moving and building.
     */
    public void takeTurns() {
        if (currentPlayer == null) return;

        currentPlayer = (currentPlayer == playerA) ? playerB : playerA;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Check if the winner is generated.
     *
     * @return Game is finished if return true, false otherwise.
     */
    public Player getWinner() {
        if (playerA.isWinner())
            return playerA;
        else if (playerB.isWinner())
            return playerB;
        return null;
    }

    public void setIsRunning() {
        this.isRunning = true;
    }

    public void setIsFinished() {
        this.isRunning = false;
    }

    public boolean getIsRunning() { return this.isRunning; }
}
