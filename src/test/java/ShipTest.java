
import model.Ship;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {

    @Test
    void testShipHealthAndSunk() {
        Ship ship = new Ship(3);
        assertEquals(3, ship.getHealth());
        assertFalse(ship.isSunk());

        ship.hit();
        assertEquals(2, ship.getHealth());

        ship.hit();
        ship.hit();
        assertTrue(ship.isSunk());
    }
}
