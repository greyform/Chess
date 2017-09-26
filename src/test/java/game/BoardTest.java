package game;

import game.piece.ChessPiece;
import game.piece.PieceColor;
import game.piece.PieceType;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class BoardTest {

    ChessPiece pawnB = new ChessPiece(PieceColor.BLACK, PieceType.PAWN);
    ChessPiece rookB = new ChessPiece(PieceColor.BLACK, PieceType.ROOK);
    ChessPiece knightB = new ChessPiece(PieceColor.BLACK, PieceType.KNIGHT);
    ChessPiece bishopB = new ChessPiece(PieceColor.BLACK, PieceType.BISHOP);
    ChessPiece queenB = new ChessPiece(PieceColor.BLACK, PieceType.QUEEN);
    ChessPiece kingB = new ChessPiece(PieceColor.BLACK, PieceType.KING);

    ChessPiece pawnW = new ChessPiece(PieceColor.WHITE, PieceType.PAWN);
    ChessPiece rookW = new ChessPiece(PieceColor.WHITE, PieceType.ROOK);
    ChessPiece knightW = new ChessPiece(PieceColor.WHITE, PieceType.KNIGHT);
    ChessPiece bishopW = new ChessPiece(PieceColor.WHITE, PieceType.BISHOP);
    ChessPiece queenW = new ChessPiece(PieceColor.WHITE, PieceType.QUEEN);
    ChessPiece kingW = new ChessPiece(PieceColor.WHITE, PieceType.KING);

    Player player1;
    Player player2;

    Board board;

    @Before
    public void setUp() {
        player1 = new Player("player1", PieceColor.WHITE);
        player2 = new Player("player2", PieceColor.BLACK);

        board = new Board(player1, player2);
    }


    @Test
    public void BoardOrientation() throws Exception {

        //Assert locations of Non-Pawn Pieces;
        assertEquals(rookB, board.getPiece(0,0));
        assertEquals(knightB, board.getPiece(0,1));
        assertEquals(bishopB, board.getPiece(0,2));
        assertEquals(queenB, board.getPiece(0,3));
        assertEquals(kingB, board.getPiece(0,4));
        assertEquals(bishopB, board.getPiece(0,5));
        assertEquals(knightB, board.getPiece(0,6));
        assertEquals(rookB, board.getPiece(0,7));

        assertEquals(rookW, board.getPiece(7,0));
        assertEquals(knightW, board.getPiece(7,1));
        assertEquals(bishopW, board.getPiece(7,2));
        assertEquals(queenW, board.getPiece(7,3));
        assertEquals(kingW, board.getPiece(7,4));
        assertEquals(bishopW, board.getPiece(7,5));
        assertEquals(knightW, board.getPiece(7,6));
        assertEquals(rookW, board.getPiece(7,7));

        // location of Pawns
        for (int i =0; i < 8; i++) {
            assertEquals(pawnB, board.getPiece(1,i));
            assertEquals(pawnW, board.getPiece(6, i));
        }

    }


    @Test
    public void makeMove() throws Exception {
        // Remove pawns.
        for (int i = 0; i < 8; i++) {
            board.removePiece(1, i);
            board.removePiece(6, i);
        }

        /* Test possible yet invalid move */
        // Black Queen check White King
        board.movePiece(new Move(PieceColor.BLACK, board, 0, 3, 1, 4 ));
        assertFalse(board.makeMove(new Move(PieceColor.WHITE, board, 7, 4, 6, 4)));
        assertTrue(board.makeMove(new Move(PieceColor.WHITE, board, 7, 4, 6, 3)));
        assertTrue(board.undoMove());

    }

    @Test
    public void undoMove() throws Exception {
        board.printBoard();
        board.movePiece(new Move(PieceColor.BLACK, board, 0, 3, 1, 4 ));
        assertEquals(board.getPiece(1, 4), queenB);
        assertNull(board.getPiece(0, 3));

        board.undoMove();
        assertEquals(board.getPiece(0, 3), queenB);
        assertNull(board.getPiece(1, 4));

    }

    @Test
    public void isCheckmate() throws Exception {
        /* Remove all black pieces, except for king */
        board.removePiece(0, 0);
        board.removePiece(0,1);
        board.removePiece(0, 2);
        board.removePiece(0, 3);
        board.removePiece(0, 5);
        board.removePiece(0, 6);
        board.removePiece(0, 7);

        /* Remove all Pawns */
        for (int i = 0; i < 8; i++) {
            board.removePiece(1, i);
            board.removePiece(6, i);
        }

        /* Remove all white pieces except for King and Queen */
        board.removePiece(7, 0);
        board.removePiece(7,1);
        board.removePiece(7, 2);
        board.removePiece(7, 5);
        board.removePiece(7, 6);
        board.removePiece(7, 7);

        // Move black king to coner.
        board.movePiece(new Move(PieceColor.BLACK, board, 0, 4, 0, 7));

        // Move white queen to corner
        board.movePiece(new Move(PieceColor.WHITE, board, 7, 3, 1, 6));

        // no checkmate yet
        assertFalse(board.isCheckmate(1));

        // Move white king to corner. Forms a checkmate.
        board.movePiece(new Move(PieceColor.WHITE, board, 7, 4, 2, 6));


        assertTrue(board.isCheckmate(1));

        board.printBoard();

    }

    @Test
    public void isStalemate() throws Exception {

        /* Remove all black pieces, except for king */
        board.removePiece(0, 0);
        board.removePiece(0,1);
        board.removePiece(0, 2);
        board.removePiece(0, 3);
        board.removePiece(0, 5);
        board.removePiece(0, 6);
        board.removePiece(0, 7);

        /* Remove all Pawns */
        for (int i = 0; i < 8; i++) {
            board.removePiece(1, i);
            board.removePiece(6, i);
        }

        /* Remove all white pieces except for King and Queen */
        board.removePiece(7, 0);
        board.removePiece(7,1);
        board.removePiece(7, 2);
        board.removePiece(7, 5);
        board.removePiece(7, 6);
        board.removePiece(7, 7);

        // Move white king to corner
        board.movePiece(new Move(PieceColor.WHITE, board, 7, 4, 2, 6));

        // Move white queen to corner
        board.movePiece(new Move(PieceColor.WHITE, board, 7, 3, 1, 5));

        // no stalemate yet
        assertEquals(board.isStalemate(1), false);

        // Move black king to coner. Forms a stalemate
        board.movePiece(new Move(PieceColor.BLACK, board, 0, 4, 0, 7));

        board.printBoard();

        assertEquals(board.isStalemate(1), true);

    }

}