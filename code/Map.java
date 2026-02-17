// Mehmet Arda Kutlu

import java.awt.*;
import java.util.ArrayList;

/**
 * Stores the primary elements (tiles, coins, and the knight).
 * Provides methods that enable other classes to interact with map objects and draw the components.
 */
public class Map {
    // Map size is x,y (col,row)
    private int col;
    private int row;
    // 2D tile array that makes finding adjacent tiles easier to store the Tile objects.
    private Tile[][] tiles;
    // ArrayList that stores the objectives (coins).
    private ArrayList<Coin> coins = new ArrayList<>();
    // The knight
    private Knight knight;
    // Width and height of the canvas, which are dynamic values adjusted with respect to the map size.
    private int canvasHeight;
    private int canvasWidth;

    /**
     * Constructor of the class. Defines the length of the tiles array and sets the canvas dimensions.
     * @param col number of tile columns
     * @param row number of tile rows
     */
    Map(int col, int row){
        this.col = col;
        this.row = row;
        this.tiles = new Tile[col][row];
        canvasWidth = col * 30;
        canvasHeight = row * 30;
    }

    /**
     * Returns the canvas width.
     * @return the width of the canvas
     */
    public int getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * Returns the canvas height.
     * @return the height of the canvas
     */
    public int getCanvasHeight() {
        return canvasHeight;
    }

    /**
     * Puts a Tile object to the tiles array which was initially empty.
     * @param colNum y index of the 2D array
     * @param rowNum x index of the 2D array
     * @param tile the Tile object that will be put
     */
    public void fillTiles(int colNum, int rowNum, Tile tile){
        tiles[colNum][rowNum] = tile;
    }

    /**
     * Returns the tiles array.
     * @return 2D tiles array
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Returns the Knight object of the map.
     * @return the Knight object of the map
     */
    public Knight getKnight() {
        return knight;
    }

    /**
     * Sets the Knight object of the map.
     * @param knight the Knight object which will be set. His position is obtained from objectives file.
     */
    public void setKnight(Knight knight) {
        this.knight = knight;
    }

    /**
     * Adds a Coin object to the coins ArrayList which was initially empty.
     * @param coin the Coin object that will be added
     */
    public void addCoin(Coin coin){
        coins.add(coin);
    }

    /**
     * Returns the coins ArrayList.
     * @return ArrayList coins that includes the objectives (coins).
     */
    public ArrayList<Coin> getCoins(){
        return coins;
    }

    /**
     * Finds the x center of an object (tile, coin or the knight) using the x coordinate of this
     * object in the map.
     * @param colNum the x coordinate of the object
     * @return the x center of the object which will be used to draw the object to the canvas
     */
    public double xCenterFinder(int colNum){
        int xInterval = canvasWidth / col;
        return xInterval / 2.0 + colNum * xInterval;
    }

    /**
     * Finds the y center of an object (tile, coin or the knight) using the y coordinate of this
     * object in the map.
     * @param rowNum the y coordinate of the object
     * @return the y center of the object which will be used to draw the object to the canvas
     */
    public double yCenterFinder(int rowNum){
        int yInterval = canvasHeight / row;
        return canvasHeight - yInterval / 2.0 - rowNum * yInterval;
    }

    /**
     * Checks all the tiles in the 2D array, finds the neighboring tiles by checking
     * the consecutive indices and adds them to adjacentTiles ArrayList.
     */
    public void adjacentFinder(){
        // Check every tile in the array using two nested for loops
        for(int i = 0; i < col; i++){
            for(int j = 0; j < row; j++){
                // Look for only valid indices
                if(i > 0){
                    tiles[i][j].addAdjacentTile(tiles[i-1][j]);
                }
                if(i < col - 1){
                    tiles[i][j].addAdjacentTile(tiles[i+1][j]);
                }
                if(j > 0){
                    tiles[i][j].addAdjacentTile(tiles[i][j-1]);
                }
                if(j < row - 1){
                    tiles[i][j].addAdjacentTile(tiles[i][j+1]);
                }
            }
        }
    }

    /**
     * Draws the components of the map to the canvas.
     */
    public void draw(){
        int xInterval = canvasWidth / col;
        int yInterval = canvasHeight / row;
        double xCenter;
        double yCenter;
        // Draw the tiles.
        for(Tile[] tileList : tiles){
            for(Tile tile : tileList){
                xCenter = xInterval / 2.0 + tile.getColumn() * xInterval;
                yCenter = canvasHeight - yInterval / 2.0 - tile.getRow() * yInterval;
                // Draw considering types using enhanced switch.
                switch(tile.getType()){
                    case 0 -> StdDraw.picture(xCenter,yCenter,"misc/grassTile.jpeg",xInterval,yInterval);
                    case 1 -> StdDraw.picture(xCenter,yCenter,"misc/sandTile.png",xInterval,yInterval);
                    case 2 -> StdDraw.picture(xCenter,yCenter,"misc/impassableTile.jpeg",xInterval,yInterval);
                }
                // If the algorithm marked the tile as on the path, it will be drawn
                // with a red circle at its center.
                if(tile.isMarked()){
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.filledCircle(xCenter,yCenter,5);
                }
            }
        }
        xCenter = xCenterFinder(knight.getColNum());
        yCenter = yCenterFinder(knight.getRowNum());

        // Draw the knight.
        StdDraw.picture(xCenter,yCenter,"misc/knight.png",xInterval,yInterval);

        // Draw the coins that are not marked as collected by the algorithm.
        for(Coin coin : coins){
            if(!coin.isCollected()){
                xCenter = xCenterFinder(coin.getColNum());
                yCenter = yCenterFinder(coin.getRowNum());
                StdDraw.picture(xCenter,yCenter,"misc/coin.png",xInterval,yInterval);
            }
        }
        // Show the contents.
        StdDraw.show();
    }

    /**
     * Checks if the knight encounters a coin when moving in the shortest path to collect all
     * reachable objectives (coins). Only used in the bonus part.
     * @param objectiveNum the index of the coin in coins ArrayList
     * @return true if the knight reached the coin, false otherwise
     */
    public boolean isKnightOnCoin(int objectiveNum){
        return knight.getColNum() == coins.get(objectiveNum).getColNum() &&
                knight.getRowNum() == coins.get(objectiveNum).getRowNum();
    }

    /**
     * Returns the tile that the knight is standing.
     * @param knight the Knight object
     * @return Tile object that the knight is standing
     */
    public Tile knightIsOn(Knight knight){
        return tiles[knight.getColNum()][knight.getRowNum()];
    }

    /**
     * Returns the tile that the given coin is sitting on.
     * @param coin the coin that will be looked for
     * @return Tile object that keeps the coin
     */
    public Tile coinIsOn(Coin coin){
        return tiles[coin.getColNum()][coin.getRowNum()];
    }

    /**
     * Draws the components of the map for the bonus part.
     * Key difference with the regular draw is showing the path that the knight follows.
     */
    public void drawBonus(){
        int xInterval = canvasWidth / col;
        int yInterval = canvasHeight / row;
        double xCenter;
        double yCenter;
        // Draw the tiles with respect to their types.
        for(Tile[] tileList : tiles){
            for(Tile tile : tileList){
                xCenter = xInterval / 2.0 + tile.getColumn() * xInterval;
                yCenter = canvasHeight - yInterval / 2.0 - tile.getRow() * yInterval;
                switch(tile.getType()){
                    case 0 -> StdDraw.picture(xCenter,yCenter,"misc/grassTile.jpeg",xInterval,yInterval);
                    case 1 -> StdDraw.picture(xCenter,yCenter,"misc/sandTile.png",xInterval,yInterval);
                    case 2 -> StdDraw.picture(xCenter,yCenter,"misc/impassableTile.jpeg",xInterval,yInterval);
                }
                // Unlike the regular draw,
                // every path between two objectives (coins) is drawn with a different color.
                if(tile.isMarked()){
                    StdDraw.setPenColor(tile.getMarkColor());
                    StdDraw.filledCircle(xCenter,yCenter,5);
                }
            }
        }
        xCenter = xCenterFinder(knight.getColNum());
        yCenter = yCenterFinder(knight.getRowNum());

        // Draw the knight.
        StdDraw.picture(xCenter,yCenter,"misc/knight.png",xInterval,yInterval);

        // Draw the coins which are not collected by the knight.
        for(Coin coin : coins){
            if(!coin.isCollected()){
                xCenter = xCenterFinder(coin.getColNum());
                yCenter = yCenterFinder(coin.getRowNum());
                StdDraw.picture(xCenter,yCenter,"misc/coin.png",xInterval,yInterval);
            }
        }
        // Show the contents.
        StdDraw.show();
    }
}
