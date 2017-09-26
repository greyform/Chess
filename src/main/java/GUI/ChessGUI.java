package GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import java.lang.Runnable;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import game.*;

public class ChessGUI {

    private final int N = 8;
    private final int PIXEL = 70;
    private final int MARGIN = 40;

    private JPanel contentPanel;
    private JButton[][] chessPieces;
    private JPanel board;
    private JMenuBar menuBar;

    /**
     *  Constructor to setup to GUI.
     *  contentPanel: the panel that defines frame layout.
     *  board: the panel that is GridLayout, 8x8, holds chessPieces.
     *  chessPieces: double array of JButtons with chess piece image attached to them, to detect movement
     *  menuBar: the main menu to start new games, edit moves, etc.
     */
    public ChessGUI() {
        contentPanel = new JPanel(new BorderLayout(10, 10));

        initButtons();
        initBoard(BorderFactory.createEmptyBorder(MARGIN,MARGIN,MARGIN,MARGIN), new Dimension(N * PIXEL, N * PIXEL));
        initMenu();

        contentPanel.add(board, BorderLayout.CENTER);
        showMessageDialog(null,"Welcome to Chess!","Chess", INFORMATION_MESSAGE);
    }

    /**
     *  Initialize menu bar.
     *  ---------------------------
     *   Game  |  Edit  |  About
     *  ---------------------------
     *   New   |  Undo  |  About
     *  Restart|  Redo  | the game
     *  Resign |        |
     *  ---------------------------
     */
    public void initMenu() {
        menuBar = new JMenuBar();
        // GAME Menu
        JMenu game = new JMenu("Game");
        //New Game item
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNewGame();
            }
        });
        //Restart/Resign item
        JMenuItem restartGame = new JMenuItem("Restart Game");
        JMenuItem resignGame= new JMenuItem("Resign Game");

        game.add(newGame);
        game.add(restartGame);
        game.add(resignGame);


        // EDIT Menu
        JMenu edit = new JMenu(("Edit"));
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");
        edit.add(undo);
        edit.add(redo);

        // ABOUT Menu
        JMenu about = new JMenu("About");
        JMenuItem thisGame = new JMenuItem("About This Game");
        thisGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbout();
            }
        });
        about.add(thisGame);

        menuBar.add(game);
        menuBar.add(edit);
        menuBar.add(about);
    }


    public void initBoard(Border border, Dimension dim) {
        board = new JPanel();
        board.setBorder(border);
        board.setPreferredSize(dim);
        board.setLayout(new GridLayout(N, N, 0, 0));
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board.add(chessPieces[i][j]);
            }
        }
    }

    public void initButtons(){
        chessPieces = new JButton[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i < 2 || i > 5) {
                    chessPieces[i][j] = createButton("" + i + j + ".png");
                } else {
                    chessPieces[i][j] = createButton("empty.png");
                }
                chessPieces[i][j].setBackground(((i+j)%2 == 0) ? Color.gray : Color.WHITE);
            }
        }
    }

    private JButton createButton(String path) {
        JButton button = new JButton();
        try {
            final Image img = ImageIO.read(getClass().getResourceAsStream(path));
            ImageIcon icon = new ImageIcon(img);

            button.setIcon(icon);
            button.setOpaque(true);
            button.setBorderPainted(false);

            button.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent adjustSize) {

                    JButton button = (JButton) adjustSize.getComponent();
                    Dimension size = adjustSize.getComponent().getSize();

                    if (size.width > size.height) {
                        size.width = -1;
                    } else {
                        size.height = -1;
                    }

                    Image newImg = img.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(newImg));
                }

            });

        } catch (Exception ex) {
            //ignore
        }

        return button;
    }


    /**
     * When player clicked "New Game", a new Game object should be created and enter main game loop.
     */
    private void onNewGame() {
        String username1 = showInputDialog("Please enter WHITE player's name: ");
        String username2 = showInputDialog("Please enter BLACK player's name: ");

        Game game = new Game(username1, username2);
        game.enterGameLoop(0);
    }

    /**
     * When player clicked "About this Game", a message dialog should pop-up explaining the Chess Game.
     */
    private void onAbout() {
        showMessageDialog(null,
                "Chess is a two-player strategy board game played on a chessboard, \n" +
                "a checkered gameboard with 64 squares arranged in an 8×8 grid. \n" +
                "The game is played by millions of people worldwide.Each player beg-\n" +
                "ins with 16 pieces: one king, one queen, two rooks, two knights, two \n" +
                "bishops, and eight pawns. Each of the six piece types moves differently,\n" +
                " with the most powerful being the queen and the least powerful the pawn.\n" +
                "The objective is to checkmate the opponent's king by placing it under \n" +
                "an inescapable threat of capture. To this end, a player's pieces are used\n" +
                "to attack and capture the opponent's pieces, while supporting each other.", "About This Game", INFORMATION_MESSAGE);
    }


    /**
     * Main function to start the game GUI.
     */
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            public void run() {
                ChessGUI chessGui = new ChessGUI();
                JFrame frame = new JFrame("Chess");

                frame.setContentPane(chessGui.getContentPanel());
                frame.setJMenuBar(chessGui.getMenuBar());

                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationByPlatform(true);
                frame.pack();


                frame.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /* Getter and Setters */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}
