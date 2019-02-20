import javax.swing.*;
import java.awt.event.*;
/**
 * MinesweeperMouseListener is a MouseListener implemented specifically for use with minesweeper buttons.
 * 
 * @author Sally Zhou
 * @version 1.0 2018-03-16
 */
public class MinesweeperMouseListener extends MouseAdapter {
    // instance fields
    private GUI minesweeper;
    private int row;
    private int col;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isBothPressed;

    // constructor //

    public MinesweeperMouseListener(GUI minesweeper, int row, int col)
    {
        this.minesweeper = minesweeper;
        this.row = row;
        this.col = col;
        isLeftPressed = false;
        isRightPressed = false;
        isBothPressed = false;
    }

    // methods //
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            isLeftPressed = true;
        }
        else if (SwingUtilities.isRightMouseButton(e))
        {
            isRightPressed = true;
        }

        if (isLeftPressed && isRightPressed)
        {
            isBothPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            isLeftPressed = false;
            if (isBothPressed)
            {
                if (!isRightPressed)
                {
                    isBothPressed = false;
                    minesweeper.uncoverAll(row, col);
                }
            }
            else // only left button was pressed
            {
                minesweeper.uncover(row, col);
            }
        }
        else if (SwingUtilities.isRightMouseButton(e))
        {
            isRightPressed = false;
            if (isBothPressed)
            {
                if (!isLeftPressed)
                {
                    isBothPressed = false;
                    minesweeper.uncoverAll(row, col);
                }
            }
            else // only right button was pressed
            {
                minesweeper.markMine(row, col);
            }
        }
    }
    
} // end of class MinesweeperMouseListener