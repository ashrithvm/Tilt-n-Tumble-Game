package puzzles.tipover.model;

// TODO: implement your TipOverConfig for the common solver

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.*;
public class TipOverConfig implements Configuration {

    private final Coordinates gridSize;
    public HashMap<Coordinates, TipOverConfig> board;
    private int height;
    private Coordinates towerCoords;
    private final Coordinates goal;

    public TipOverConfig(Coordinates gridSize, 
                         int height, int row, int col, Coordinates goal) {
        this.gridSize = gridSize;
        this.height = height;
        this.towerCoords = new Coordinates(row, col);
        this.goal = goal;
        this.board = new HashMap<>();
    }

    public Coordinates getGridSize() {
        return this.gridSize;
    }

    /**
     * @return height of tower
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return coordinates of tower
     */
    public Coordinates getTowerCoords() {
        return towerCoords;
    }

    @Override
    public boolean isSolution() {
        return this.towerCoords.compareTo(this.goal) == 0;
    }

    public void tipCoords(Coordinates newCoords, int newHeight) {
        this.towerCoords = newCoords;
        this.height = newHeight;
    }

    public TipOverConfig createTippedTower(Coordinates newCoords) {
        return new TipOverConfig(this.gridSize, 1, newCoords.row(),
                newCoords.col(), this.goal);
    }

    public boolean canTip(String direction) {
        if (this.height == 1) {
            return false;
        }

        Coordinates adjacent;

        switch(direction) {
            case "N" -> {
                adjacent = new Coordinates(this.towerCoords.row() - 1, this.towerCoords.col());
                if (board.containsKey(adjacent)) {
                    return false;
                }
            }
            case "S" -> {
                adjacent = new Coordinates(this.towerCoords.row() + 1, this.towerCoords.col());
                if (board.containsKey(adjacent)) {
                    return false;
                }
            }
            case "E" -> {
                adjacent = new Coordinates(this.towerCoords.row(), this.towerCoords.col() + 1);
                if (board.containsKey(adjacent)) {
                    return false;
                }
            }
            case "W" -> {
                adjacent = new Coordinates(this.towerCoords.row(), this.towerCoords.col() - 1);
                if (board.containsKey(adjacent)) {
                    return false;
                }
            }
        }

        return true;
    }

     /**
     * Method created to ensure that the dircetion that the tower is tipped is legal
     * @param direction
     * @return
     */
    public boolean isLegal(String direction) {
        switch(direction) {
            case "N" -> {
                if (board.containsKey(new Coordinates(this.towerCoords.row() - 1,
                        this.towerCoords.col()))) {
                    return true;
                } else {
                    if (this.towerCoords.row() - this.height < 0) {
                        return false;
                    }
                    for (int i = 1; i < height; i++) {
                        if (board.containsKey(new Coordinates(
                                this.towerCoords.row() - i, this.towerCoords.col()))) {
                            return false;
                        }
                    }
                }
            }
            case "S" -> {
                if (board.containsKey(new Coordinates(this.towerCoords.row() + 1,
                        this.towerCoords.col()))) {
                    return true;
                } else {
                    if (this.towerCoords.row() + this.height >= gridSize.row()) {
                        return false;
                    }
                    for (int i = 1; i < height; i++) {
                        if (board.containsKey(new Coordinates(
                                this.towerCoords.row() + i, this.towerCoords.col()))) {
                            return false;
                        }
                    }
                }
            }
            case "E" -> {
                if (board.containsKey(new Coordinates(this.towerCoords.row(),
                        this.towerCoords.col() + 1))) {
                    return true;
                } else {
                    if (this.towerCoords.col() + this.height >= gridSize.col()) {
                        return false;
                    }
                    for (int i = 1; i < height; i++) {
                        if (board.containsKey(new Coordinates(
                                this.towerCoords.row(), this.towerCoords.col() + i))) {
                            return false;
                        }
                    }
                }
            }
            case "W" -> {
                if (board.containsKey(new Coordinates(this.towerCoords.row(),
                        this.towerCoords.col() - 1))) {
                    return true;
                } else {
                    if (this.towerCoords.col() - this.height < 0) {
                        return false;
                    }
                    for (int i = 1; i < height; i++) {
                        if (board.containsKey(new Coordinates(
                                this.towerCoords.row(), this.towerCoords.col() - i))) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Collects all possible next moves
     * @return all neighbors of the tower
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();

        if (isLegal("N")) {
            if (board.containsKey(new Coordinates(this.towerCoords.row() - 1,
                    this.towerCoords.col()))) {
                neighbors.add(board.get(new Coordinates(this.towerCoords.row() - 1,
                        this.towerCoords.col())));
            } else {
                neighbors.add(new TipOverConfig(this.gridSize, 1, this.towerCoords.row() -
                        this.height, this.towerCoords.col(), this.goal));
            }
        }
        if (isLegal("S")) {
            if (board.containsKey(new Coordinates(this.towerCoords.row() + 1,
                    this.towerCoords.col()))) {
                neighbors.add(board.get(new Coordinates(this.towerCoords.row() + 1,
                        this.towerCoords.col())));
            } else {
                neighbors.add(new TipOverConfig(this.gridSize, 1, this.towerCoords.row() +
                        this.height, this.towerCoords.col(), this.goal));
            }
        }
        if (isLegal("E")) {
            if (board.containsKey(new Coordinates(this.towerCoords.row(),
                    this.towerCoords.col() + 1))) {
                neighbors.add(board.get(new Coordinates(this.towerCoords.row(),
                        this.towerCoords.col() + 1)));
            } else {
                neighbors.add(new TipOverConfig(this.gridSize, 1, this.towerCoords.row()
                        , this.towerCoords.col() + this.height, this.goal));
            }
        }
        if (isLegal("W")) {
            if (board.containsKey(new Coordinates(this.towerCoords.row(),
                    this.towerCoords.col() - 1))) {
                neighbors.add(board.get(new Coordinates(this.towerCoords.row(),
                        this.towerCoords.col() - 1)));
            } else {
                neighbors.add(new TipOverConfig(this.gridSize, 1, this.towerCoords.row()
                        , this.towerCoords.col() - this.height, this.goal));
            }
        }

        return neighbors;
    }

    /**
     * TipOverConfigs are to be equal if they are:
     * 1. In the same game (same grid dimensions with the towers and goal in the same coordinates)
     * 2. The same height
     * 3. Have the same coordinates
     * @param other: TipOverConfig to compare to this
     * @return Whether the two are equal or not
     */
    @Override
    public boolean equals(Object other) {
        if (this.getClass() == other.getClass()) {
            TipOverConfig o = (TipOverConfig) other;
            return (this.gridSize.equals(o.gridSize) && this.board.equals(o.board) &&
            this.height == o.height && this.towerCoords == o.towerCoords
                    && this.goal == o.goal);
        }
        return false;
    }

    /**
     * HashCode is the addition of the height, HashCode of the gridSize and HashCode of the tower's
     * coordinates
     * @return HashCode of TipOverConfig
     */
    @Override
    public int hashCode() {
        return this.height + this.gridSize.hashCode() + this.towerCoords.hashCode();
    }

    /**
     * Shows what the board looks like
     * @return Position of all towers and their heights
     */
    @Override
    public String toString() {
        String game = "";
        game += this.gridSize.col() + "x" + this.gridSize.row() + " grid.\n";
        game += "Currently at " + this.towerCoords + " with height " + this.height +
                ". Get to " + this.goal + "\n";
        for (int r = 0; r < gridSize.row(); r++) {
            for (int c = 0; c < gridSize.col(); c++) {
                Coordinates currCoords = new Coordinates(r, c);
                if (board.containsKey(currCoords)) {//adds the height of tower
                    game += this.board.get(currCoords).getHeight();
                } else {//puts 0 if no tower at currCoords
                    game += "0";
                }
                game += " ";
            }
            game += "\n";
        }

        return game;
    }
}
