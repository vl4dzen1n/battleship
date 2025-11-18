package model;

import util.ShipPlacement;

/**
 * Класс игры. Хранит игроков, текущий ход и состояние игры.
 */
public class Game {

    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private GameState state;

    public enum GameState { SETUP_PLAYER1, SETUP_PLAYER2, PLAYING, FINISHED }

    public Game(String player1Name, String player2Name) {
        this.player1 = new Player(player1Name);
        this.player2 = new Player(player2Name);
        this.currentPlayer = player1;
        this.state = GameState.SETUP_PLAYER1;
    }

    public void startGame() {
        state = GameState.SETUP_PLAYER1;
        currentPlayer = player1;
    }

    public void placeShipsAutomatically() {
        if (currentPlayer.getBoard().getShips().isEmpty()) {
            ShipPlacement.placeShipsRandomly(currentPlayer.getBoard());
        }
        nextSetupPhase();
    }

    private void nextSetupPhase() {
        if (state == GameState.SETUP_PLAYER1) {
            state = GameState.SETUP_PLAYER2;
            currentPlayer = player2;
        } else if (state == GameState.SETUP_PLAYER2) {
            state = GameState.PLAYING;
            currentPlayer = player1;
        }
    }

    public void switchPlayer() { currentPlayer = (currentPlayer == player1) ? player2 : player1; }

    public boolean checkWinCondition() {
        Player opponent = getOpponent();
        if (opponent.getBoard().isAllShipsSunk()) {
            state = GameState.FINISHED;
            return true;
        }
        return false;
    }

    public Player getOpponent() { return (currentPlayer == player1) ? player2 : player1; }
    public Player getWinner() { return state != GameState.FINISHED ? null :
            (player1.getBoard().isAllShipsSunk()) ? player2 : player1; }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }
}
