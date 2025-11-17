
import model.Board;
import model.Ship;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void testPlaceShipSuccessfully() {
        Board board = new Board();
        Ship ship = new Ship(3);

        boolean placed = board.placeShip(ship, 0, 0, true);
        assertTrue(placed);
        assertEquals(1, board.getShips().size());
    }

    @Test
    void testShipPlacementOutOfBounds() {
        Board board = new Board();
        Ship ship = new Ship(4);

        boolean placed = board.placeShip(ship, 8, 8, true);
        assertFalse(placed);
    }

    @Test
    void testShipTooClose() {
        Board board = new Board();

        Ship ship1 = new Ship(3);
        Ship ship2 = new Ship(3);

        assertTrue(board.placeShip(ship1, 0, 0, false));
        assertFalse(board.placeShip(ship2, 1, 1, false)); // касается
    }

    @Test
    void testReceiveShotHit() {
        Board board = new Board();
        Ship ship = new Ship(1);

        board.placeShip(ship, 0, 0, true);

        Board.ShotResult result = board.receiveShot(0, 0);
        assertEquals(Board.ShotResult.HIT, result);
    }

    @Test
    void testReceiveShotMiss() {
        Board board = new Board();
        Board.ShotResult result = board.receiveShot(5, 5);

        assertEquals(Board.ShotResult.MISS, result);
    }
}
