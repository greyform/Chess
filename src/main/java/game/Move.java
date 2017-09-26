package game;

import game.piece.ChessPiece;
import game.piece.PieceColor;

import java.lang.Math;


public class Move {
    private final PieceColor player;
    private final Board board;

    private int srcX;
    private int srcY;
    private int destX;
    private int destY;

    private boolean capture;


    /**
     * Constructor for Move Class.
     */
    public Move(PieceColor player, Board board, int srcX, int srcY, int destX, int destY) {
        this.player = player;
        this.board = board;
        this.srcX = srcX;
        this.srcY = srcY;
        this.destX = destX;
        this.destY = destY;
        this.capture = false;
    }


    /**
     * This is a public helper function that checks whether the move is valid with respect to the pieces. \n
     * It does not check whether this move would cause the king to be checkmated.
     * @return true if the move symbolized by this MoveChecker doesn't violate chess rules.
     */
    public boolean isMovePossible() {
        if (board.getPiece(srcX, srcY) == null) {
            //System.out.println("No piece on the starting position!");
            return false;
        }

        if (board.getPiece(srcX, srcY).getColor() != player) {
           // System.out.println("This piece does not belong to you!");
            return false;
        }

        switch (board.getPiece(srcX, srcY).getType()){
            case KNIGHT:
                return isKnightMovePossible();
            case ROOK:
                return isRookMovePossible();
            case BISHOP:
                return isBishopMovePossible();
            case QUEEN:
                return isQueenMovePossible();
            case KING:
                return isKingMovePossible();
            case PAWN:
                return isPawnMovePossible();
            case CANNON:
                return isCannonMovePossible();
            case ALFIL:
                return isAlfinMovePossible();
        }
        return false;
    }


    /**
     *  Test functions to check whether the Move is valid according to game rules.
     *  i.e. satisfy piece rule, path is not blocked, and destination is not occupied by same color.
     * @return return if the move is valid.
     */
    private boolean isKnightMovePossible() {
        return isMoveLShape() && !isDestBlockedBySameColor();
    }
    private boolean isRookMovePossible() {
        return isMoveLinear() && !isPathBlocked() && !isDestBlockedBySameColor();
    }
    private boolean isBishopMovePossible() {
        return isMoveDiagonal() && !isPathBlocked() && !isDestBlockedBySameColor();
    }
    private boolean isQueenMovePossible() {
        return (isMoveLinear() || isMoveDiagonal()) && !isPathBlocked() && !isDestBlockedBySameColor();
    }

    /** Cannon is a classic piece in Chinese Chess, it can only move linearly and hopping over a "curdle" piece.
     * @return true if the Move is possible
     */

    private boolean isCannonMovePossible(){
        return (isMoveLinear() && isPathBlocked()) && !isDestBlockedBySameColor();
    }

    /** Alfil(Elephant) is a classic piece in Chinese Chess, it moves diagonally by distance of two.
     * @return true if the Move is possible
     */
    private boolean isAlfinMovePossible(){
        int distX = Math.abs(destX - srcX);
        int distY = Math.abs(destY - srcY);
        boolean isDistanceDifferByTwo = ((distX==2) && (distY == 2));
        return (isDistanceDifferByTwo && isMoveDiagonal() && !isDestBlockedBySameColor());
    }

    private boolean isKingMovePossible() {
        int distX = Math.abs(destX - srcX);
        int distY = Math.abs(destY - srcY);

        return (distX <= 1 && distY <= 1) && !isDestBlockedBySameColor();
    }

    private boolean isPawnMovePossible() {
        if (isDestBlockedBySameColor()) { //cannot be blocked by same color.
            return false;
        }

        if (player == PieceColor.WHITE) {
            return isPawnMovePossible(-1);
        } else {
            return isPawnMovePossible(1);
        }

    }

    private boolean isPawnMovePossible(int factor){
        int distX = destX - srcX;
        int distY = destY - srcY;

        if (distY == factor && Math.abs(distX) == 1 && isDestBlockedByDifferentColor()) { //diagonal move, capturing.
            return true;
        }
        else if (!board.getPiece(srcX, srcY).isHasMoved()){ //has not moved before
            if ((distX == 2*factor || distX == factor) && distY == 0) { //straightMove, Move upward
                return true;
            }
        }
        else { // hasMoved == true
            if (distX == factor && distY == 0) { //straightMove, Move upward
                return true;
            }
        }

        return false;
    }





    /**
     * Helper functions to check the characteristics of the move.
     * @return true if satisfy the condition specified in names.s
     */
    private boolean isMoveLinear() {
        int distX = destX - srcX;
        int distY = destY - srcY;
        return ((distX == 0) && (distY != 0)) || ((distX != 0) && (distY == 0));
    }

    private boolean isMoveDiagonal() {
        int distX = destX - srcX;
        int distY = destY - srcY;
        return (distX != 0) && (Math.abs(distX) == Math.abs(distY));
    }

    private boolean isMoveLShape() {
        int distX = Math.abs(destX - srcX);
        int distY = Math.abs(destY - srcY);
        return (distX == 2 && distY == 1) || (distX == 1 && distY == 2);
    }

    private boolean isPathBlocked() {
        int distX = destX - srcX;
        int distY = destY - srcY;

        if (isMoveLinear()) {
            if (distX == 0) {
                for (int i = 1; i < Math.abs(distY); i++) {
                    if (board.getPiece(Math.min(srcX, destX), Math.min(srcY, destY)+i) != null) {
                        return true;
                    }
                }
            }
            else { //distY == 0
                for (int i = 1; i < Math.abs(distX)-1; i++) {
                    if (board.getPiece(Math.min(srcX, destX)+i, Math.min(srcY, destY)) != null) {
                        return true;
                    }
                }
            }
        } // end isMoveLinear

        else if (isMoveDiagonal()){
            if (distY + distX == 0) { // one + and one -
                for (int i = 1; i < Math.abs(distX)-1; i++) {
                    if (board.getPiece(Math.min(srcX, destX)+i, Math.max(srcY, destY)-i) != null) {
                        return true;
                    }
                }
            }
            else {  //if (distY - distX == 0)
                for (int i = 1; i < Math.abs(distX)-1; i++) {
                    if (board.getPiece(Math.min(srcX, destX)+i, Math.min(srcY, destY)+i) != null) {
                        return true;
                    }
                }
            }
        } // end isMoveDiagonal
        return false;
    }

    private boolean isDestBlockedBySameColor() {
        ChessPiece destPiece = board.getPiece(destX, destY);
        return (destPiece != null) && (destPiece.getColor() == player);
    }

    private boolean isDestBlockedByDifferentColor() {
        ChessPiece destPiece = board.getPiece(destX, destY);
        return (destPiece != null) && (destPiece.getColor() != player);
    }


    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Move))return false;
        Move otherMyClass = (Move)other;

        if (otherMyClass.getSrcX() != this.srcX) return false;
        if (otherMyClass.getSrcY() != this.srcY) return false;
        if (otherMyClass.getDestX() != this.destX) return false;
        if (otherMyClass.getDestY() != this.destY) return false;
        if (otherMyClass.isCapture() != this.capture) return false;
        if (otherMyClass.getPlayerColor() != this.player) return false;
        if (otherMyClass.getBoard() != this.board) return false;

        return true;
    }

    /* Getter and Setter */

    public PieceColor getPlayerColor() {
        return player;
    }

    public Board getBoard() {
        return board;
    }

    public int getSrcX() {
        return srcX;
    }

    public int getSrcY() {
        return srcY;
    }

    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

}
