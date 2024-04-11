package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Arrays;
import java.util.Collection;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Ashrith V. Mudundi
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            // TODO YOUR MAIN CODE HERE
            int desired = Integer.parseInt(args[0]);//desired amount of water to be collected
            int[] buckets = new int[args.length-1];//the capacities of the N available buckets
            for(int i = 1; i < args.length; i++){
                buckets[i-1] = Integer.parseInt(args[i]);
            }
            int[] start = new int[buckets.length];
            WaterConfig waterConfig = new WaterConfig(desired, buckets, start);
            Solver solver = new Solver();
            Collection<Configuration> path = solver.solve(waterConfig);//path from start to finish
            System.out.println("Amount: " + desired + ", Buckets: " + Arrays.toString(buckets));
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