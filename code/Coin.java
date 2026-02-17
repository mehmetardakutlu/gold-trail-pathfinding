// Mehmet Arda Kutlu

/**
 * Represents the objectives on the map.
 * Stores the position of the coin on the map and whether it is collected.
 */
public class Coin {
    // The x coordinate of the coin on the map.
    private final int col;
    // The y coordinate of the coin of the map.
    private final int row;
    // Whether the coin is collected (will be displayed on the screen).
    private boolean isCollected;

    /**
     * Constructor of the class. Creates a coin object with given x and y coordinates.
     * isCollected variable is set as false by default.
     * @param col the x position
     * @param row the y position
     */
    Coin(int col, int row){
        this.col = col;
        this.row = row;
    }

    /**
     * Returns the coin's x position.
     * @return the column index
     */
    public int getColNum() {
        return col;
    }

    /**
     * Returns the coin's y position.
     * @return the row index
     */
    public int getRowNum() {
        return row;
    }

    /**
     * Returns the current collection status of the coin (whether it is collected by the knight).
     * @return the collection status
     */
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Sets the collection status of the coin (whether it is collected by the knight).
     * @param isCollected the new collection status
     */
    public void setCollected(boolean isCollected){
        this.isCollected = isCollected;
    }
}
