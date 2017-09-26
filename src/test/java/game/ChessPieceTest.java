package game;

import game.piece.ChessPiece;
import game.piece.PieceColor;
import game.piece.PieceType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChessPieceTest {

    @Test
    public void equals() throws Exception {
        ChessPiece pawnB1 = new ChessPiece(PieceColor.BLACK, PieceType.PAWN);
        ChessPiece pawnB2 = new ChessPiece(PieceColor.BLACK, PieceType.PAWN);
        ChessPiece rookB = new ChessPiece(PieceColor.BLACK, PieceType.ROOK);
        ChessPiece pawnW = new ChessPiece(PieceColor.WHITE, PieceType.PAWN);

        assertTrue(pawnB1.equals(pawnB2));
        //wrong type
        assertFalse(pawnB1.equals(rookB));
        //wrong color
        assertFalse(pawnB1.equals(pawnW));
    }

    @Test
    public void getter_setter() throws Exception {
        ChessPiece pawnW = new ChessPiece(PieceColor.WHITE, PieceType.PAWN);

        assertFalse(pawnW.isHasMoved());
        pawnW.setHasMoved(true);
        assertTrue(pawnW.isHasMoved());

    }
}