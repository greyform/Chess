package game.piece;


public class ChessPiece {
    private PieceColor color;
    private PieceType type;
    private boolean hasMoved;


    /**
     * Constructor for the ChessPiece class.
     * @param color color of the piece.
     * @param type type of the piece.
     */
    public ChessPiece(PieceColor color, PieceType type){
        this.color = color;
        this.type = type;
        this.hasMoved = false;
    }


    public PieceType getType() {
        return type;
    }
    public PieceColor getColor() {
        return color;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ChessPiece))return false;
        ChessPiece otherMyClass = (ChessPiece)other;
        if (otherMyClass.getColor() != this.color ) return false;
        if (otherMyClass.getType() != this.type ) return false;
        if (otherMyClass.isHasMoved() != this.hasMoved) return false;

        return true;
    }

}

