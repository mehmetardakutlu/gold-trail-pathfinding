// Mehmet Arda Kutlu

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Handles reading data from map and objective files. Provides methods to read integers from the files.
 */
public class Reader {
    private File mapData;
    private File objectives;
    private Scanner mapReader;
    private Scanner objectiveReader;

    /**
     * Constructor of the class. File names are obtained from the console.
     * @param mapDataName name of the map data file
     * @param objectivesName name of the objectives file
     * @throws FileNotFoundException if any of the files cannot be found
     */
    Reader(String mapDataName, String objectivesName) throws FileNotFoundException {
        mapData = new File("misc/" + mapDataName);
        objectives = new File("misc/" + objectivesName);
        mapReader = new Scanner(mapData);
        objectiveReader = new Scanner(objectives);
    }

    /**
     * Reads the next integer from the map data file.
     * @return the next integer
     */
    public int readMap(){
        return mapReader.nextInt();
    }

    /**
     * Checks if there is more map data available to read.
     * @return true if there is more data, false otherwise
     */
    public boolean isMapInfoFinished(){
        return mapReader.hasNextInt();
    }

    /**
     * Reads the next integer from the objectives file.
     * @return the next integer
     */
    public int readObjectives(){
        return objectiveReader.nextInt();
    }

    /**
     * Checks if there is more objective data available to read.
     * @return true if there is more data, false otherwise
     */
    public boolean isObjectiveInfoFinished(){
        return objectiveReader.hasNextInt();
    }

    /**
     * Close both scanners to avoid unexpected behavior.
     */
    public void closeScanners(){
        mapReader.close();
        objectiveReader.close();
    }
}
