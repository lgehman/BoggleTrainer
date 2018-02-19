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

    public GameCoordinator() {
        dictionary = new Dictionary();
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

        Label inDictionary = new Label("In dictionary?:");
        Button checkButton = new Button("Submit Words");
        checkButton.setOnAction(event -> {
            if(dictionary.contains(textField.getText())){
                inDictionary.setText("In dictionary?: Yes");
            } else {
                inDictionary.setText("In dictionary?: No");
            }
        });

        buttonsAndLabels.getChildren().addAll(checkButton,inDictionary);

        rootLayout.setLeft(buttonsAndLabels);
        rootLayout.setCenter(new Tray());
        rootLayout.setRight(textField);

        root.getChildren().add(rootLayout);
        return root;
    }

    public void run(){
        launch();
    }



}
