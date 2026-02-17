// Mehmet Arda Kutlu

import java.io.*;

public class Main {
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

        // Create new AlgorithmRunner object.
        AlgorithmRunner algorithmRunner = new AlgorithmRunner(map, pathFinder, new Writer("out/output.txt"));
        boolean isAllUnreachable = algorithmRunner.isAllUnreachable();

        // Prepare the canvas if -draw flag is used and there is at least one reachable objective.
        if(!isAllUnreachable && willDraw){
            int canvasWidth = map.getCanvasWidth();
            int canvasHeight = map.getCanvasHeight();
            StdDraw.setCanvasSize(canvasWidth, canvasHeight);
            StdDraw.setXscale(0.0, canvasWidth);
            StdDraw.setYscale(0.0, canvasHeight);
            StdDraw.enableDoubleBuffering();
        }

        // Draw the map for the first time and wait for a while before the animation begins.
        if(!isAllUnreachable && willDraw){
            map.draw();
            StdDraw.pause(500);
        }

        // Run the algorithm, write the output to output.txt
        algorithmRunner.run(willDraw, isAllUnreachable);
        algorithmRunner.write(String.format("Total Step: %d, Total Cost: %.2f\n",
                              algorithmRunner.getTotalStep(), algorithmRunner.getTotalCost()));

        // Close BufferedWriter object to avoid unexpected behavior.
        algorithmRunner.closeBuffer();

        // Draw the final state of the map (where all the reachable coins are collected).
        if(!isAllUnreachable && willDraw){
            map.draw();
        }
    }
}
