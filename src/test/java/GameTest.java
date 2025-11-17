import model.Player;
import model.Game;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void testStartAndTurnSwitch() {
        Game game = new Game("P1", "P2");

        assertEquals("P1", game.getCurrentPlayer().getName());
        game.switchPlayer();
        assertEquals("P2", game.getCurrentPlayer().getName());
    }

    @Test
    void testWinCondition() {
        Game game = new Game("P1", "P2");
        Player opponent = game.getOpponent();

        opponent.getBoard().getShips().clear();
        assertTrue(game.checkWinCondition());
    }
}
