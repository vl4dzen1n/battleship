package model;

/**
 * Одиночная клетка на игровом поле.
 * Хранит информацию о состоянии клетки и ссылку на корабль.
 */
public class Cell {

    private final int x, y;
    private Ship ship;
    private CellState state;

    public enum CellState { EMPTY, HIT, MISS }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = CellState.EMPTY;
    }

    public boolean hasShip() { return ship != null; }
    public void setShip(Ship ship) { this.ship = ship; }
    public Ship getShip() { return ship; }

    public CellState getState() { return state; }
    public boolean isHit() { return state == CellState.HIT; }

    public void markHit() {
        state = CellState.HIT;
        if (ship != null) ship.hit();
    }

    public void markMiss() { state = CellState.MISS; }

    @Override
    public String toString() {
        if (state == CellState.HIT) return "X";
        if (hasShip()) return "S";
        if (state == CellState.MISS) return "O";
        return ".";
    }

    public String toHiddenString() {
        if (state == CellState.HIT) return "X";
        if (state == CellState.MISS) return "O";
        return ".";
    }
}
