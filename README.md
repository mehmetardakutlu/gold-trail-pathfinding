# Gold Trail: The Knight's Path

A grid-based pathfinding and optimization project implemented in Java using the StdDraw library. This project consists of two distinct parts: the **Standard Part** (Sequential Pathfinding) and the **Bonus Part** (Traveling Salesman Problem Solver).

## Visualization
<img width="748" height="750" alt="Ekran görüntüsü 2025-05-11 153500" src="https://github.com/user-attachments/assets/1b3d1072-57cc-4b81-82e4-646ee31002ac" />


## Project Modes

### 1. Standard Mode: Sequential Pathfinding
The core assignment focuses on calculating the shortest path between the knight and specific gold coins on a weighted grid.
* **Goal:** Navigate from the knight's starting position to a target coin (or series of coins) individually.
* **Terrain System:** The map features different terrain types with specific movement costs :
    * **Grass:** Low cost (1-5 units).
    * **Sand:** Medium cost (8-10 units).
    * **Obstacles:** Impassable walls.
* **Algorithm:** Implements **Dijkstra's Algorithm** to determine the most efficient route between two points based on movement costs.

### 2. Bonus Mode: TSP Solver (Global Optimization)
The advanced implementation solves the **Traveling Salesperson Problem (TSP)** to find the absolute optimal route for collecting **all** accessible gold coins and returning to the start.
* **Goal:** Compute the path with the **minimum total cost** to visit every reachable coin and return to the initial position.
* **Complexity:** Unlike the standard mode which finds local shortest paths, this mode calculates the global optimum.
* **Algorithm:** Utilizes **Bitmask Dynamic Programming (DP)** with state compression to handle path permutations efficiently for up to 20 objectives.

## Project Structure
The source code is organized to separate the standard logic from the bonus implementation:

* **`code/`**:
    * **`Main.java`**: Runs the Standard Mode.
    * **`Bonus.java`**: Runs the Bonus Mode.
    * **`AlgorithmRunner.java`**: Manages the execution of both the standard and bonus pathfinding algorithms.
    * **`Coin.java`**: Stores coin properties.
    * **`Knight.java`**: Represents the knight character on the map.
    * **`Map.java`**: Provides methods that enable other classes to interact with map objects and draw the components.
    * **`PathFinder.java`**: Implements the Dijkstra's algorithm for the standard part.
    * **`Reader.java`**: Handles collecting information from input files.
    * **`ShortestRoute.java`**: Implements Bitmask DP to solve the TSP.
    * **`Tile.java`**: Stores tile properties.
    * **`Writer.java`**: Saves the generated output to the output file. 
* **`report/`**:
    * **`MehmetArdaKutlu.pdf`**: Detailed report that involves the UML diagrams of the classes and explains the algortihms.
* **`misc/`**:
    * Contains assets (e.g. `sandTile.png`).
* **`stdlib.jar`**: The graphics library required for visualization.
* **`testCases.zip`**: Includes the input and expected output files.

## How to Run
To run the project, you need to compile the Java files including the `stdlib.jar` library. You can execute the modes using the commands below:

# Compile:
```bash
javac -d out -cp "localPath/stdlib.jar" code/*.java
```

# --- Standard Mode (Sequential Pathfinding) ---
# Run (use the -draw flag to enable visualization):
```bash
java -cp "out:localPath/stdlib.jar" Main -draw mapData.txt travelCosts.txt objectives.txt
```

# --- Bonus Mode (TSP Solver) ---
# Run (use the -draw flag to enable visualization):
```bash
java -cp "out:localPath/stdlib.jar" Bonus -draw mapData.txt travelCosts.txt objectives.txt
```

## Technologies & Algorithms
* **Language:** Java
* **Visualization:** StdDraw
* **Standard Algorithms:** * **Graph Theory:** Representing the grid as a weighted graph.
    * **Dijkstra's Algorithm:** Used for finding the shortest path on weighted terrains (Grass vs. Sand).
* **Bonus Algorithms:** * **Traveling Salesperson Problem (TSP):** Modeling the gold collection as a TSP optimization.
    * **Bitmask Dynamic Programming:** Utilizing bitmasks to represent visited states efficiently.

## Input Format
The program accepts input files (e.g., `input1.txt`) that define the map structure. The typical structure includes:
1.  **Grid Dimensions:** Width and Height of the map.
2.  **Map Matrix:** A grid of integers representing terrain types (e.g., `0` for obstacles, `1` for grass).
3.  **Coordinates:** Starting position of the knight and locations of the gold coins.

*Sample input files are provided in the `testCases.zip` file.*

## Detailed Documentation
For a comprehensive explanation of the algorithmic approach, please refer to the full project report:

[Read the Project Report (PDF)](report/MehmetArdaKutlu.pdf)
