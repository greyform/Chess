package game;

import game.piece.PieceColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void gameLoop1() throws Exception {
        Game game = new Game("player1", "player2");
        Player player1 = new Player("player1", PieceColor.WHITE);
        Player player2 = new Player("player2", PieceColor.BLACK);

        Board board = new Board(player1, player2);

        assertEquals(game.getBoard(), board);
        assertEquals(game.getPlayers()[0], player1);
        assertEquals(game.getPlayers()[1], player2);
    }


}