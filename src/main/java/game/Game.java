package game;



import java.util.Scanner;
import game.piece.PieceColor;



public class Game {
    private static Board board;
    private static Player[] players;
    private static final Scanner scanner = new Scanner(System.in); // scanner for command-line input

    public Game(String username1, String username2){
        players = new Player[2];
        players[0] = new Player(username1, PieceColor.WHITE);
        players[1] = new Player(username2, PieceColor.BLACK);

        board = new Board(players[0], players[1]);
    }

    public static Board getBoard() {
        return board;
    }

    public static Player[] getPlayers() {
        return players;
    }

    /*@Deprecated
    public static void main(String[] args){
        System.out.println("Welcome to Chess!");
        players = new Player[2];

        //  prompt for the user's name
        System.out.print("Enter player 1 name: ");

        // get their input as a String
        String username1 = scanner.next();
        players[0] = new Player(username1, PieceColor.WHITE);


        // prompt for second player 's name
        System.out.print("Enter player 2 name: ");

        // get their input as a String
        String username2 = scanner.next();
        players[1] = new Player(username2, PieceColor.BLACK);

        //set the board
        board = new Board(players[0], players[1]);

        int currentPlayer = 0; //0 for white

        enterGameLoop(currentPlayer);
        scanner.close();
    }*/


    /**
     * Main game loop
     * Keeps prompting the user for nextmove until the game is reading an ending
     * condition such as CheckMate.
     * @param currentPlayer track which player is white.
     */
    public static void enterGameLoop(int currentPlayer) {
        boolean checkmate = false;
        boolean stalemate = false;

        do
        {
            // prompt for first player's move
            System.out.println("Player " + players[currentPlayer].getName() + "'s turn: ");
            System.out.println("(enter move through four integer coordinates: srcX, srcY, destX, destY)");

            int srcX = readCoordinate("srcX");
            int srcY = readCoordinate("srcY");
            int destX = readCoordinate("destX");
            int destY = readCoordinate("destY");

            Move newMove = new Move(players[currentPlayer].getColor(), board, srcX, srcY, destX, destY);

            boolean success = board.makeMove(newMove);

            if (success) {
                System.out.println("Successfully Moved! (" + srcX + ", " + srcY + "), to (" + destX + ", " + destY + ")");
                currentPlayer = (currentPlayer + 1) % 2; //switch turn
                // Check ending conditions
                checkmate = board.isCheckmate(currentPlayer);
                stalemate = board.isStalemate(currentPlayer);
            }

        }while(!checkmate && !stalemate);

        if (checkmate) {
            System.out.println("Congratulations! Player " + players[(currentPlayer+1)%2].getName() + " won!");
            players[(currentPlayer+1)%2].increaseNoOfWins();
            players[currentPlayer].increaseNoOfLoses();
        }
        else {
            System.out.println("The Game ends in a draw");
            players[currentPlayer].increaseNoOfDraws();
            players[currentPlayer+1].increaseNoOfDraws();
        }
    }


    /**
     * Function to read user input of one coordinate from the terminal.
     * @param coordinate "srcX" "srcY" "destX" "destY"
     * @return parsed integer
     */
    public static int readCoordinate(String coordinate)
    {
        System.out.print(coordinate + ": ");
        int coor;
        try {
            coor = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Please enter Integer between 0-7!");
            return readCoordinate(coordinate);
        }

        if(coor>=0 && coor<=7) {
            return coor;
        }
        else {
            System.out.println("Please enter Integer between 0-7!");
            return readCoordinate(coordinate);
        }
    }

}
