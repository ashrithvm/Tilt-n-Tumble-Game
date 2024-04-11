package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Main class for the water buckets puzzle.
 * <p>
 *     This class should implement a solver for the puzzle.
 *     It takes the desired amount of water to be collected and the capacities of the buckets as
 *     parameters to the constructor.
 *     Uses Breadth First Search to find the solution.
 * </p>
 */
public class WaterConfig implements Configuration {
    private final int desired_amount;//desired amount of water to be collected
    private final int[] capacities;//the capacities of the N available buckets
    private final int[] current_amounts;//the current amount of water in each bucket

    /**
     * Constructor for the WaterConfig class.
     * @param desired the desired amount of water to be collected
     * @param buckets the capacities of the N available buckets
     * @param current_amounts the current amount of water in each bucket
     */
    public WaterConfig(int desired, int[] buckets, int[] current_amounts) {
        this.desired_amount = desired;
        this.capacities = buckets;
        this.current_amounts = current_amounts;
    }

    /**
     * Checks if this configuration is the solution.
     * @return true if this configuration is the solution, false otherwise.
     */
    @Override
    public boolean isSolution() {
        for (int bucket : this.current_amounts) {
            if (bucket == this.desired_amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a collection of neighboring configurations from the current configuration.
     * @return a collection of neighboring configurations from the current configuration.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        // Returns a collection of neighboring configurations from the current configuration.
        // For example, if the current configuration is [0, 0], the neighbors are [3, 0]
        // [0, 3], and [0, 5].
        Collection<Configuration> neighbors = new java.util.HashSet<>(List.of());//HashSet is used to avoid duplicates
        for(int i=0;i<this.capacities.length;i++) {
            if (current_amounts[i] != 0) {//if the current amount of water in the bucket is not 0
                int[] temp = this.current_amounts.clone();
                temp[i] = 0;
                neighbors.add(new WaterConfig(this.desired_amount, this.capacities, temp));
            }
            if (current_amounts[i] != this.capacities[i]) {//if the current amount of water in the bucket is not equal to the capacity of the bucket
                int[] temp = this.current_amounts.clone();
                temp[i] = this.capacities[i];
                neighbors.add(new WaterConfig(this.desired_amount, this.capacities, temp));
            }
            for (int j=0; j<this.capacities.length; j++) {//if the current amount of water in the bucket is not equal to the capacity of the bucket
                int[] temp = this.current_amounts.clone();
                int movable= Math.min(temp[i], this.capacities[j] - temp[j]);
                temp[i] -= movable;
                temp[j] += movable;
                neighbors.add(new WaterConfig(this.desired_amount, this.capacities, temp));
            }
        }
        return neighbors;
    }

    /**
     * Checks if this configuration is equal to another configuration.
     * @param other the other configuration to be compared to.
     * @return true if this configuration is equal to the other configuration, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        // Implement equals method.
        if(other instanceof WaterConfig otherWaterConfig){
            return Arrays.equals(current_amounts,otherWaterConfig.current_amounts);
        }
        return false;
    }

    /**
     * Returns the hash code of this configuration.
     * @return the hash code of this configuration.
     */
    @Override
    public int hashCode() {
        // Implement hashCode method.
        return Arrays.hashCode(current_amounts);
    }

    /**
     * Returns the path from start to finish as a string.
     * @return the path from start to finish as a string.
     */
    @Override
    public String toString() {
        // Returns path from start to finish as a string.
        return Arrays.toString(this.current_amounts);
    }
}