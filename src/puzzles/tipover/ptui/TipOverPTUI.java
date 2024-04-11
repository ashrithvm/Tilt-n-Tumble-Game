package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.model.TipOverModel;
import java.util.*;

public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model;
    private Scanner in;
    private boolean gameOn;

    public TipOverPTUI(TipOverModel model) {
        this.model = model;
        this.model.addObserver(this);
        gameOn = true;
        in = new Scanner(System.in);
    }

    @Override
    public void update(TipOverModel model, String message) {
        if(message.equals(TipOverModel.LOADED)) {
            System.out.println("Game Loaded");
            model.displayBoard();
            return;
        } else if(message.equals(TipOverModel.LOAD_FAILED)) {
            System.out.println("Load Failed");
            return;
        } else if(message.startsWith(TipOverModel.HINT_PREFIX)) {
            System.out.println(message);
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
        System.out.println(message);
    }

    public void run() {
        String msg = "";
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("m(ove) {N|S|E|W}    -- move the tipper in the given direction");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");
        while (gameOn) {
            msg = "";
            String[] command = in.nextLine().strip().split(" ");
            switch (command[0]) {
                case "q" -> {
                    System.out.println("Goodbye!");
                    gameOn = false;
                }
                case "h" -> {
                    if (!model.gameOver()){
                        model.getHint();
                    }
                    else {
                        msg = "Game is over, please r(eset) or l(oad) or q(uit)";
                    }
                }
                case "r" -> model.reset();
                case "l" -> {
                    try {
                        model.loadBoardFromFile("data/tipover/" + command[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        msg = "usage: l(oad) filename";
                    }
                }
                case "m" -> {
                    if (!model.gameOver()) {
                        try {
                            model.move(command[1]);
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
            } else {
                update(model, msg);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        } else {
            TipOverPTUI tiptui = new TipOverPTUI(new TipOverModel());
            tiptui.run();
        }
    }
}
