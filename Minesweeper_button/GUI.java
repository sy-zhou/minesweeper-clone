import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import static javax.swing.GroupLayout.Alignment.*;
import java.util.*;
/**
 * GUI contains all the GUI needed for the minesweeper game.
 * 
 * @author Sally Zhou
 * @version 1.0 2018-03-16
 */
public class GUI
{
    // constants
    private static final String HEADING = "Minesweeper";
    private static final String TRADEMARK = "SYZ\u00a9";
    private static final String INSTRUCTIONS = "So like... just do it.";
    private static final int DIMENSION_OF_CELL = 20;
    private static final int BEGINNER = 0;
    private static final int INTERMEDIATE = 1;
    private static final int ADVANCED = 2;
    
    private static final int NUM_MINES_BEGINNER = 10;
    private static final int NUM_MINES_INTERMEDIATE = 40;
    private static final int NUM_MINES_ADVANCED = 99;
    
    private static final int HEIGHT_BEGINNER = 9;
    private static final int WIDTH_BEGINNER = 9;
    private static final int HEIGHT_INTERMEDIATE = 16;
    private static final int WIDTH_INTERMEDIATE = 16;
    private static final int HEIGHT_ADVANCED = 16;
    private static final int WIDTH_ADVANCED = 30;
    
    private static final int MINE_NUMBER = 9;
    private static final int FLAG_NUMBER = 10;
    private static final int UNOPENED_NUMBER = 11;

    // instance fields
    private JFrame game;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenu helpMenu;
    private JButton[][] cellButtons;
    private Cell[][] cells;
    private JLabel minesLeft;
    private JLabel timer;

    // images of tiles
    private ImageIcon[] images;
    
    // static fields
    private int numMines;
    private int numMinesLeft;
    private static int height;
    private static int width;
    private static int cellsUncovered;
    private static int time = 0;
    private TimerThread timerThread = new TimerThread();

    public GUI()
    {
        numMines = NUM_MINES_BEGINNER;
        numMinesLeft = NUM_MINES_BEGINNER;
        height = 9;
        width = 9;
        cellsUncovered = 0;
        cells = new Cell[width][height];
        initComponents();
    }

    public GUI(int difficulty)
    {
        setBoard(difficulty);
        cellsUncovered = 0;
        cells = new Cell[width][height];
        initComponents();
    }
    
    private void setBoard(int difficulty)
    {
        if (difficulty == BEGINNER)
        {
            numMines = NUM_MINES_BEGINNER;
            numMinesLeft = NUM_MINES_BEGINNER;
            width = WIDTH_BEGINNER;
            height = HEIGHT_BEGINNER;
        }
        else if (difficulty == INTERMEDIATE)
        {
            numMines = NUM_MINES_INTERMEDIATE;
            numMinesLeft = NUM_MINES_INTERMEDIATE;
            width = WIDTH_INTERMEDIATE;
            height = HEIGHT_INTERMEDIATE;
        }
        else if (difficulty == ADVANCED)
        {
            numMines = NUM_MINES_ADVANCED;
            numMinesLeft = NUM_MINES_ADVANCED;
            width = WIDTH_ADVANCED;
            height = HEIGHT_ADVANCED;
        }
    }

    private void initCells(int x, int y)
    {
        Random rand = new Random();
        ArrayList<Integer> rowMines = new ArrayList<>();
        ArrayList<Integer> colMines = new ArrayList<>();
        do
        {
            int tempRow = rand.nextInt(height);
            int tempCol = rand.nextInt(width);
            boolean exists = false;
            // check if prospective mine has the same coordinates as a previous mine (i.e. a mine already exists on that position)
            for (int i = 0; i < rowMines.size(); i++)
            {
                if (tempRow == rowMines.get(i) && tempCol == colMines.get(i))
                    exists = true;
            }
            // check if the prospective mine is in the cell of the user's click
            if (tempRow == x && tempCol == y)
                exists = true;
            // check if the prospective mine is in the cells surrounding the user's click
            for (int i = x - 1; i <= x + 1 && !exists; i++)
            {
                for (int j = y - 1; j <= y + 1 && !exists; j++)
                {
                    try
                    {
                        if (tempRow == i && tempCol == j)
                            exists = true;
                    }
                    catch (IndexOutOfBoundsException e) {}
                }
            }
            
            // if it passes previous checks, then add the new mine
            if (!exists)
            {
                rowMines.add(tempRow);
                colMines.add(tempCol);
            }
        } while (rowMines.size() < numMines);
        
        // iniitialize cells
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                cells[col][row] = new Cell();
            }
        }
        // set cells with the right statuses
        for (int i = 0; i < rowMines.size(); i++)
        {
            int currentCol = colMines.get(i);
            int currentRow = rowMines.get(i);
            cells[currentCol][currentRow].setStatus(MINE_NUMBER);
            // initialize surrounding cells
            for (int h = currentRow - 1; h <= currentRow + 1; h++)
            {
                for (int j = currentCol - 1; j <= currentCol + 1; j++)
                {
                    if (h != currentRow || j != currentCol)
                    {
                        try
                        {
                            cells[j][h].addToStatus();
                        }
                        catch (IndexOutOfBoundsException e) {}
                    }
                }
            }
        }
        
    }
    
    /*
     * Initializes all GUI components and formats the game board.
     */
    private void initComponents()
    {
        initImages();
        // set up formatting stuff
        Border paneBorder = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(8, 8, 8, 8));
        Border statsBorder = BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        Border fieldBorder = BorderFactory.createCompoundBorder(new EmptyBorder(8, 0, 0, 0), BorderFactory.createLoweredBevelBorder());
        
        game = new JFrame(HEADING);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel contentPane = (JPanel) game.getContentPane();
        contentPane.setBorder(paneBorder);
        contentPane.setLayout(new BorderLayout());
        
        initMenuBar();
        
        // set up game components
        minesLeft = new JLabel("" + numMinesLeft);
        timer = new JLabel ("" + time);
        timer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(1, 2));
        stats.add(minesLeft);
        stats.add(timer);
        stats.setBorder(statsBorder);
        
        JPanel field = new JPanel();
        field.setBorder(fieldBorder);
        field.setLayout(new GridLayout(height, width));

        cellButtons = new JButton[width][height];

        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                final int x = row;
                final int y = col;
                cellButtons[col][row] = new JButton();
                cellButtons[col][row].setIcon(images[UNOPENED_NUMBER]);
                
                cellButtons[col][row].setBorder(null);
                cellButtons[col][row].addMouseListener(new MinesweeperMouseListener(this, x, y));
                field.add(cellButtons[col][row]);
            }
        }

        JLabel trademark = new JLabel(TRADEMARK);
        trademark.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // adding everything to the frame
        contentPane.add(stats, BorderLayout.NORTH);
        contentPane.add(field, BorderLayout.CENTER);
        contentPane.add(trademark, BorderLayout.SOUTH);
        
        game.pack();
        game.setResizable(false);
        game.setVisible(true);
    }

    private void initImages()
    {
        images = new ImageIcon[12];
        
        images[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/opened.jpg")));
        images[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/one.jpg")));
        images[2] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/two.jpg")));
        images[3] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/three.jpg")));
        images[4] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/four.jpg")));
        images[5] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/five.jpg")));
        images[6] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/six.jpg")));
        images[7] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/seven.jpg")));
        images[8] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/eight.jpg")));
        images[9] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/mine.jpg")));
        images[10] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/flag.jpg")));
        images[11] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/unopened.jpg")));
    
        for (int index = 0; index < images.length; index++)
        {
            Image image = images[index].getImage();  
            Image newimg = image.getScaledInstance(DIMENSION_OF_CELL, DIMENSION_OF_CELL, java.awt.Image.SCALE_SMOOTH) ;  
            images[index] = new ImageIcon(newimg);
        }
    }
    
    private void initMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        helpMenu = new JMenu("Help");
        
        JMenuItem howToMenuItem = new JMenuItem("How To Play");
        howToMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    JOptionPane.showMessageDialog(null, INSTRUCTIONS);
                }
            });
        helpMenu.add(howToMenuItem);
        
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        game.setJMenuBar(menuBar);
    }
    
    public void uncover(int row, int col) throws IndexOutOfBoundsException
    {
        if (cells[0][0] == null) // first click
        {
            initCells(row, col);
            startTimer();
        }
        // do the things
        if (cells[col][row].isMine())
        {
            if (!cellButtons[col][row].getIcon().equals(images[FLAG_NUMBER]))
                mineTripped();
        }   
        else if (cells[col][row].isSafe())
        {
            cells[col][row].uncover();
            cellButtons[col][row].setIcon(images[cells[col][row].getStatus()]);
            //cellButtons[col][row].setEnabled(false);
            cellsUncovered++;
            uncoverAll(row, col);
        }
        else if (!cells[col][row].isUncovered())
        {
            cells[col][row].uncover();
            cellButtons[col][row].setIcon(images[cells[col][row].getStatus()]);
            //cellButtons[col][row].setEnabled(false);
            cellsUncovered++;
        }
        // check if all the necessary cells have been uncovered 
        checkWin();
    }

    /**
     * Double click to uncover surrounding cell
     */
    public void uncoverAll(int row, int col)
    {
        int numMinesSurrounding = cells[col][row].getStatus();
        int numMinesMarked = 0;
        boolean minesMarkedCorrectly = true;
        // see if all surrounding mines have been marked
        for (int i = row - 1; i <= row + 1; i++)
        {
            for (int j = col - 1; j <= col + 1; j++)
            {
                if (i != row || j != col)
                {
                    try
                    {
                        if (cellButtons[j][i].getIcon().equals(images[FLAG_NUMBER]))
                        {
                            numMinesMarked++;
                            if (!cells[j][i].isMine())
                                minesMarkedCorrectly = false;
                        }
                    }
                    catch (IndexOutOfBoundsException e) {}
                }
            }
        }
        // uncover surrounding cells
        if (minesMarkedCorrectly && numMinesMarked == numMinesSurrounding)
        {
            for (int i = row - 1; i <= row + 1; i++)
            {
                for (int j = col - 1; j <= col + 1; j++)
                {
                    if (i != row || j != col)
                    {
                        try
                        {
                            if (!cells[j][i].isMine() && !cells[j][i].isUncovered())
                            {
                                uncover(i, j);
                            }
                        }
                        catch (IndexOutOfBoundsException e) {}
                    }
                }
            }
        }
        else // the mines were not marked correctly
        {
            if (numMinesMarked == numMinesSurrounding) // the user has mismarked a mine
                mineTripped();
        }
    }

    public void markMine(int row, int col)
    {
        if (cellButtons[col][row].isEnabled())
        {
            if (cellButtons[col][row].getIcon().equals(images[UNOPENED_NUMBER]))
            {
                cellButtons[col][row].setIcon(images[FLAG_NUMBER]);
                decreaseMinesLeft();
            }
            else if (cellButtons[col][row].getIcon().equals(images[FLAG_NUMBER]))
            {
                cellButtons[col][row].setIcon(images[UNOPENED_NUMBER]);
                increaseMinesLeft();
            }
        }
    }

    private void mineTripped()
    {
        timerThread.interrupt();
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                JButton cell = cellButtons[col][row];
                if (cell.getIcon().equals(images[UNOPENED_NUMBER]) && cells[col][row].isMine())
                {
                    cell.setIcon(images[MINE_NUMBER]);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Game over. You tripped a mine!");
        endGame();
    }

    private void decreaseMinesLeft()
    {
        numMinesLeft = numMinesLeft - 1;
        minesLeft.setText("" + numMinesLeft);
    }
    
    private void increaseMinesLeft()
    {
        numMinesLeft = numMinesLeft + 1;
        minesLeft.setText("" + numMinesLeft);
    }
    
    private void startTimer()
    {
        timerThread.start();
    }
    
    private void checkWin()
    {
        if (cellsUncovered == (height * width - numMines))
        {
            timerThread.interrupt();
            JOptionPane.showMessageDialog(null, "Congratulations! You won!");
            endGame();
        }
    }
    
    private void endGame()
    {
        System.exit(0);
    }
    
    private class TimerThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                while (true)
                {
                    timer.setText("" + (++time));
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) {}
        }
        
    }
    
} // end of class GUI