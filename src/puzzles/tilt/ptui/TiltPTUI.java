package puzzles.tilt.ptui;

/*
  The plain text UI for the Tilt puzzle.
  Uses the observer pattern to get notified when the model changes.
  The view is responsible for displaying the board and getting user input.

  @author Author V Mudundi
 */


import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.util.Arrays;
import java.util.Scanner;

public class TiltPTUI implements Observer<TiltModel, String> {
    private final TiltModel model;
    private final Scanner in;
    private boolean gameOn;

    /**
     * Constructor for the TiltPTUI.
     * @param model the model for the Tilt puzzle
     */
    public TiltPTUI(TiltModel model) {
        this.model = model;
        this.model.addObserver(this);
        gameOn = false;
        in = new Scanner( System.in );
    }


    /**
     * The observed subject calls this method on each observer that has
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server model can send to the observer
     *
     */
    @Override
    public void update(TiltModel model, String msg) {
        // Implementation to update the view based on model changes
        if(msg.equals(TiltModel.LOADED)) {
            System.out.println("Game Loaded");
            model.displayBoard();
            return;
        }else if(msg.equals(TiltModel.LOAD_FAILED)) {
            System.out.println("Load Failed");
            return;
        }else if(msg.startsWith(TiltModel.HINT_PREFIX)){
            System.out.println(msg);
            return;
        }
        if(model.gameOver()){
            model.displayBoard();
            System.out.println("You Win!");
            System.out.println();
            System.out.println("l(oad) filename     -- load new puzzle file");
            System.out.println("q(uit)              -- quit the game");
            System.out.println("r(eset)             -- reset the current game");
            return;
        }
        model.displayBoard();
        System.out.println(msg);
    }


    /**
     * The main loop for the TiltPTUI.
     */
    public void run() {
        String msg;
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("t(ilt) {N|S|E|W}    -- tilt the board in the given direction");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");
        while (gameOn) {
            msg = "";
            String[] command = in.nextLine().strip().split(" ");
            switch (command[0]) {
                case "q", "Q" -> {
                    System.out.println("Goodbye!");
                    gameOn = false;
                }
                case "h", "H" -> {
                    if (!model.gameOver()){
                        model.getHint();
                        //model.getHintDirection();
                    }
                    else {
                        msg = "Game is over, please r(eset) or l(oad) or q(uit)";
                    }
                }
                case "r", "R" -> model.reset();
                case "l", "L" -> {
                    try {
                        model.loadBoardFromFile(command[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        msg = "usage: l(oad) filename";
                    }
                }
                case "t", "T" -> {
                    if (!model.gameOver()) {
                        try {
                            model.tilt(command[1].charAt(0));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            msg = "usage: t(ilt) {N|S|E|W}";
                        }
                    } else {
                        msg = "Game is over, please r(eset) or l(oad) or q(uit)";
                    }
                }
                default -> msg = "Unknown command: " + command[0];
            }
            if(!msg.isEmpty()){
                System.out.println("Command: "+Arrays.toString(command)+"\n\033[0;1m***"+msg+"***\033[0;0m");
            }
        }
    }

    /**
     * The main method for the TiltPTUI.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Main method to start the game
        TiltModel model = new TiltModel();
        TiltPTUI view = new TiltPTUI(model);
        model.loadBoardFromFile(args[0]);
        view.gameOn = true;
        // Load initial board if provided in args, else default board
        view.run();
    }
}
