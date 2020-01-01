package view;

import model.Board;
import model.BoardListener;
import model.GameEvent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import controller.GameController;

/**
 * This class represents the GUI representation for Tic Tac Toe. It is the main
 * component of the view in the MVC pattern.
 * 
 * GameView updates whenever the board has undergone a change. This means that
 * it is registered as a BoardListener.
 * 
 * Constants are used to attempt to dynamically scale text based on the size of
 * a Board. Please note, due to fonts being defined on an OS basis, results past
 * 7x7 are not guaranteed to display correctly (although the game will function
 * correctly).
 * 
 * @author John Breton
 * @version December 15th, 2019
 */
public class GameView extends JFrame implements ActionListener, BoardListener, Runnable {

    /**
     * A percentage (60%) of the current display's height (or width, depending on
     * which is greater), which will be used in calculations to determine
     * appropriate scaling of GameView elements.
     */
    public static final double SIDE_LENGTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() > Toolkit
            .getDefaultToolkit().getScreenSize().getHeight()
                    ? (0.6 * Toolkit.getDefaultToolkit().getScreenSize().getHeight())
                    : (0.6 * Toolkit.getDefaultToolkit().getScreenSize().getWidth());

    // Display size of the Xs and Os.
    public static final int X_O_SIZE = (int) ((SIDE_LENGTH * 0.8) / (Board.SIZE));
    // Display size for all other text in the game.
    public static final int TEXT_SIZE = (int) (SIDE_LENGTH / 45);

    private Board board;
    private JButton btnReset, btnSave, btnLoad;
    private JMenuBar menuBar;
    private JButton[][] buttons;
    private JPanel turnPanel, gridPanel;
    private JTextArea turnDisplay;
    private GameController gameController;
    private JFileChooser fc;

    /**
     * Construct a new GameView.
     * 
     * @param board The Board this GameView will be listening to
     */
    public GameView(Board board) {
        fc = new JFileChooser();
        this.board = board;
        this.board.addListener(this);
        this.gameController = new GameController(board);
        gridPanel = new JPanel(new GridLayout(Board.SIZE, Board.SIZE));

        this.setJMenuBar(menuBar = new JMenuBar());
        menuBar.setLayout(new GridLayout(1, 3));

        menuBar.add(btnReset = createMenuBarButton("Reset"));
        menuBar.add(btnSave = createMenuBarButton("Save"));
        menuBar.add(btnLoad = createMenuBarButton("Load"));

        intializeTurnText();
        initializeButtons();
        intializeFrame(this);

        btnReset.addActionListener(this);
        btnSave.addActionListener(this);
        btnLoad.addActionListener(this);
    }

    /**
     * Create and initialize a JButton to add to the JMenuBar.
     *
     * @param text The text inside the button
     * @return The newly created JButton
     */
    private static JButton createMenuBarButton(String text) {
        JButton button = new JButton("<html><p style='text-align:center;'>" + text + "</p></html>");
        button.setFont(new Font("Times New Roman", Font.PLAIN, TEXT_SIZE));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        return button;
    }

    /**
     * Initialize a JTextArea that displays information about the current turn.
     */
    private void intializeTurnText() {
        turnPanel = new JPanel();
        turnPanel.add(turnDisplay = new JTextArea("It is X's turn."));
        turnDisplay.setEditable(false);
        turnDisplay.setFont(new Font("Times New Roman", Font.BOLD, TEXT_SIZE));
        turnDisplay.setHighlighter(null);
        turnDisplay.setOpaque(false);
    }

    /**
     * Initialize a JFrame with default specifications.
     * 
     * @param parent Used to properly display the exit dialog pop-up relative to the
     *               frame (the parent).
     */
    private void intializeFrame(Component parent) {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                String[] options = new String[] { "Yes", "No" };
                if (JOptionPane.showOptionDialog(parent, "Are you sure you want to exit?", "Exit Tic Tac Toe",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]) == 0)
                    System.exit(0);
            }
        });
        this.setTitle("Tic Tac Toe");
        this.getContentPane().add(gridPanel, BorderLayout.CENTER);
        this.getContentPane().add(turnPanel, BorderLayout.SOUTH);
        this.setMinimumSize(new Dimension((int) SIDE_LENGTH, (int) SIDE_LENGTH));
        this.setPreferredSize(new Dimension((int) SIDE_LENGTH, (int) SIDE_LENGTH));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.validate();
        this.repaint();
        this.setVisible(true);
    }

    /**
     * Initializes the JButtons apart of the 2D-array.
     */
    private void initializeButtons() {
        buttons = new JButton[Board.SIZE][Board.SIZE];

        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                buttons[x][y] = new JButton();
                buttons[x][y].setBackground(Color.WHITE);
                buttons[x][y].setFont(new Font("Times New Roman", Font.PLAIN, X_O_SIZE));
                buttons[x][y].setFocusPainted(false);
                buttons[x][y].setName(x + "," + y);
                gridPanel.add(buttons[x][y]);

                // Required to setup an anonymous listener on the button
                final int xCopy = x;
                final int yCopy = y;

                // Register an anonymous listener on the button which notifies the controller
                // whenever a move is made (i.e. a button is clicked)
                buttons[x][y].addActionListener(e -> gameController.registerClick(xCopy, yCopy));
            }
        }
    }

    /**
     * Update the graphical representation of the Board.
     */
    private void updateView() {
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (board.getCharacter(x, y) == 'x') {
                    buttons[x][y].setText("X");
                    buttons[x][y].setEnabled(false);
                } else if (board.getCharacter(x, y) == 'o') {
                    buttons[x][y].setText("O");
                    buttons[x][y].setEnabled(false);
                } else
                    buttons[x][y].setText("");
            }
        }
    }

    /**
     * Enable or disable all of the buttons on the game board.
     * 
     * @param state True to enable all of the buttons, false to disable them.
     */
    private void enableButtons(boolean state) {
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                buttons[x][y].setEnabled(state);
            }
        }
    }

    /**
     * Update the turn to display the current player's turn.
     * 
     * @param turn True for X, false for O
     */
    private void updateTurnText(boolean turn) {
        turnDisplay.setText("It is " + (turn ? 'X' : 'O') + "'s turn.");
    }

    /**
     * Resets the game.
     */
    private void resetGame() {
        board.resetBoard();
        btnSave.setEnabled(true);
        enableButtons(true);
        updateTurnText(true);
        updateView();
    }

    /**
     * Save the current game.
     */
    private final boolean save(String path) {
        try {
            // Ensure that a file of that name does not already exist.
            if (new File(path).isFile()) {
                return false;
            }
            
            FileOutputStream file = new FileOutputStream(path); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
              
            out.writeObject(board); 
              
            out.close(); 
            file.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Load a previously saved game.
     */
    private final boolean load(String path) {
        try
        {    
            FileInputStream file = new FileInputStream(path); 
            ObjectInputStream in = new ObjectInputStream(file); 
               
            board = (Board)in.readObject(); 
              
            in.close(); 
            file.close(); 
            
            updateView();
            enableButtons(true);
            for (int x = 0; x < Board.SIZE; x++) 
                for (int y = 0; y < Board.SIZE; y++) 
                    if (board.getCharacter(x, y) != ' ') 
                        buttons[x][y].setEnabled(false);
            
            btnSave.setEnabled(true);
            this.board.addListener(this);
            this.gameController = new GameController(board);
            updateTurnText(board.getCurrentTurn());
            
            return true;
        } 
          
        catch(IOException ex)  { 
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        } 
    }

    /**
     * Handle any changes to the Board, and take appropriate action.
     */
    @Override
    public void handleBoardChange() {
        updateView();
        GameEvent gameState = board.boardState();
        switch (gameState.getStatus()) {
        case WINNER:
            turnDisplay.setText((gameState.getTurn() ? 'O' : 'X') + " wins.");
            enableButtons(false);
            btnSave.setEnabled(false);
            break;
        case DRAW:
            turnDisplay.setText("It's a draw!");
            btnSave.setEnabled(false);
            break;
        default:
            updateTurnText(gameState.getTurn());
        }
    }

    /**
     * Perform the appropriate action based on the menu button that was just
     * clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnReset)
            resetGame();
        else if (e.getSource() == btnSave) {
            int returnVal = fc.showSaveDialog(this);
            while(returnVal == JFileChooser.APPROVE_OPTION && !save(fc.getSelectedFile().getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "A file with that name already exists, please try again.", "Error saving file", JOptionPane.INFORMATION_MESSAGE);
                returnVal = fc.showSaveDialog(this);
            }
        } else {
            int returnVal = fc.showOpenDialog(this);
            while(returnVal == JFileChooser.APPROVE_OPTION && !load(fc.getSelectedFile().getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "The file was moved while loading occured, please try again.", "Error loading file", JOptionPane.INFORMATION_MESSAGE);
                returnVal = fc.showSaveDialog(this);
            }
        }
    }

    /**
     * Method stub required to make use of invokeLater()
     */
    @Override
    public void run() {
    }

    /**
     * Starts a game of Tic Tac Toe
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GameView(new Board()));
    }
}
