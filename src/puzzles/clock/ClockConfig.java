package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.List;

/**
 * Main class for the clock puzzle.
 * <p>
 *     This class should implement a solver for the puzzle.
 *     It takes the number of hours in the clock, the starting hour, and the finish hour as
 *     parameters to the constructor.
 *     Uses Breadth First Search to find the solution.
 * </p>
 * @author Ashrith V. Mudundi
 */
public class ClockConfig implements Configuration {
    private final int hours;
    private final int current;
    private final int end;

    /**
     * Constructor for the ClockConfig class.
     * @param hours Total number of hours in the clock.
     * @param current Current hour.
     * @param end Finish hour.
     */
    public ClockConfig(int hours, int current, int end) {
        this.hours = hours;
        this.current = current;
        this.end = end;
    }

    /**
     * Checks if this configuration is the solution.
     * @return true if this configuration is the solution, false otherwise.
     */
    @Override
    public boolean isSolution() {
        // Implement your logic to check if this configuration is the solution.
        return this.end == this.current;
    }

    /**
     * Returns a collection of neighboring configurations from the current configuration.
     * @return a collection of neighboring configurations from the current configuration.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        // Returns a collection of neighboring hours from the current hour. there are two neighbors
        // for each hour. For example, if the current hour is 3, the neighbors are 2 and 4.
        // The current hour can never be equal to 0 or hours + 1.
        if(this.current == 1){//if the current hour is 1
            return List.of(new ClockConfig(this.hours, this.hours, this.end), new ClockConfig(this.hours, this.current+1, this.end));
        }
        if(this.current == this.hours){//if the current hour is the last hour
            return List.of(new ClockConfig(this.hours, this.current-1, this.end), new ClockConfig(this.hours, 1, this.end));
        }
        return List.of(new ClockConfig(this.hours, this.current-1, this.end), new ClockConfig(this.hours, this.current+1, this.end));
    }

    /**
     * Checks if this configuration is equal to another configuration.
     * @param other The other configuration to compare to.
     * @return true if this configuration is equal to the other configuration, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        // Implement equals method.
        if(other instanceof ClockConfig otherClockConfig){
            return this.current == otherClockConfig.current;
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
        return current;
    }

    /**
     * Returns a string representation of this configuration.
     * @return a string representation of this configuration.
     */
    @Override
    public String toString() {
        // Returns path from start to finish as a string.
        return this.current + "";

    }
}