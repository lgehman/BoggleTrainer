import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Author: Luke Gehman
 * Operates a game of Boggle, generating and updating the GUI and processing user input
 */
public class GameCoordinator extends Application{
    private Dictionary dictionary;
    private Tray tray;

    /**
     * The GameCoordinator constructor creates a new dictionary and tray to use in the game.
     */
    public GameCoordinator() {
        dictionary = new Dictionary();
        tray = new Tray();
    }

    /**
     * Creates a new stage with a new scene based on the Parent node pass from createContent.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Boggle");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private Parent createContent(){
        Pane root = new Pane();
        BorderPane rootLayout = new BorderPane();

        TextField textField = new TextField();
        textField.setPromptText("Write a word to check");

        VBox buttonsAndLabels = new VBox();
        buttonsAndLabels.setPrefWidth(120);

        Label validWord = new Label("Valid Word?:");
        Button checkButton = new Button("Check");
        checkButton.setOnAction(event -> {
            String word = textField.getText();
            if(dictionary.contains(word) && tray.contains(word) && word.length()>2){
                validWord.setText("Valid Word?: Yes");
            } else {
                validWord.setText("Valid Word?: No");
            }
        });

        buttonsAndLabels.getChildren().addAll(checkButton,validWord);

        rootLayout.setLeft(buttonsAndLabels);
        rootLayout.setCenter(tray);
        rootLayout.setRight(textField);

        root.getChildren().add(rootLayout);
        return root;
    }

    public void run(){
        launch();
    }



}
