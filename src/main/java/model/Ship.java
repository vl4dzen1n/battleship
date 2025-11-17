package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс корабля.
 * Хранит размер, клетки и количество попаданий.
 */
public class Ship {

    private final int size;
    private final List<Cell> cells;
    private int hits;

    public Ship(int size) {
        this.size = size;
        this.cells = new ArrayList<>();
        this.hits = 0;
    }

    public int getSize() { return size; }

    public void addCell(Cell cell) {
        cells.add(cell);
        cell.setShip(this);
    }

    public void hit() { hits++; }

    public boolean isSunk() { return hits >= size; }

    public List<Cell> getCells() { return cells; }
}
