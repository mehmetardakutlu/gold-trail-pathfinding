// Mehmet Arda Kutlu

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Implements the pathfinder algorithm. Provides methods that find the shortest path and the
 * cost of this path between two objectives.
 */
public class PathFinder {
    // Stores the travel cost between any two adjacent tiles using a string key.
    private HashMap<String, Double> travelCosts = new HashMap<>();

    /**
     * Constructs a PathFinder by loading movement costs from a file.
     * @param travelCostsName the name of travelCost.txt file (located in the "misc" directory)
     * @throws FileNotFoundException if the file is not found
     */
    PathFinder(String travelCostsName) throws FileNotFoundException {
        Locale.setDefault(Locale.US); // Setting the locale to US, otherwise I get an error on my Windows PC.
        File travelCostFile = new File("misc/" + travelCostsName);
        Scanner inputFile = new Scanner(travelCostFile);
        // Each line: x1 y1 x2 y2 cost
        while(inputFile.hasNextDouble()){
            int x1 = inputFile.nextInt();
            int y1 = inputFile.nextInt();
            int x2 = inputFile.nextInt();
            int y2 = inputFile.nextInt();
            double cost = inputFile.nextDouble();
            // Saving the costs in both directions.
            travelCosts.put(String.format("%d,%d,%d,%d",x1,y1,x2,y2),cost);
            travelCosts.put(String.format("%d,%d,%d,%d",x2,y2,x1,y1),cost);
        }
        inputFile.close();
    }

    // Returns the movement cost between two tiles, or -1 if one of the tile is impassable.
    public double costCalculator(Tile tile1, Tile tile2){
        String key = String.format("%d,%d,%d,%d",tile1.getColumn(),tile1.getRow(),tile2.getColumn(),tile2.getRow());
        return travelCosts.getOrDefault(key,-1.0);
    }

    /**
     * Uses Dijkstra's algorithm to compute the shortest path between two tiles.
     * @param start the starting tile
     * @param objective the target tile
     * @return an ArrayList of tiles representing the shortest path (from start to end),
     *         or an empty list if the objective is unreachable
     */
    public ArrayList<Tile> algorithm(Tile start, Tile objective){
        // Stores the cost to reach each tile.
        HashMap<Tile, Double> currentCost = new HashMap<>();
        // Stores the tile that came before each tile on the shortest path.
        HashMap<Tile, Tile> previousTile = new HashMap<>();
        // Priority queue to process tiles by lowest cost.
        PriorityQueue<Tile> path = new PriorityQueue<>(Comparator.comparingDouble(currentCost::get));
        currentCost.put(start,0.0); // Cost to reach start is 0.
        path.add(start);

        while(!path.isEmpty()){
            Tile currentTile = path.poll();
            // Exits the loop if the objective is reached.
            if(currentTile.isEqual(objective)){
                break;
            }

            for(Tile neighbor : currentTile.getAdjacentTiles()){
                // Skip impassable tiles.
                if(neighbor.getType() == 2){
                    continue;
                }

                double movingCost = costCalculator(currentTile, neighbor);

                // Skip impossible moves.
                if(movingCost < 0){
                    continue;
                }

                double newCost = currentCost.get(currentTile) + movingCost;

                // Update path and queue if a cheaper path is found.
                if(newCost < currentCost.getOrDefault(neighbor, Double.POSITIVE_INFINITY)){
                    currentCost.put(neighbor, newCost);
                    previousTile.put(neighbor, currentTile);
                    path.add(neighbor);
                }
            }
        }
        // If the objective is unreachable, return an empty path.
        if(!previousTile.containsKey(objective)){
            return new ArrayList<>();
        }

        // Reconstruct the path by walking backward from the objective.
        ArrayList<Tile> shortestPath = new ArrayList<>();
        Tile currentTile = objective;
        while(!currentTile.isEqual(start)){
            shortestPath.add(currentTile);
            currentTile = previousTile.get(currentTile);
        }
        shortestPath.add(start);
        Collections.reverse(shortestPath); // Convert path from (end -> start) to (start -> end).
        return shortestPath;
    }

    /**
     * Calculates the total cost of moving along a path.
     * @param path the list of tiles to traverse
     * @return the sum of movement costs between successive tiles
     */
    public double findTotalCost(ArrayList<Tile> path){
        double totalCost = 0;
        for(int i = 0; i < path.size() - 1; i++){
            double cost = costCalculator(path.get(i), path.get(i+1));
            totalCost += cost;
        }
        return totalCost;
    }
}
