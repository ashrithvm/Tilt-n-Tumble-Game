package puzzles.tilt.solver;

/*
  Program to solve the Tilt puzzle.
  Performs a breadth-first search of the puzzle space.
  The puzzle is read from a file whose name is given as a command-line argument.

  Usage: java Tilt filename
  @ author Ashrith V Mudundi
 */

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;

public class Tilt {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {
            BufferedReader reader= new BufferedReader(new FileReader(args[0]));
            try {
                String line = reader.readLine();
                int size = Integer.parseInt(line);
                Character[][] board = new Character[size][size];
                int[] expected_count = new int[2];
                int i = 0;
                while (line != null) {
                    line = reader.readLine();
                    if(line!=null) {
                        String[] temp = line.split(" ");
                        for (int j = 0; j < size; j++) {
                            board[i][j] = temp[j].charAt(0);
                            if (board[i][j] == 'B') {
                                expected_count[1]++;
                            }
                        }
                        i++;
                    }
                }
                TiltConfig tiltConfig = new TiltConfig(board, expected_count);
                Solver solver = new Solver();//solver object
                Collection<Configuration> path = solver.solve(tiltConfig);//path from start to finish
                System.out.println("File: " + args[0]);
                System.out.println(tiltConfig);
                System.out.println("Total configs: " + solver.getTotal());
                System.out.println("Unique configs: " + solver.getUnique());
                if (path != null) {
                    int temp= 0;
                    for (Configuration config : path) {
                        System.out.println("Step "+temp+":");
                        System.out.println(config.toString());
                        temp++;
                    }
                } else {
                    System.out.println("No solution");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
