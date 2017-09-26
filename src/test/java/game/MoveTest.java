package game;

import game.piece.ChessPiece;
import game.piece.PieceColor;
import game.piece.PieceType;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;


public class MoveTest {

    private Player player1;
    private Player player2;

    private Board board;

    @Before
    public void setUp() {
        player1 = new Player("player1", PieceColor.WHITE);
        player2 = new Player("player2", PieceColor.BLACK);

        board = new Board(player1, player2);

        //Remove all pawns to make movement easier.
        for (int i = 0; i < 8; i++) {
            board.removePiece(1, i);
            board.removePiece(6, i);
        }
    }

    @Test
    public void GetterSetterConstructor() throws Exception {
        Move move1 = new Move(PieceColor.BLACK, board, 0,3,7, 3);

        assertEquals(move1.getSrcX(), 0);
        assertEquals(move1.getSrcY(), 3);
        assertEquals(move1.getDestX(), 7);
        assertEquals(move1.getDestY(), 3);
        assertEquals(move1.getBoard(), board);
        assertEquals(move1.getPlayerColor(), PieceColor.BLACK);

        assertFalse(move1.isCapture());

        //Black queen capture white queen.
        board.makeMove(move1);
        assertTrue(move1.isCapture());
    }

    @Test
    public void equals() throws Exception {
        Move move1 = new Move(PieceColor.BLACK, board, 0,0,1, 1);
        Move move2 = new Move(PieceColor.BLACK, board, 0,0,1, 1);
        Move move3 = new Move(PieceColor.WHITE, board, 0,0,1, 1);
        assertTrue(move1.equals(move2));
        assertFalse(move1.equals(move3));
    }

    @Test
    public void isMovePossible() throws Exception {
        // non existent piece
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 1, 1, 0, 1)));
        // wrong color
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 7, 3, 3, 3)));
        // same destination with source
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 7, 3, 7, 3)));


        TestQueenMovement();
        TestKnightMovement();
        TestKingMovement();
        TestRookMovement();
    }

    @Test
    public void TestPawnMovement() throws Exception {
        //put back pawns
        for (int i = 0; i < 8; i++) {
            board.undoMove();
            board.undoMove();
        }

        // move backward
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 1, 1, 0, 1)));
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 1, 1, 0, 1)));
        // diagonal movement without capturing
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 1, 1, 2, 0)));
        // occupied by same color
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 1, 1, 1, 0)));


        // move forward 2 or 1
        assertTrue(board.makeMove(new Move(PieceColor.BLACK, board, 1, 1, 3, 1)));
        assertTrue(board.makeMove(new Move(PieceColor.BLACK, board, 1, 0, 2, 0)));
    }

    private void TestQueenMovement() throws Exception {
        // move forward
        assertTrue(board.makeMove(new Move(PieceColor.WHITE, board, 7, 3, 4, 3)));
        // diagonal movement
        assertTrue(board.makeMove(new Move(PieceColor.WHITE, board, 4, 3, 1, 0)));
    }

    private void TestKnightMovement() throws Exception {
        // move forward
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 0, 1, 3, 1)));
        // diagonal movement
        assertFalse(board.makeMove(new Move(PieceColor.BLACK, board, 0, 1, 1, 2)));
        //L-shaped movement
        assertTrue(board.makeMove(new Move(PieceColor.BLACK, board, 0, 1, 2, 0)));

        board.undoMove();
    }

    private void TestKingMovement() throws Exception {
        // move forward
        assertTrue(board.makeMove(new Move(PieceColor.WHITE, board, 7, 4, 6, 4)));
        // diagonal movement
        assertTrue(board.makeMove(new Move(PieceColor.WHITE, board, 6, 4, 5, 5)));
    }

    private void TestRookMovement() throws Exception {
        // move forward
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 7, 4, 6, 4)));
        // diagonal movement
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 6, 4, 5, 5)));
    }

    @Test
    public void TestCannonMovement() throws Exception {
        ChessPiece cannon = new ChessPiece(PieceColor.WHITE, PieceType.CANNON);
        board.addPiece(4, 4, cannon);
        // move forward
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 4, 4, 5, 4)));
        // diagonal movement
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 4, 4, 6, 3)));
        // Hopping over a curdle
        board.makeMove(new Move(PieceColor.WHITE, board, 7, 3, 4,3));
        assertTrue(board.makeMove(new Move(PieceColor.WHITE, board, 4, 4, 4, 0)));
    }

    @Test
    public void TestAlfilMovement() throws Exception {
        ChessPiece alfil = new ChessPiece(PieceColor.WHITE, PieceType.ALFIL);
        board.addPiece(4, 4, alfil);
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 4, 4, 5, 4)));
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 4, 4, 6, 4)));
        // diagonal movement
        assertTrue(board.makeMove(new Move(PieceColor.WHITE, board, 4, 4, 2, 6)));
    }

}