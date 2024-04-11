package puzzles.tilt.model;

/*
  This class represents a configuration of the Tilt puzzle.
  A configuration is a board with a certain number of green and blue balls.
  The goal of the puzzle is to move all the green balls to the holes.

  @param board the board of the puzzle
 * @param expected_count the number of green and blue balls
 * @param current_count the number of green and blue balls currently on the board
 *
 * @auther Ashrith V Mudundi
 */

import puzzles.common.solver.Configuration;

import java.util.*;

public class TiltConfig implements Configuration {
    private final Character[][] board;//[row][column]
    private final int[] expected_count;//[number of green=0, number of blue balls]
    private final int[] current_count = {0,0};//[number of green, number of blue balls]
    public TiltConfig(Character[][] board,int[] expected_count) {
        this.board = board;
        this.expected_count = expected_count;
        for (Character[] characters : this.board) {
            for (int j = 0; j < this.board.length; j++) {
                if (characters[j] == 'G') {
                    this.current_count[0]++;
                } else if (characters[j] == 'B') {
                    this.current_count[1]++;
                }
            }
        }
    }

    /**
     * Checks if the current configuration is a solution
     * @return true if the current configuration is a solution
     */
    @Override
    public boolean isSolution() {
        return this.current_count[0]==0;
    }

    /**
     * Checks if the current configuration is valid
     * @return true if the current configuration is valid
     */
    public boolean isValid(){
        return this.current_count[1]==this.expected_count[1];
    }

    /**
     * Returns the board of the current configuration
     * @return the board of the current configuration
     */
    public Character[][] getBoard() {
        return board;
    }

    /**
     * Returns the expected count of green and blue balls
     * @return the expected count of green and blue balls
     */
    public int[] getExpected_count() {
        return expected_count;
    }

    /**
     * Returns the arrangement of the board if tilted left
     * @param board the board to be tilted
     * @return the arrangement of the board if tilted left
     */
    public Character[][] tilt_left(Character[][] board){
        Character[][] new_board = new Character[board.length][board.length];
        for(int i=0;i<board.length;i++){
            System.arraycopy(board[i], 0, new_board[i], 0, board.length);
        }
        for(int i=0;i<board.length;i++){
            for(int j=1;j<board.length;j++){
                char current = new_board[i][j];
                if(current=='G' || current=='B'){
                    int temp=j;
                    while(temp>0 && new_board[i][temp-1]=='.'){
                        new_board[i][temp-1] = current;
                        new_board[i][temp] = '.';
                        temp--;
                    }
                    if(temp>0 && new_board[i][temp-1]=='O'){
                        new_board[i][temp]='.';
                    }
                }
            }
        }
        return new_board;
    }

    /**
     * Returns the arrangement of the board if tilted right
     * @param board the board to be tilted
     * @return the arrangement of the board if tilted right
     */
    public Character[][] tilt_right(Character[][] board){
        Character[][] new_board = new Character[board.length][board.length];
        for(int i=0;i<board.length;i++){
            System.arraycopy(board[i], 0, new_board[i], 0, board.length);
        }
        for(int i=0;i<board.length;i++){
            for(int j=board.length-2;j>=0;j--){
                char current = new_board[i][j];
                if(current=='G' || current=='B'){
                    int temp=j;
                    while(temp<board.length-1 && new_board[i][temp+1]=='.'){
                        new_board[i][temp+1] = current;
                        new_board[i][temp] = '.';
                        temp++;
                    }
                    if(temp<board.length-1 && new_board[i][temp+1]=='O'){
                        new_board[i][temp]='.';
                    }
                }
            }
        }
        return new_board;
    }

    /**
     * Returns the arrangement of the board if tilted up
     * @param board the board to be tilted
     * @return the arrangement of the board if tilted up
     */
    public Character[][] tilt_up(Character[][] board){
        Character[][] new_board = new Character[board.length][board.length];
        for(int i=0;i<board.length;i++){
            System.arraycopy(board[i], 0, new_board[i], 0, board.length);
        }
        for(int i=1;i<board.length;i++){
            for(int j=0;j<board.length;j++){
                char current = new_board[i][j];
                if(current=='G' || current=='B'){
                    int temp=i;
                    while(temp>0 && new_board[temp-1][j]=='.'){
                        new_board[temp-1][j] = current;
                        new_board[temp][j] = '.';
                        temp--;
                    }
                    if(temp>0 && new_board[temp-1][j]=='O'){
                        new_board[temp][j]='.';
                    }
                }
            }
        }
        return new_board;
    }

    /**
     * Returns the arrangement of the board if tilted down
     * @param board the board to be tilted
     * @return the arrangement of the board if tilted down
     */
    public Character[][] tilt_down(Character[][] board){
        Character[][] new_board = new Character[board.length][board.length];
        for(int i=0;i<board.length;i++){
            System.arraycopy(board[i], 0, new_board[i], 0, board.length);
        }
        for(int i=board.length-2;i>=0;i--){
            for(int j=0;j<board.length;j++){
                char current = new_board[i][j];
                if(current=='G' || current=='B'){
                    int temp=i;
                    while(temp<board.length-1 && new_board[temp+1][j]=='.'){
                        new_board[temp+1][j] = current;
                        new_board[temp][j] = '.';
                        temp++;
                    }
                    if(temp<board.length-1 && new_board[temp+1][j]=='O'){
                        new_board[temp][j]='.';
                    }
                }
            }
        }
        return new_board;
    }

    /**
     * Returns the possible valid next configurations from the current configuration
     * @return the possible valid next configurations from the current configuration
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new HashSet<>(List.of());//HashSet is used to avoid duplicates
        Character[][] temp_board = tilt_left(this.board);
        TiltConfig left_tiltConfig = new TiltConfig(temp_board, this.expected_count);
        if(left_tiltConfig.isValid()){
            neighbors.add(left_tiltConfig);
        }
        temp_board = tilt_right(this.board);
        TiltConfig right_tiltConfig = new TiltConfig(temp_board, this.expected_count);
        if(right_tiltConfig.isValid()){
            neighbors.add(right_tiltConfig);
        }
        temp_board = tilt_up(this.board);
        TiltConfig up_tiltConfig = new TiltConfig(temp_board, this.expected_count);
        if(up_tiltConfig.isValid()){
            neighbors.add(up_tiltConfig);
        }
        temp_board = tilt_down(this.board);
        TiltConfig down_tiltConfig = new TiltConfig(temp_board, this.expected_count);
        if(down_tiltConfig.isValid()){
            neighbors.add(down_tiltConfig);
        }
        return neighbors;
    }


    /**
     * Checks if the current configuration is equal to the other configuration
     * @param other the other configuration to be compared with
     * @return true if the current configuration is equal to the other configuration
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof TiltConfig otherTiltConfig){
            return Arrays.deepEquals(this.board, otherTiltConfig.board);
        }
        return false;
    }

    /**
     * Returns the hashcode of the current configuration
     * @return the hashcode of the current configuration
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**
     * Returns the string representation of the current configuration
     * @return the string representation of the current configuration
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Character[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                result.append(ints[j]);
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
