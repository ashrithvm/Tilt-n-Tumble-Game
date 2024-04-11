package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;

import java.io.File;
import java.util.List;

public class TipOverGUI extends Application implements Observer<TipOverModel, String> {
    private TipOverModel model;
    private GridPane gridPane;
    private Label statusLabel;
    @Override
    public void init() {
        List<String> args= getParameters().getRaw();
        String filename = args.get(0);
        model = new TipOverModel(); // Your model initialization here
        model.loadBoardFromFile(filename); // Load the board file
        model.addObserver(this);
        gridPane = new GridPane();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Button btnLeft = new Button("<");
        Button btnRight = new Button(">");
        Button btnUp = new Button("^");
        Button btnDown = new Button("v");
        btnUp.setOnAction(e -> model.move("N"));
        btnDown.setOnAction(e -> model.move("S"));
        btnLeft.setOnAction(e -> model.move("W"));
        btnRight.setOnAction(e -> model.move("E"));
        btnLeft.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        btnRight.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        HBox layout = new HBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(btnLeft, gridPane, btnRight);

        VBox layout2 = new VBox(10);
        layout2.setAlignment(Pos.CENTER);
        layout2.setPadding(new Insets(10));

        layout2.getChildren().addAll(btnUp, layout, btnDown);

        borderPane.setCenter(layout2);
        borderPane.setRight(createControlPane());
        borderPane.setTop(createStatusPane());

        Scene scene = new Scene(borderPane, 800, 700);
        primaryStage.setTitle("Tilt Puzzle Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        //updateGrid();
    }

    private VBox createControlPane() {
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(15));
        controls.setAlignment(Pos.CENTER);

        Button btnLoad = new Button("Load");
        Button btnReset = new Button("Reset");
        Button btnHint = new Button("Hint");

        btnLoad.setOnAction(this::handleLoad);
        btnReset.setOnAction(e -> model.reset());
        btnHint.setOnAction(e -> model.moveHint());

        controls.getChildren().addAll(btnLoad, btnReset, btnHint);

        return controls;
    }

    private void handleLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Puzzle File");
        fileChooser.setInitialDirectory(new File("data/tipover"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            model.loadBoardFromFile("data/tipover/" + file.getName());
        }
    }

    private HBox createStatusPane() {
        HBox statusPane = new HBox();
        statusPane.setAlignment(Pos.CENTER);
        statusLabel = new Label("Welcome to Tilt!");
        statusPane.getChildren().add(statusLabel);

        return statusPane;
    }

    private void updateGrid() {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);


        for (int row = 0; row < model.getDimension().get(0); row++) {
            for (int col = 0; col < model.getDimension().get(1); col++) {
                String tile = Integer.toString(model.getTileAt(row, col));
                Label curr = new Label(tile);
                gridPane.add(curr, col, row);
            }
        }
    }

    @Override
    public void update(TipOverModel tipOverModel, String message) {
        if (message.equals(tipOverModel.LOADED)) {
            statusLabel.setText("Puzzle loaded successfully!");
        } else if (message.equals(tipOverModel.LOAD_FAILED)) {
            statusLabel.setText("Failed to load puzzle.");
        } else {
            statusLabel.setText(message);
        }
        if(model.gameOver()){
            statusLabel.setText("Solved!");
        }
        updateGrid();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
