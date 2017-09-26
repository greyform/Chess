package game;

import game.piece.PieceColor;


public class Player {
    private String name;
    private PieceColor color;
    private int noOfWins;
    private int noOfDraws;
    private int noOfLoses;



    /**
     * Constructor for the Player Class.
     * @param name
     * @param color
     */
    public Player(String name, PieceColor color) {
        this.name = name;
        this.color = color;
        this.noOfWins = 0;
        this.noOfLoses = 0;
        this.noOfDraws = 0;
    }

    public PieceColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void increaseNoOfWins() {
        this.noOfWins += 1;
    }

    public void increaseNoOfDraws() {
        this.noOfDraws += 1;
    }

    public void increaseNoOfLoses() {
        this.noOfLoses += 1;
    }

    public int getNoOfWins() {
        return noOfWins;
    }

    public int getNoOfDraws() {
        return noOfDraws;
    }

    public int getNoOfLoses() {
        return noOfLoses;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Player))return false;
        Player otherMyClass = (Player)other;

        if (otherMyClass.getColor() != this.color) return false;
        if (otherMyClass.getName() != this.name) return false;

        return true;
    }

}
