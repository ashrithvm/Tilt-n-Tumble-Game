package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;

/**
 * Main class for the clock puzzle.
 *
 * @author Ashrith V. Mudundi
 */
public class Clock {
    /**
     * Run an instance of the clock puzzle.
     *
     * @param args [0]: the number of hours in the clock;
     *             [1]: the starting hour;
     *             [2]: the finish hour.
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(("Usage: java Clock hours start finish"));
        } else {
            // TODO YOUR MAIN CODE HERE
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);

            ClockConfig clockConfig = new ClockConfig(hours, start, end);
            Solver solver = new Solver();
            Collection<Configuration> path = solver.solve(clockConfig);

            System.out.println("Hours: " + hours + ", Start: " + start + ", End: " + end);
            System.out.println("Total configs: " + solver.getTotal());
            System.out.println("Unique configs: " + solver.getUnique());
            if (path != null) {
                int temp= 0;
                for (Configuration config : path) {
                    System.out.println("Step "+temp+":"+config.toString());
                    temp++;
                }
            } else {
                System.out.println("No solution");
            }
        }
    }
}