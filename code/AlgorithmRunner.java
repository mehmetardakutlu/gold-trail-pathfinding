// Mehmet Arda Kutlu

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
/**
 * Manages the execution of both the standard and bonus
 * pathfinding algorithms. Provides methods that track total cost and steps, handle
 * drawing and writing output to the output file.
 */
public class AlgorithmRunner {
    // Total number of steps the knight has taken.
    private int totalStep;
    // Total cost of all movements.
    private double totalCost;
    // Map that stores the main elements (tiles, objectives, knight)
    private Map map;
    // PathFinder object to find the paths and calculate the costs.
    private PathFinder pathFinder;
    // Writer object that is used for writing output to the output file.
    private Writer writer;

    /**
     * Constructor of the class.
     * @param map the Map object that stores the environment
     * @param pathFinder the PathFinder to find paths and calculate movement costs
     * @param writer the Writer to write the output to the output file
     */
    AlgorithmRunner(Map map, PathFinder pathFinder, Writer writer){
        this.map = map;
        this.pathFinder = pathFinder;
        this.writer = writer;
        totalCost = 0;
        totalStep = 0;
    }

    /**
     * Checks whether all the objectives (coins) are unreachable.
     * @return true if all objectives are unreachable, false otherwise
     */
    public boolean isAllUnreachable(){
        for(Coin objective : map.getCoins()){
            ArrayList<Tile> checkPath = pathFinder.algorithm(map.knightIsOn(map.getKnight()),
                    map.coinIsOn(objective));
            if(!checkPath.isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * Runs the standard algorithm, which visits each objective one-by-one.
     * @param willDraw whether the -draw flag is used
     * @param isAllUnreachable whether all objectives are unreachable (used to skip drawing)
     * @throws IOException if writing to output fails
     */
    public void run(boolean willDraw, boolean isAllUnreachable) throws IOException {
        for(int i = 0; i < map.getCoins().size(); i++){
            // Store cost of every individual path.
            double pathCost = 0;
            // Find the shortest path for the current objective.
            ArrayList<Tile> objectivePath = pathFinder.algorithm(map.knightIsOn(map.getKnight()),
                    map.coinIsOn(map.getCoins().get(i)));
            // Skip the unreachable objectives.
            if(objectivePath.isEmpty()){
                writer.write(String.format("Objective %d cannot be reached!\n", i + 1));
                continue;
            }
            for(int j = 0; j < objectivePath.size() - 1; j++){
                // Process every step in the path.
                if(j == 0){
                    writer.write(String.format("Starting position: (%d, %d)\n",objectivePath.get(j).getColumn(), objectivePath.get(j).getRow()));
                }
                // Mark the current tile in the path.
                objectivePath.get(j).setMarked(true);
                // Move the knight.
                map.getKnight().setColNum(objectivePath.get(j+1).getColumn());
                map.getKnight().setRowNum(objectivePath.get(j+1).getRow());
                // Increment the total step number.
                totalStep += 1;
                // Collect the coin as collected when the knight reaches it.
                if(j == objectivePath.size() - 2){
                    map.getCoins().get(i).setCollected(true);
                }
                // Remove the marks of every tile in the path before the knight starts a new path.
                if(j == objectivePath.size() - 2){
                    for(Tile tile : objectivePath){
                        tile.setMarked(false);
                    }
                }
                // Update the moving cost for the path.
                pathCost += pathFinder.costCalculator(objectivePath.get(j),objectivePath.get(j+1));
                writer.write(String.format("Step Count: %d, move to (%d, %d). Total Cost: %.2f.\n",
                             j+1,objectivePath.get(j+1).getColumn(),objectivePath.get(j+1).getRow(), pathCost));
                if(i == map.getCoins().size() - 1 && j == objectivePath.size() - 2){
                    continue;
                }
                // Draw the map if -draw flag is used and there are at least one reachable objective.
                if(willDraw && !isAllUnreachable){
                    map.draw();
                    StdDraw.pause(200);
                }
            }
            // Update the total movement cost of every path.
            totalCost += pathCost;
            writer.write(String.format("Objective %s reached!\n",i + 1));
            if(!(i == map.getCoins().size() - 1)){
                if(willDraw && !isAllUnreachable){
                    StdDraw.pause(300); // Pause longer when the knight reaches an objective (coin).
                }
            }
        }
    }

    /**
     * Generates a random color used for different paths in bonus part.
     * @return a randomly generated color
     */
    public Color randomMarkColor(){
        Random random = new Random();
        return new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
    }

    /**
     * Runs the bonus part, which there is a complete shortest path that includes all the objectives.
     * @param shortestPath the full path covering all objectives and return to the initial position
     * @param willDraw whether the -draw flag is used
     * @param isAllUnreachable whether all objectives are unreachable (used to skip drawing)
     * @throws IOException if writing to output fails
     */
    public void runBonus(List<Tile> shortestPath, boolean willDraw, boolean isAllUnreachable) throws IOException {
        Color tileMarkColor = null;
        // Set an initial mark color.
        if(willDraw && !isAllUnreachable){
            tileMarkColor = randomMarkColor();
        }
        // ArrayList that stores remaining uncollected coins.
        ArrayList<Integer> coinNums = new ArrayList<>();
        for(int i = 0; i < map.getCoins().size(); i++){
            coinNums.add(i);
        }
        // Iterate through the shortest path.
        for(int i = 0; i < shortestPath.size() - 1; i++){
            // Increment the total step number.
            totalStep += 1;
            Tile currentTile = shortestPath.get(i);
            Tile nextTile = shortestPath.get(i+1);
            // Mark the tile that the knight passed.
            map.getTiles()[currentTile.getColumn()][currentTile.getRow()].setMarked(true);
            // Update the coordinates of the knight.
            map.getKnight().setColNum(nextTile.getColumn());
            map.getKnight().setRowNum(nextTile.getRow());
            // Update the total movement cost.
            totalCost += pathFinder.costCalculator(currentTile, nextTile);
            writer.write(String.format("Step Count: %d, move to (%d, %d). Total Cost: %.2f.\n",totalStep,
                    nextTile.getColumn(),nextTile.getRow(),totalCost));
            // Stores the index of the collected coin.
            // Remains as -1 if the knight didn't collect a coin in the current step.
            int remove = -1;
            boolean isCoinCollected = false;
            // Check whether a coin is collected.
            for(int j : coinNums){
                if(map.isKnightOnCoin(j)){
                    writer.write(String.format("Objective %d reached!\n", j + 1));
                    remove = j;
                    if(willDraw && !isAllUnreachable){
                        map.getCoins().get(j).setCollected(true);
                        isCoinCollected = true;
                    }
                    break;
                }
            }
            // Remove the index of the collected coin.
            if(remove >= 0){
                coinNums.remove(Integer.valueOf(remove));
            }
            // Draw the map if the -draw flag is used and there is a valid path.
            if(willDraw && !isAllUnreachable){
                currentTile.setMarked(true);
                currentTile.setMarkColor(tileMarkColor);
                map.drawBonus();
                StdDraw.pause(200);
                if(isCoinCollected){
                    tileMarkColor = randomMarkColor(); // Change the mark color for every coin collection.
                    StdDraw.pause(300); // Pause longer if a coin is collected.
                }
            }
        }
    }

    /**
     * Returns the total number of steps taken to write to the output file.
     * @return the total step count
     */
    public int getTotalStep() {
        return totalStep;
    }

    /**
     * Returns the total movement cost of the path.
     * @return the total cost value
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * closes the BufferedWriter of the Writer object to avoid unexpected behavior.
     * @throws IOException if closing fails
     */
    public void closeBuffer() throws IOException {
        writer.closeBuffer();
    }

    /**
     * Writes the given output string to the output file.
     * @param toWrite the string to write
     * @throws IOException if writing fails
     */
    public void write(String toWrite) throws IOException {
        writer.write(toWrite);
    }
}
