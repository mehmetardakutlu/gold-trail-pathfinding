// Mehmet Arda Kutlu

import java.util.*;

/**
 * Finds the shortest route that the knight can follow to collect all the coins
 * and return to starting position.
 */
public class ShortestRoute {

    /**
     * Default constructor for the class.
     */
    ShortestRoute(){
    }

    /**
     * Finds the shortest path that starts at the knight's position,
     * visits all objectives, and returns to the start.
     * @param start the starting Tile (Knight's initial position)
     * @param objectives list of objective tiles (coin positions)
     * @param pathFinder the PathFinder object used for shortest path calculations
     * @return an ArrayList of Tile objects representing the full path.
     */
    public ArrayList<Tile> findShortestTour(Tile start, List<Tile> objectives, PathFinder pathFinder) {
        ArrayList<Tile> allTiles = new ArrayList<>();
        allTiles.add(start); // First index is the starting point.

        // Filter out unreachable objectives
        ArrayList<Tile> reachableObjectives = new ArrayList<>();
        for (Tile objective : objectives) {
            ArrayList<Tile> testPath = pathFinder.algorithm(start, objective);
            if (!testPath.isEmpty()) {
                reachableObjectives.add(objective);
            }
        }

        if (reachableObjectives.isEmpty()) {
            return new ArrayList<>();
        }

        allTiles.addAll(reachableObjectives);
        int n = allTiles.size(); // Total number of tiles.

        double[][] costMatrix = new double[n][n]; // Stores travel cost between every tile pair.
        HashMap<String, ArrayList<Tile>> pathMatrix = new HashMap<>(); // Stores full paths between tiles.
        // Precompute the shortest paths and fill cost/path matrices.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                ArrayList<Tile> path = pathFinder.algorithm(allTiles.get(i), allTiles.get(j));
                if (path.isEmpty()) {
                    costMatrix[i][j] = Double.POSITIVE_INFINITY;
                } else {
                    costMatrix[i][j] = pathFinder.findTotalCost(path);
                    pathMatrix.put(i + "-" + j, path);
                }
            }
        }

        int END_STATE = (1 << n) - 1; // The state where all nodes are visited.
        double[][] dp = new double[n][1 << n]; // dp[i][state] = Min cost to reach tile i having visited 'state'.
        int[][] parent = new int[n][1 << n]; // parent[i][state] = Previous tile before i in optimal path.
        // Initialize table with infinity.
        for (double[] row : dp) Arrays.fill(row, Double.POSITIVE_INFINITY);
        dp[0][1] = 0; // Cost of starting at first tile is 0.

        // Filling the table.
        for (int state = 1; state < (1 << n); state += 2) { // Ensure start tile is always included.
            for (int last = 0; last < n; last++) {
                if ((state & (1 << last)) == 0) continue; // Skip already visited ones.
                for (int next = 0; next < n; next++) {
                    if ((state & (1 << next)) != 0) continue;
                    if (costMatrix[last][next] == Double.POSITIVE_INFINITY) continue;
                    int nextState = state | (1 << next);
                    double newCost = dp[last][state] + costMatrix[last][next];
                    if (newCost < dp[next][nextState]) {
                        dp[next][nextState] = newCost; // Update minimum cost
                        parent[next][nextState] = last; // Track path
                    }
                }
            }
        }

        // Find the best path that ends at any tile and returns to initial position.
        double minCost = Double.POSITIVE_INFINITY;
        int lastIndex = -1;
        for (int i = 1; i < n; i++) {
            if (costMatrix[i][0] == Double.POSITIVE_INFINITY) continue; // Can't return to start.
            double tourCost = dp[i][END_STATE] + costMatrix[i][0];
            if (tourCost < minCost) {
                minCost = tourCost;
                lastIndex = i;
            }
        }

        // If there are no valid paths, return an empty path.
        if (lastIndex == -1) {
            return new ArrayList<>();
        }

        // Reconstruct the sequence of visited tiles.
        ArrayList<Integer> nodeOrder = new ArrayList<>();
        int current = lastIndex;
        int state = END_STATE;
        while (current != 0) {
            nodeOrder.add(current); // Add current node
            int prev = parent[current][state]; // Backtrack
            state ^= (1 << current); // Remove current from state
            current = prev;
        }
        nodeOrder.add(0); // Add starting node
        Collections.reverse(nodeOrder); // Reverse to get the correct order
        nodeOrder.add(0); // Return to initial position

        // Build the complete path using the tile order and path matrix.
        ArrayList<Tile> fullTour = new ArrayList<>();
        for (int i = 0; i < nodeOrder.size() - 1; i++) {
            int from = nodeOrder.get(i);
            int to = nodeOrder.get(i + 1);
            ArrayList<Tile> segment = pathMatrix.get(from + "-" + to);
            if (segment == null || segment.isEmpty()) continue;
            if (i > 0) segment.remove(0); // Avoid repeating tiles
            fullTour.addAll(segment);
        }

        return fullTour;
    }
}
