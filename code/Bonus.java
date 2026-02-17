// Mehmet Arda Kutlu

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bonus {
    public static void main(String[] args) throws IOException {
        // Store whether the -draw flag is used.
        boolean willDraw = false;
        // Ensure the files names are received correctly regardless of the usage of the -draw flag.
        int argsIndex = 0;
        String mapDataName;
        String travelCostsName;
        String objectivesName;

        // Check whether the draw flag is used, adjust willDraw.
        if(args.length > 0 && args[0].equals("-draw")){
            willDraw = true;
            argsIndex += 1;
        }
        // Obtain required file names from terminal.
        mapDataName = args[argsIndex];
        argsIndex += 1;
        travelCostsName = args[argsIndex];
        argsIndex += 1;
        objectivesName = args[argsIndex];

        // Create the Reader object.
        Reader reader = new Reader(mapDataName, objectivesName);

        // Create the Map object.
        Map map = new Map(reader.readMap(), reader.readMap());

        // Fill the tile array of the map with Tile objects using the information from the map data file.
        while(reader.isMapInfoFinished()){
            int colNum = reader.readMap();
            int rowNum = reader.readMap();
            map.fillTiles(colNum, rowNum ,new Tile(colNum, rowNum, reader.readMap()));
        }
        map.adjacentFinder(); // Find adjacent tiles for every tile in the map.

        // Create the Knight object.
        Knight knight = new Knight(reader.readObjectives(), reader.readObjectives());
        map.setKnight(knight);

        // Crate the PathFinder object.
        PathFinder pathFinder = new PathFinder(travelCostsName);

        // Fill the coin array of the map with Coin objects using the information from the objectives file.
        while(reader.isObjectiveInfoFinished()){
            int objectiveCol = reader.readObjectives();
            int objectiveRow = reader.readObjectives();
            map.addCoin(new Coin(objectiveCol, objectiveRow));
        }

        // Close the Scanner objects to avoid unexpected behavior.
        reader.closeScanners();

        // Convert the 2D tile array to a list format that ShortestRoute can process.
        List<Tile> tileList = new ArrayList<>();
        for(Coin coin : map.getCoins()){
            tileList.add(map.coinIsOn(coin));
        }

        // Create the ShotestRoute object.
        ShortestRoute shortestRoute = new ShortestRoute();
        // Compute the shortest path that the knight can follow.
        List<Tile> shortestPath = shortestRoute.findShortestTour(map.knightIsOn(knight),tileList,pathFinder);

        // Store whether all the coins are unreachable.
        boolean isAllUnreachable = shortestPath.isEmpty();

        // Create AlgorithmRunner objective.
        AlgorithmRunner algorithmRunner = new AlgorithmRunner(map,pathFinder,new Writer("out/bonus.txt"));

        // Set the canvas if the -draw flag is used and there is at least one reachable objective (coin).
        if(willDraw && !isAllUnreachable){
            int canvasWidth = map.getCanvasWidth();
            int canvasHeight = map.getCanvasHeight();
            StdDraw.setCanvasSize(canvasWidth, canvasHeight);
            StdDraw.setXscale(0.0, canvasWidth);
            StdDraw.setYscale(0.0, canvasHeight);
            StdDraw.enableDoubleBuffering();
            map.draw();
            StdDraw.pause(500);
        }

        // Run the algorithm (the bonus part instead of the regular one).
        algorithmRunner.runBonus(shortestPath, willDraw, isAllUnreachable);
        algorithmRunner.write(String.format("Total Step: %d, Total Cost: %.2f\n",
                algorithmRunner.getTotalStep(), algorithmRunner.getTotalCost()));

        // Close BufferedWriter object to avoid unexpected behavior.
        algorithmRunner.closeBuffer();
    }
}
