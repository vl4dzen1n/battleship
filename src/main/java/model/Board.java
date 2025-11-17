package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Игровое поле игрока.
 * Хранит клетки, корабли, позволяет размещать корабли и обрабатывать выстрелы.
 */
public class Board {

    public static final int SIZE = 10;
    private final Cell[][] cells;
    private final List<Ship> ships;

    public Board() {
        cells = new Cell[SIZE][SIZE];
        ships = new ArrayList<>();
        initializeBoard();
    }

    /**
     * Инициализация поля клетками.
     */
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                cells[i][j] = new Cell(i, j);
    }

    /**
     * Размещает корабль на поле.
     *
     * @param ship     Корабль
     * @param x        Начальная строка
     * @param y        Начальный столбец
     * @param vertical Вертикальная ориентация
     * @return true, если размещение прошло успешно
     */
    public boolean placeShip(Ship ship, int x, int y, boolean vertical) {
        if (!isValidPlacement(ship, x, y, vertical)) return false;
        for (int i = 0; i < ship.getSize(); i++) {
            int cx = vertical ? x + i : x;
            int cy = vertical ? y : y + i;
            ship.addCell(cells[cx][cy]);
        }
        ships.add(ship);
        return true;
    }

    /**
     * Проверяет, можно ли разместить корабль на поле.
     */
    public boolean isValidPlacement(Ship ship, int x, int y, boolean vertical) {
        return getPlacementError(ship, x, y, vertical) == null;
    }

    /**
     * Возвращает ошибку при попытке размещения корабля, или null если размещение возможно.
     */
    public String getPlacementError(Ship ship, int x, int y, boolean vertical) {
        for (int i = 0; i < ship.getSize(); i++) {
            int cx = vertical ? x + i : x;
            int cy = vertical ? y : y + i;
            if (cx >= SIZE || cy >= SIZE) return "Корабль выходит за границы поля";
            if (cells[cx][cy].hasShip()) return "Место уже занято другим кораблем";
            for (int dx = -1; dx <= 1; dx++)
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = cx + dx, ny = cy + dy;
                    if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && cells[nx][ny].hasShip())
                        return "Корабли слишком близко";
                }
        }
        return null;
    }

    /**
     * Получение результата выстрела по координатам.
     */
    public ShotResult receiveShot(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return ShotResult.INVALID;
        Cell cell = cells[x][y];
        if (cell.isHit() || cell.getState() == Cell.CellState.MISS) return ShotResult.ALREADY_SHOT;
        if (cell.hasShip()) {
            cell.markHit();
            return cell.getShip().isSunk() ? ShotResult.SUNK : ShotResult.HIT;
        } else {
            cell.markMiss();
            return ShotResult.MISS;
        }
    }

    /**
     * Проверяет, потоплены ли все корабли.
     */
    public boolean isAllShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    /**
     * Отображение поля для владельца.
     */
    public void displayForOwner() {
        System.out.print(" ");
        for (int i = 0; i < SIZE; i++) System.out.print((char)('A'+i) + " ");
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i+1) + " ");
            for (int j = 0; j < SIZE; j++) System.out.print(cells[i][j] + " ");
            System.out.println();
        }
    }

    /**
     * Отображение поля для противника (скрыты корабли).
     */
    public void displayForOpponent() {
        System.out.print(" ");
        for (int i = 0; i < SIZE; i++) System.out.print((char)('A'+i) + " ");
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i+1) + " ");
            for (int j = 0; j < SIZE; j++) System.out.print(cells[i][j].toHiddenString() + " ");
            System.out.println();
        }
    }

    public Cell[][] getCells() { return cells; }
    public List<Ship> getShips() { return ships; }

    public enum ShotResult { HIT, MISS, SUNK, INVALID, ALREADY_SHOT }
}
