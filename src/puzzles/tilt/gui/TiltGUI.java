package puzzles.tilt.gui;

/*
  The GUI for the Tilt puzzle.
  Uses JavaFX to display the board and get user input.
  The view is responsible for displaying the board and getting user input.

  @Author Ashrith V Mudundi
 */

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
import puzzles.tilt.model.TiltModel;

import java.io.File;
import java.util.List;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    private TiltModel model;
    private GridPane gridPane;
    private Label statusLabel;

    /**
     * Initialize the GUI.
     */
    @Override
    public void init() {
        List<String> args= getParameters().getRaw();
        String filename = args.get(0);
        model = new TiltModel(); // Your model initialization here
        model.loadBoardFromFile(filename); // Load the board file
        model.addObserver(this);
        gridPane = new GridPane();
    }

    /**
     * Start the GUI.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        // Create the GridPane for the game board with black borders
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Button btnLeft = new Button("<");
        Button btnRight = new Button(">");
        Button btnUp = new Button("^");
        Button btnDown = new Button("v");
        btnUp.setOnAction(e -> model.tilt('N'));
        btnDown.setOnAction(e -> model.tilt('S'));
        btnLeft.setOnAction(e -> model.tilt('W'));
        btnRight.setOnAction(e -> model.tilt('E'));
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

        updateGrid();
    }

    /**
     * Create the control pane with buttons for Load, Reset, and Hint.
     * @return the control pane
     */
    private VBox createControlPane() {
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(15));
        controls.setAlignment(Pos.CENTER);

        Button btnLoad = new Button("Load");
        Button btnReset = new Button("Reset");
        Button btnHint = new Button("Hint");

        btnLoad.setOnAction(this::handleLoad);
        btnReset.setOnAction(e -> model.reset());
        btnHint.setOnAction(e -> model.getHintDirection());

        controls.getChildren().addAll(btnLoad, btnReset, btnHint);

        return controls;
    }

    /**
     * Create the status pane with a label for messages.
     * @return the status pane
     */
    private HBox createStatusPane() {
        HBox statusPane = new HBox();
        statusPane.setAlignment(Pos.CENTER);
        statusLabel = new Label("Welcome to Tilt!");
        statusPane.getChildren().add(statusLabel);

        return statusPane;
    }

    /**
     * Handle the Load button.
     * @param event the event object
     */
    private void handleLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Puzzle File");
        fileChooser.setInitialDirectory(new File("data/tilt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            model.loadBoardFromFile("data/tilt/"+file.getName());
        }
    }

    /**
     * Update the grid with the current board.
     */
    private void updateGrid() {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);


        for (int row = 0; row < model.getDimension(); row++) {
            for (int col = 0; col < model.getDimension(); col++) {
                char tile = model.getTileAt(row, col);
                Image image = getTileImage(tile);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                gridPane.add(imageView, col, row);
            }
        }
    }

    /**
     * Get the image for the specified tile.
     * @param tile the tile
     * @return the image
     */
    private Image getTileImage(char tile) {
        String imagePath = switch (tile) {
            case 'B' -> "blue.png";
            case 'G' -> "green.png";
            case 'O' -> "hole.png";
            case '*' -> "block.png";
            case '.' -> "empty.png";
            default -> null;
        };
        return new Image("file:src/puzzles/tilt/gui/resources/" + imagePath);
    }

    /**
     * Update the view.
     * @param tiltModel the model
     * @param message the message
     */
    @Override
    public void update(TiltModel tiltModel, String message) {
        if (message.equals(TiltModel.LOADED)) {
            statusLabel.setText("Puzzle loaded successfully!");
        } else if (message.equals(TiltModel.LOAD_FAILED)) {
            statusLabel.setText("Failed to load puzzle.");
        } else {
            statusLabel.setText(message);
        }
        if(model.gameOver()){
            statusLabel.setText("Solved!");
        }
        updateGrid();
    }

    /**
     * Launch the GUI.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
