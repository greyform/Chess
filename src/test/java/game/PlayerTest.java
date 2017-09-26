package game;

import game.Player;
import game.piece.PieceColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void equals() throws Exception {
        Player player1 = new Player("player1", PieceColor.BLACK);
        Player player2 = new Player( "player1", PieceColor.WHITE);
        Player player3 = new Player("player3", PieceColor.BLACK);
        Player player4= new Player("player1", PieceColor.BLACK);

        assertEquals(player1, player4);
        assertNotEquals(player1, player2);
        assertNotEquals(player1, player3);
    }

    @Test
    public void getterSetter() throws Exception {
        Player player1 = new Player("player1", PieceColor.BLACK);
        player1.increaseNoOfWins();
        player1.increaseNoOfLoses();
        player1.increaseNoOfDraws();

        assertEquals(player1.getNoOfWins(),1);
        assertEquals(player1.getNoOfLoses(),1);
        assertEquals(player1.getNoOfDraws(),1);

    }
}