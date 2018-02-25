import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Author: Luke Gehman
 * Operates a game of Boggle, generating and updating the GUI and processing user input
 */
public class GameCoordinator extends Application{
    private Dictionary dictionary;
    private Tray tray;
    private ObservableList<Text> playerWordList;

    /**
     * The GameCoordinator constructor creates a new dictionary and tray to use in the game.
     */
    public GameCoordinator() {
        dictionary = new Dictionary();
        tray = new Tray();
        playerWordList = FXCollections.observableArrayList();
    }

    /**
     * Creates a new stage with a new scene based on the Parent node pass from createContent.
     * @param primaryStage The stage
     */
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createGUI());

        primaryStage.setTitle("Boggle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent createGUI(){
        Pane root = new Pane();
        BorderPane rootLayout = new BorderPane();

        StackPane centerPanel = new StackPane();
        centerPanel.getChildren().add(tray);

        VBox leftPanel = new VBox();
        TextField userInputField = createUserInputField();
        Button submitAllButton = createSubmitAllButton();
        Button addButton = createAddButton(userInputField);
        ListView<Text> playerWordsDisplay = new ListView<>(playerWordList);
        leftPanel.getChildren().addAll(playerWordsDisplay,userInputField,addButton,submitAllButton);

        rootLayout.setCenter(centerPanel);
        rootLayout.setLeft(leftPanel);
        rootLayout.setMargin(centerPanel,new Insets(20,20,20,20));
        rootLayout.setMargin(leftPanel,new Insets(20,20,20,20));

        root.getChildren().add(rootLayout);
        return root;
    }

    private TextField createUserInputField(){
        TextField textField = new TextField();
        textField.setPromptText("Write a word, then press enter");
        textField.setOnKeyPressed( event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(textField.getText().length() >0){
                    playerWordList.add(new Text(textField.getText()));
                    textField.clear();
                }
            }
        });
        return textField;
    }

    private Button createSubmitAllButton(){
        Button submitAllButton = new Button("Submit All");
        submitAllButton.setOnAction(event -> {
            for(Text t : playerWordList){
                if(isValid(t.getText())){
                    t.setFill(Color.BLUE);
                } else {
                    t.setFill(Color.RED);
                }
            }
        });
        return submitAllButton;
    }

    private Button createAddButton(TextField userInputField){
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            if(userInputField.getText().length() >0){
                playerWordList.add(new Text(userInputField.getText()));
                userInputField.clear();
            }
        });
        return addButton;
    }

    private boolean isValid(String word){
        return dictionary.contains(word) && tray.contains(word) && word.length()>2;
    }

    public void run(){
        launch();
    }

}
