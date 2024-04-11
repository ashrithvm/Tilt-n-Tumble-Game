package puzzles.tilt.model;

/*
  The model for the Tilt puzzle.
  Uses the observer pattern to notify the view when the model changes.
  The model is responsible for the logic of the game.
  The model is also responsible for loading the puzzle from a file.
  Uses TiltConfig to represent the current state of the puzzle.

  @author Ashrith V Mudundi
 */

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.*;

public class TiltModel {
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();
    public static String LOADED = "loaded";
    public static String LOAD_FAILED = "load failed";
    public static String HINT_PREFIX = "Hint:";

    private TiltConfig current_board;
    private TiltConfig backupBoard;

    /**
     * Returns the dimension of the board.
     * @return the dimension of the board
     */
    public int getDimension() {
        return current_board.getBoard().length;
    }

    /**
     * Add an observer to the list of observers.
     * @param observer the observer to add
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * Returns the message to be displayed.
     * @param data the data to be displayed
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Displays the board.
     */
    public void displayBoard(){
        System.out.println(current_board.toString());
    }

    /**
     * Loads the board from the specified file.
     * @param filename the name of the file to load the board from
     */
    public void loadBoardFromFile(String filename){
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            int size = Integer.parseInt(br.readLine());
            Character[][] board = new Character[size][size];
            int[] expected_count = new int[2];
            int i=0;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(" ");
                for(int j=0;j<size;j++){
                    board[i][j] = row[j].charAt(0);
                    if(board[i][j]=='B'){
                        expected_count[1]++;
                    }
                }
                i++;
            }
            current_board = new TiltConfig(board,expected_count);
            backupBoard = new TiltConfig(board,expected_count);
            alertObservers("Loaded: "+filename);
        } catch (IOException e) {
            alertObservers(LOAD_FAILED);
        }
    }

    /**
     * Resets the board to the initial state.
     */
    public void reset() {
        current_board = backupBoard;
        alertObservers("Board Reset.");
    }

    /**
     * Tilts the board in the specified direction.
     * @param c the direction to tilt the board in
     */
    public void tilt(char c) {
        // Implementation to tilt the board in the specified direction
        // Update currentBoard based on the tilt
        // Call alertObservers with appropriate message
        TiltConfig temp_board;
        if(c=='N' || c=='n'){
            temp_board = new TiltConfig(this.current_board.tilt_up(this.current_board.getBoard()),this.current_board.getExpected_count());
            if(temp_board.isValid()){
                this.current_board = temp_board;
                alertObservers("Tilted North");
            }
            else{
                alertObservers("Illegal move. Blue ball will fall into the hole");
            }
        }
        else if(c=='S' || c=='s'){
            temp_board = new TiltConfig(this.current_board.tilt_down(this.current_board.getBoard()),this.current_board.getExpected_count());
            if(temp_board.isValid()){
                this.current_board = temp_board;
                alertObservers("Tilted South");
            }
            else{
                alertObservers("Illegal move. Blue ball will fall into the hole");
            }
        }
        else if(c=='E' || c=='e'){
            temp_board = new TiltConfig(this.current_board.tilt_right(this.current_board.getBoard()),this.current_board.getExpected_count());
            if(temp_board.isValid()){
                this.current_board = temp_board;
                alertObservers("Tilted East");
            }
            else{
                alertObservers("Illegal move. Blue ball will fall into the hole");
            }
        }
        else if(c=='W' || c=='w'){
            temp_board = new TiltConfig(this.current_board.tilt_left(this.current_board.getBoard()),this.current_board.getExpected_count());
            if(temp_board.isValid()){
                this.current_board = temp_board;
                alertObservers("Tilted West");
            }
            else{
                alertObservers("Illegal move. Blue ball will fall into the hole");
            }
        }
        else {
            alertObservers("Invalid tilt");
        }
    }

    /**
     * Gives the next move as a hint.
     */
    public void getHint() {
        Solver solver = new Solver();
        Collection<Configuration> path = solver.solve(current_board);
        // Using an iterator to access the second configuration in the path
        Iterator<Configuration> iterator = path.iterator();
        iterator.next(); // Skip the current configuration
        Configuration nextStep = iterator.next(); // Get the next step
        // Display the next step as a hint
        alertObservers(HINT_PREFIX + "\n" + nextStep.toString());
    }

    /**
     * Gives the next move as a hint.
     * Moves the board to the next step and displays the hint.
     * This method is used for the GUI.
     */
    public void getHintDirection(){
        Solver solver = new Solver();
        Collection<Configuration> path = solver.solve(current_board);
        // Using an iterator to access the second configuration in the path
        try{
            Iterator<Configuration> iterator = path.iterator();
            iterator.next(); // Skip the current configuration
            Configuration nextStep = iterator.next(); // Get the next step
            // Display the next step as a hint
            TiltConfig temp_board = (TiltConfig) nextStep;
            TiltConfig temp_board2 = this.current_board;
            this.current_board = temp_board;
            if(Objects.deepEquals(temp_board.getBoard(),temp_board2.tilt_up(current_board.getBoard()))){
                alertObservers(HINT_PREFIX + "Tilt North");
            }
            else if(Objects.deepEquals(temp_board.getBoard(),temp_board2.tilt_down(current_board.getBoard()))){
                alertObservers(HINT_PREFIX+"Tilt South");
            }
            else if(Objects.deepEquals(temp_board.getBoard(),temp_board2.tilt_left(current_board.getBoard()))){
                alertObservers(HINT_PREFIX+"Tilt West");
            }
            else if(Objects.deepEquals(temp_board.getBoard(),temp_board2.tilt_right(current_board.getBoard()))){
                alertObservers(HINT_PREFIX+"Tilt East");
            }
            else{
                alertObservers("Hint: No hint available");
            }
        }
        catch (NoSuchElementException | NullPointerException e){
            alertObservers("Hint: No hint available");
        }

    }

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise
     */
    public boolean gameOver(){
        return this.current_board.isSolution();
    }

    /**
     * Main method to start the game.
     * @param args the command line arguments
     */
    public static void main(String[] args){
        TiltModel t = new TiltModel();
        t.displayBoard();
    }

    /**
     * Returns the tile at the specified row and column.
     * @param row the row of the tile
     * @param col the column of the tile
     * @return the tile at the specified row and column
     */
    public char getTileAt(int row, int col) {
        return current_board.getBoard()[row][col];
    }
}