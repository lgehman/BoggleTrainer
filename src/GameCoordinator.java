import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameCoordinator extends Application{
    private Dictionary dictionary;
    private Tray tray;

    public GameCoordinator() {
        dictionary = new Dictionary();
        tray = new Tray();
    }

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

        Label validWord = new Label("Valid Word?:");
        Button checkButton = new Button("Check");
        checkButton.setOnAction(event -> {
            if(dictionary.contains(textField.getText()) && tray.contains(textField.getText())){
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
