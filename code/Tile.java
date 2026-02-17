// Mehmet Arda Kutlu

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents each tile in the map. Stores the position, type and the adjacent tiles of each tile.
 * It also stores the marking status of the tile to show the paths, and the marking color for the bonus part.
 */
public class Tile {
    // x position of the tile in the map.
    private int column;
    // y position of the tile in the map.
    private int row;
    // Type of the tile (1 -> grass, 2 -> sand, 3 -> impassable)
    private int type;
    // ArrayList that stores the neighboring tiles of the tile.
    private ArrayList<Tile> adjacentTiles = new ArrayList<>();
    // Whether the knight passed the tile.
    private boolean isMarked;
    // The color of the mark (used in the bonus part).
    private Color markColor;

    /**
     * Constructor of the class. Creates a tile object at specified positions with the specified type.
     * @param column x position of the tile in the map
     * @param row y position of the tile in the map
     * @param type type of the tile (1 -> grass, 2 -> sand, 3 -> impassable)
     */
    Tile(int column, int row, int type){
        this.column = column;
        this.row = row;
        this.type = type;
    }

    /**
     * Returns the x position of the tile.
     * @return the column index
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the y position of the tile.
     * @return the row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the type of the tile.
     * @return 0 if the tile is a grass tile, 1 if it is sand, 2 if it is impassable
     */
    public int getType() {
        return type;
    }

    /**
     * Checks if the provided tile is the same with the tile by comparing their properties.
     * @param tile the tile that will be compared
     * @return true if the tiles are the same, false otherwise
     */
    public boolean isEqual(Tile tile) {
        return column == tile.getColumn() && row == tile.getRow() && type == tile.getType();
    }

    /**
     * Adds a neighboring tile to the adjacentTiles ArrayList.
     * @param tile the tile that will be added
     */
    public void addAdjacentTile(Tile tile){
        adjacentTiles.add(tile);
    }

    /**
     * Returns the neighboring tiles of the tile.
     * @return ArrayList that involves the neighboring tiles
     */
    public ArrayList<Tile> getAdjacentTiles() {
        return adjacentTiles;
    }

    /**
     * Returns the marking status of the tile.
     * @return true if the tile is marked, which means that it is on the knight's path, false otherwise
     */
    public boolean isMarked(){
        return isMarked;
    }

    /**
     * Sets the marking status of the tile.
     * @param marked the new marking status
     */
    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    /**
     * Returns the color of the circular mark of the tile. Only used in the bonus part.
     * (In the standard part, all the marks are red.)
     * @return color of the mark
     */
    public Color getMarkColor(){
        return markColor;
    }

    /**
     * Sets the mark color of the tile. Only used in the bonus part.
     * @param markColor the new color of the mark
     */
    public void setMarkColor(Color markColor) {
        this.markColor = markColor;
    }
}
