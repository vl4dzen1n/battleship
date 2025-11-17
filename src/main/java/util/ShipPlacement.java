package util;

import model.Board;
import model.Ship;
import java.util.Random;

/**
 * Автоматическая расстановка кораблей на поле.
 */
public class ShipPlacement {

    private static final Random random = new Random();
    private static final int[] SHIP_SIZES = {4,3,3,2,2,2,1,1,1,1};

    public static void placeShipsRandomly(Board board) {
        for (int shipSize : SHIP_SIZES) placeSingleShip(board, shipSize);
    }

    private static void placeSingleShip(Board board, int shipSize) {
        boolean placed = false;
        int attempts = 0;
        while (!placed && attempts < 100) {
            int x = random.nextInt(Board.SIZE);
            int y = random.nextInt(Board.SIZE);
            boolean vertical = random.nextBoolean();
            Ship ship = new Ship(shipSize);
            placed = board.placeShip(ship, x, y, vertical);
            attempts++;
        }
        if (!placed) throw new IllegalStateException("Не удалось разместить корабли на поле");
    }
}
