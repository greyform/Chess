package game;

import java.util.LinkedList;
import java.util.Stack;

import game.piece.ChessPiece;
import game.piece.PieceColor;
import game.piece.PieceType;


/**
 * The Board class represents the chess board. It contains a 2D array to track locations of pieces,
 * a Stack to track removed/captured pieces and a LinkedList to track history of moves.It is also
 * responsible to make Moves, search for possible Moves and check game end conditions (isCheckMate,
 * isStaleMate), etc.
 */
public class Board {
    private ChessPiece[][] pieces;
    private Stack<ChessPiece> removedPieces;
    private Player[] players;
    private LinkedList<Move> moveHistory;


    /**
     * Board Constructor.
     * @param white The white Player
     * @param black The black Player
     */
    public Board(Player white, Player black) {
        this.removedPieces = new Stack<ChessPiece>();

        this.players = new Player[2];
        this.players[0] = white;
        this.players[1] = black;

        this.pieces = new ChessPiece [8][8];
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                pieces[i][j] = createChessPiece(i, j);
            }
        }

        this.moveHistory = new LinkedList<Move>();
    }


    /**
     * This function is public and used in the main game loop.
     *
     * It checks whether the Move is Possible, i.e. follows chesspiece-specific movement rules, is not blocked
     * by other pieces on the path, and is not blocked by pieces of same color at the destination.
     * It then checks whether this Move is Valid, i.e. will not cause the King to be checked in the next step.
     *
     * If the conditions are satisfied, the Move is executed through helper function movePiece().
     *
     * @param move The Move that was requested by user.
     * @return true if the Move was successfully executed.
     */
    public boolean makeMove(Move move) {
        if (!move.isMovePossible()) {
            System.out.println("Invalid Move!");
            return false;
        }
        if (willBeChecked(move)) {
            System.out.println("This move will cause your player to be checked!");
            return false;
        }

        ChessPiece curPiece = pieces[move.getSrcX()][move.getSrcY()];
        curPiece.setHasMoved(true);

        movePiece(move);

        return true;
    }


    /**
     * Revert the last Move.
     * @return true if the last Move was successfully undone.
     */

    public boolean undoMove() {
        if (moveHistory.isEmpty()) {
            System.out.println("Invalid Undo!");
            return false;
        }
        Move lastMove = moveHistory.removeFirst();

        int srcX = lastMove.getSrcX();
        int srcY = lastMove.getSrcY();
        int destX = lastMove.getDestX();
        int destY = lastMove.getDestY();

        // if last step was remove piece (Testing Purpose)
        if (destX == -1) {
            pieces[srcX][srcY] = removedPieces.pop();
            return true;
        }

        // if last step was a valid move. restore location.
        pieces[srcX][srcY] = pieces[destX][destY];

        // if last step was a capture, restore captured piece.
        if (lastMove.isCapture()) {
            moveHistory.removeFirst();
            pieces[destX][destY] = removedPieces.pop();
        } else {
            pieces[destX][destY] = null;
        }
        return true;
    }

    /**
     * This function checked ending condition CheckMate.
     * CheckMate requires the King of the color is checked and there is no further move.
     * @param color 0 for white and 1 for black
     * @return true if the King of the color is checkmated.
     */
    public boolean isCheckmate(int color)
    {
        return (isChecked(color) && getAllValidMovesByPlayer(color).isEmpty());
    }


    /**
     * This function checked ending condition StaleMate.
     * StaleMate requires the King of the color is not checked and there is no further move.
     * @param color 0 for white and 1 for black
     * @return true if the King of the color is stalemated.
     */
    public boolean isStalemate(int color)
    {
        return (!isChecked(color) && getAllValidMovesByPlayer(color).isEmpty());
    }


    /**
     * Check whether the King is being checked by the opposite color.
     * Or check pieces of another color can get to the current position of the king within one move.
     * @param color color of the King
     * @return true if King is being checked.
     */
    private boolean isChecked(int color) {
        int kingX = getKingByColor(color)/8; //Kings[color];
        int kingY = getKingByColor(color)%8; //Kings[color];

        for (Move move : getAllPossibleMovesByPlayer((color+1)%2)) { //all possible move of opposite color
            if (move.getDestX() == kingX && move.getDestY() == kingY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the Move will cause the King of the current player to be checked in the next step.
     * make the move, track history, check whether the King isChecked right now. if so, return true.
     * Always undo the move after.
     * @param move move that is supplied by the user
     * @return true if the Move will cause King to be checked
     */
    private boolean willBeChecked(Move move){
        movePiece(move);
        int color = (move.getPlayerColor()==PieceColor.WHITE) ? 0 : 1;
        boolean willBeChecked = isChecked(color);

        undoMove();
        return willBeChecked;
    }

    /**
     *
     *
     * this is a function
     * @param player sdf
     * @return sdfd
     */
    private LinkedList<Move> getAllValidMovesByPlayer(int player) {
        LinkedList<Move> validMoves = new LinkedList<Move>();
        for (int i = 0; i < 63; i++) {
            for (int j = 0; j < 63; j++) {
                Move move = new Move(players[player].getColor(), this, i/8, i%8, j/8, j%8);
                if (move.isMovePossible() && !willBeChecked(move)) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    //TODO: Consider using hashset to replace LinkedList for faster access.
    private LinkedList<Move> getAllPossibleMovesByPlayer(int player) {
        LinkedList<Move> possibleMoves = new LinkedList<Move> ();
        for (int i = 0; i < 63; i++) {
            for (int j = 0; j < 63; j++) {
                Move move = new Move(players[player].getColor(), this, i/8, i%8, j/8, j%8);
                if (move.isMovePossible()) {
                    possibleMoves.add(move);
                }
            }
        }
        return possibleMoves;
    }


    /* Getter Setters | Helper functions |  printBoard() */

    public ChessPiece getPiece(int srcX, int srcY){
        return pieces[srcX][srcY];
    }


    void movePiece(Move move)
    {
        int srcX = move.getSrcX();
        int srcY = move.getSrcY();
        int destX = move.getDestX();
        int destY = move.getDestY();

        ChessPiece srcPiece = pieces[srcX][srcY];

        pieces[srcX][srcY] = null;
        if (pieces[destX][destY] != null && pieces[destX][destY].getColor() != srcPiece.getColor()) {// if destBox is occupied by opponent
            removePiece(destX, destY);
            move.setCapture(true);
        }
        pieces[destX][destY] = srcPiece;

        moveHistory.addFirst(move);
    }

    void removePiece(int srcX, int srcY) {
        ChessPiece curPiece = pieces[srcX][srcY];
        removedPieces.push(curPiece);
        pieces[srcX][srcY] = null;

        Move remove = new Move(curPiece.getColor(), this, srcX, srcY, -1, -1);
        moveHistory.addFirst(remove);
    }

    void addPiece(int srcX, int srcY, ChessPiece newPiece) {
        pieces[srcX][srcY] = newPiece;
    }

    private ChessPiece createChessPiece(int x, int y){
        switch (x) {
            case 0:
                return createChessPiece(PieceColor.BLACK, y);
            case 1:
                return new ChessPiece(PieceColor.BLACK, PieceType.PAWN);
            case 6:
                return new ChessPiece(PieceColor.WHITE, PieceType.PAWN);
            case 7:
                return createChessPiece(PieceColor.WHITE, y);
            default:
                return null;
        }
    }

    private ChessPiece createChessPiece(PieceColor color, int y){
        switch (y) {
            case 0:
                return new ChessPiece(color, PieceType.ROOK);
            case 1:
                return new ChessPiece(color, PieceType.KNIGHT);
            case 2:
                return new ChessPiece(color, PieceType.BISHOP);
            case 3:
                return new ChessPiece(color, PieceType.QUEEN);
            case 4:
                return new ChessPiece(color, PieceType.KING);
            case 5:
                return new ChessPiece(color, PieceType.BISHOP);
            case 6:
                return new ChessPiece(color, PieceType.KNIGHT);
            case 7:
                return new ChessPiece(color, PieceType.ROOK);
        }

        return null;
    }


    private int getKingByColor(int color) {
        PieceColor pieceColor = (color == 0) ? PieceColor.WHITE : PieceColor.BLACK;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece curr = getPiece(i, j);
                if (curr != null && curr.getType() == PieceType.KING && curr.getColor() == pieceColor) {
                    return i*8+j;
                }
            }
        }
        System.err.println("Error: The King for " + color + " no longer exists!");
        return -1;
    }


    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece curr = getPiece(i, j);
                if (curr != null)
                    System.out.print(curr.getType()+ " ");
                else System.out.print("null ");
            }
            System.out.println("");
            System.out.println(" ");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Board))return false;

        return true;
    }
}