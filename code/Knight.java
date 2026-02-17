// Mehmet Arda Kutlu

/**
 * Represents the knight character on the map.
 * It stores the knight's current position using column and row indices.
 */
public class Knight {
    // The x coordinate of the knight on the map.
    private int col;
    // The y coordinate of the knight on the map.
    private int row;

    /**
     * Constructor of the class. Creates a knight object with given x and y coordinates.
     * @param col the x position
     * @param row the y position
     */
    Knight(int col, int row){
        this.col = col;
        this.row = row;
    }

    /**
     * Returns the knight's current x position.
     * @return the column index
     */
    public int getColNum() {
        return col;
    }

    /**
     * Sets the knight's x position.
     * @param colNum the new x index
     */
    public void setColNum(int colNum){
        this.col = colNum;
    }

    /**
     * Returns the knight's current y position.
     * @return the row index
     */
    public int getRowNum() {
        return row;
    }

    /**
     * Sets the knight's y position.
     * @param rowNum the new y index
     */
    public void setRowNum(int rowNum){
        this.row = rowNum;
    }
}
