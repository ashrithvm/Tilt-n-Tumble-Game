package puzzles.tipover.model;

import javafx.util.Pair;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.*;

public class TipOverModel {
    /** the collection of observers of this model */
    private final List<Observer<TipOverModel, String>> observers = new LinkedList<>();

    /** constants */
    public static String LOADED = "loaded";
    public static String LOAD_FAILED = "load failed";
    public static String HINT_PREFIX = "Hint:";

    /** the current configuration */
    private TipOverConfig currentConfig;
    private TipOverConfig backupConfig;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TipOverModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    public void displayBoard() {
        System.out.println(currentConfig.toString());
    }

    public boolean checkSolution(){
        if(currentConfig.isSolution()){
            alertObservers("You win!");
            return true;
        }
        return false;
    }

    public void changeAllBoard(HashMap<Coordinates, TipOverConfig> board) {
        for (Coordinates c : board.keySet()) {
            this.currentConfig.board.put(c, board.get(c));
        }
    }

    public void loadBoardFromFile(String filename){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            try {
                String line = reader.readLine();
                String[] settings = line.split(" ");
                int rows = Integer.parseInt(settings[0]);
                int columns = Integer.parseInt(settings[1]);
                Coordinates gridSize = new Coordinates(rows, columns);
                int towerRow = Integer.parseInt(settings[2]);
                int towerColumn = Integer.parseInt(settings[3]);
                int goalRow = Integer.parseInt(settings[4]);
                int goalColumn = Integer.parseInt(settings[5]);
                Coordinates goal = new Coordinates(goalRow, goalColumn);
                HashMap<Coordinates, TipOverConfig> board = new HashMap<>();
                boolean breakLoop = true;
                int r = 0;
                int towerHeight = 0;
                while (breakLoop) {
                    line = reader.readLine();
                    String[] temp = line.split(" ");
                    for (int c = 0; c < columns; c++) {
                        if (temp[c].equals("")) {
                            breakLoop = false;
                            break;
                        }
                        int height = Integer.parseInt(temp[c]);
                        if (height > 0) {
                            Coordinates currCoords = new Coordinates(r, c);
                            TipOverConfig curr = new TipOverConfig(gridSize,
                                    height, r, c, goal);
                            board.put(currCoords, curr);
                        }
                        if (towerRow == r && towerColumn == c) {
                            towerHeight = height;
                        }
                    }
                    r++;
                }
                this.currentConfig = new TipOverConfig(gridSize, towerHeight,
                        towerRow, towerColumn, goal);
                changeAllBoard(board);
                this.backupConfig = this.currentConfig;
                System.out.println(LOADED + filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(LOAD_FAILED);
            System.out.println("File not found");
        }
    }

    public HashMap<Coordinates, TipOverConfig> tipped(String axis, int sign) {
        HashMap<Coordinates, TipOverConfig> board = this.currentConfig.board;
        Coordinates currCoords = currentConfig.getTowerCoords();
        if (axis.equals("x")) {
            int i = 1;
            while (i <= currentConfig.getHeight()) {
                Coordinates newCoords = new Coordinates(currCoords.row(), currCoords.col() + i * sign);
                TipOverConfig newTower = currentConfig.createTippedTower(newCoords);
                board.put(newCoords, newTower);
                i++;
            }
        } else if (axis.equals("y")) {
            int i = 1;
            while (i <= currentConfig.getHeight()) {
                Coordinates newCoords = new Coordinates(currCoords.row() + i * sign, currCoords.col());
                TipOverConfig newTower = currentConfig.createTippedTower(newCoords);
                board.put(newCoords, newTower);
                i++;
            }
        }

        board.remove(currentConfig.getTowerCoords());
        return board;
    }

    public void move(String direction) {
        HashMap<Coordinates, TipOverConfig> newBoard = new HashMap<>();
        Coordinates newCoords;
        if (currentConfig.isLegal(direction)) {
            switch (direction) {
                case "N" -> {
                    if (!currentConfig.canTip("N")) {
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row() - 1,
                                currentConfig.getTowerCoords().col());
                        if (this.currentConfig.board.containsKey(newCoords)) {
                            this.currentConfig.tipCoords(newCoords,
                                    this.currentConfig.board.get(newCoords).getHeight());
                            alertObservers("Moved over north!");
                        }
                    } else {
                        newBoard = tipped("y", -1);
                        changeAllBoard(newBoard);
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row() - currentConfig.getHeight(),
                                currentConfig.getTowerCoords().col());
                        this.currentConfig.tipCoords(newCoords, 1);
                        alertObservers("Tipped over north!");
                    }
                }
                case "S" -> {
                    if (!currentConfig.canTip("S")) {
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row() + 1,
                                currentConfig.getTowerCoords().col());
                        if (this.currentConfig.board.containsKey(newCoords)) {
                            this.currentConfig.tipCoords(newCoords,
                                    this.currentConfig.board.get(newCoords).getHeight());
                            alertObservers("Moved over north!");
                        }
                    } else {
                        newBoard = tipped("y", 1);
                        changeAllBoard(newBoard);
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row() + currentConfig.getHeight(),
                                currentConfig.getTowerCoords().col());
                        this.currentConfig.tipCoords(newCoords, 1);
                        alertObservers("Tipped over south!");
                    }
                }
                case "E" -> {
                    if (!currentConfig.canTip("E")) {
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row(),
                                currentConfig.getTowerCoords().col() + 1);
                        alertObservers("Moved over north!");
                        if (this.currentConfig.board.containsKey(newCoords)) {
                            this.currentConfig.tipCoords(newCoords,
                                    this.currentConfig.board.get(newCoords).getHeight());
                        }
                    } else {
                        newBoard = tipped("x", 1);
                        changeAllBoard(newBoard);
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row(),
                                currentConfig.getTowerCoords().col() + currentConfig.getHeight());
                        this.currentConfig.tipCoords(newCoords, 1);
                        alertObservers("Tipped over east!");
                    }
                }
                case "W" -> {
                    if (!currentConfig.canTip("W")) {
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row(),
                                currentConfig.getTowerCoords().col() - 1);
                        alertObservers("Moved over west!");
                        if (this.currentConfig.board.containsKey(newCoords)) {
                            this.currentConfig.tipCoords(newCoords,
                                    this.currentConfig.board.get(newCoords).getHeight());
                        }
                    } else {
                        newBoard = tipped("x", -1);
                        changeAllBoard(newBoard);
                        newCoords = new Coordinates(
                                currentConfig.getTowerCoords().row(),
                                currentConfig.getTowerCoords().col() - currentConfig.getHeight());
                        this.currentConfig.tipCoords(newCoords, 1);
                        alertObservers("Tipped over west!");
                    }
                }
                default -> {
                    alertObservers("Invalid move");
                }
            }
        } else {
            alertObservers("Illegal move!");
        }
    }

    public Configuration getHint() {
        Solver solver = new Solver();
        Collection<Configuration> path = solver.solve(this.currentConfig);
        //Finds the next path in the configuartion
        Iterator<Configuration> iterator = path.iterator();
        iterator.next();
        TipOverConfig nextStep = (TipOverConfig) iterator.next();

//        if (nextStep == null) {
//            alertObservers("Game is not solvable in it's current state");
//            return null;
//        }
        //Displays the hint
        alertObservers(HINT_PREFIX + "\n" + nextStep.getTowerCoords());

        return nextStep;
    }

    public void moveHint() {
        TipOverConfig nextStep = (TipOverConfig) getHint();
        if (nextStep == null) {
            reset();
            alertObservers("No solutions! Board reset");
        } else {
            Coordinates nextCoords = new Coordinates(nextStep.getTowerCoords().row(),
                    nextStep.getTowerCoords().col());

            Coordinates currCoords = this.currentConfig.getTowerCoords();
            int complexDiff = (int)(Math.atan2(nextCoords.row() - currCoords.row(),
                    nextCoords.col() - currCoords.col()) * 180 / Math.PI);
            switch (complexDiff) {
                case 0:
                    alertObservers("Applied hint");
                    move("E");
                case 90:
                    alertObservers("Applied hint");
                    move("N");
                case 180:
                    alertObservers("Applied hint");
                    move("W");
                case 270:
                    alertObservers("Applied hint");
                    move("S");
            }
        }
    }

    /**
     * Checks to see if the game has been won or not
     * @return Whether the game is finished or not
     */
    public boolean gameOver() {
        return this.currentConfig.isSolution();
    }

    public void reset() {
        this.currentConfig = this.backupConfig;
        alertObservers("Board reset");
    }

    public ArrayList<Integer> getDimension() {
        ArrayList<Integer> dimensions = new ArrayList<>();
        dimensions.add(this.currentConfig.getGridSize().row());
        dimensions.add(this.currentConfig.getGridSize().col());

        return dimensions;
    }

    public int getTileAt(int row, int col) {
        return this.currentConfig.board.get(new Coordinates(row, col)).getHeight();
    }

    public static void main(String[] args){
        TipOverModel toc = new TipOverModel();
        toc.loadBoardFromFile(args[0]);
        System.out.println();
        toc.displayBoard();
    }
}
