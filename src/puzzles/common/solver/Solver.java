package puzzles.common.solver;

import java.util.*;

public class Solver {
    public int getTotal(){
        return total;
    }
    public int getUnique(){
        return unique;
    }
    // TODO
    private int total;
    private int unique;
    // This class should implement a solver for the puzzle.
    // It takes the starting value as a parameter to the constructor.
    // Uses Breadth First Search to find the solution
    // The solution is stored in a queue
    // Checks if the solution is equal to finish hour. i.e. 2.isSolution()
    // If not, it gets the neighbors of the current hour and adds them to the queue
    // It continues until it finds the solution
    // It returns the path to the solution as an array of numbers to the main class
    public Collection<Configuration> solve(Configuration initialConfig) {
        // Implement your breadth-first search algorithm here to find the shortest path.
        // You can use the Configuration interface to represent the puzzle's configuration.
        // You can use the Configuration's methods isSolution() and getNeighbors() to check
        // if the current configuration is a solution or to get the neighbors from the current
        // configuration.
        total=1;
        unique=1;
        Map<Configuration, Configuration> predecessorMap = new HashMap<>();
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(initialConfig);
        predecessorMap.put(initialConfig, null);
        while (!queue.isEmpty()) {
            Configuration current = queue.remove();
            if (current.isSolution()) {
                return backtrace(predecessorMap, current);
            }
            for (Configuration neighbor : current.getNeighbors()) {
                total++;
                if (!predecessorMap.containsKey(neighbor)) {
                    queue.add(neighbor);
                    unique++;
                    predecessorMap.put(neighbor, current);
                }
            }
        }
        return null;
    }

    private Collection<Configuration> backtrace(Map<Configuration, Configuration> predecessorMap, Configuration current) {
        List<Configuration> path = new ArrayList<>();
        path.add(current);
        while (predecessorMap.get(current) != null) {
            current = predecessorMap.get(current);
            path.add(0, current);
        }
        return path;
    }
}