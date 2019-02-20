
/**
 * Cell represents a cell in a minesweeper field.
 * 
 * @author Sally Zhou
 * @version 1.0 2018-03-16
 */
public class Cell
{
    // constants
    private static final int SAFE_NUMBER = 0;
    private static final int MINE_NUMBER = 9;
    
    // instance fields
    private int status;
    private boolean uncovered;
    
    // constructors //
    
    public Cell()
    {
        status = SAFE_NUMBER;
        uncovered = false;
    }
    
    public Cell(int num)
    {
        status = num;
        uncovered = false;
    }
    
    // accessors //
    
    /**
     * Returns the number of mines surrounding this Cell.
     * @return the number of mines surrounding this Cell.
     */
    public int getStatus()
    {
        return status;
    }
    
    /**
     * Returns whether this Cell is uncovered.
     * @return whether this Cell is uncovered.
     */
    public boolean isUncovered()
    {
        return uncovered;
    }
    
    /**
     * Returns whether this Cell is a mine.
     * @return whether this Cell is a mine.
     */
    public boolean isMine()
    {
        if (status >= MINE_NUMBER)
            return true;
        return false;
    }

    /**
     * Returns whether there are any mines surrounding this Cell.
     * @return whether there are any mines surrounding this Cell.
     */
    public boolean isSafe()
    {
        if (status == SAFE_NUMBER)
            return true;
        return false;
    }
    
    // mutators //
    
    public void addToStatus()
    {
        status = status + 1;
    }
    
    public void setStatus(int newStatus)
    {
        status = newStatus;
    }
    
    public void uncover()
    {
        this.uncovered = true;
    }
    
} // end of class Cell