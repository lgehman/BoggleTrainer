import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Author: Luke Gehman
 * Operates a full, GUI based game of Boggle.
 */
public class GameCoordinator extends Application{
    private Dictionary dictionary;
    private Tray tray;
    private Button startNewGameButton;
    private ObservableList<Text> playerWordList;
    private IntegerProperty timeSeconds;
    private Timeline timeline;
    private int totalScore = 0;
    private int scoreThisRound = 0;
    private Label scoreThisRoundLabel;
    private Label totalScoreLabel;

    private final Integer GAME_TIME = 180;   //Seconds

    /**
     * The constructor sets up a new dictionary and tray (though initially the tray is not set with letters,
     * this only happens when set() is called in the startNewGame() method).
     */
    public GameCoordinator() {
        dictionary = new Dictionary();
        tray = new Tray();
        playerWordList = observableArrayList();
        timeSeconds = new SimpleIntegerProperty(GAME_TIME);
    }

    /**
     * Creates a new stage with a new scene based on the Parent node passed from createContent.
     * @param primaryStage The one and only stage used in this application
     */
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createGUI());
        primaryStage.setTitle("Boggle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Resets the score, clears the player's word list, sets the tray's letters, and sets up a timer to count down
     * from GAME_TIME. When this timer is up, gameOver() is called.
     */
    private void startNewGame(){
        scoreThisRound = 0;
        scoreThisRoundLabel.setText("Score: " + scoreThisRound);
        playerWordList.clear();
        tray.set();
        tray.clearWordAttempt();
        if(timeline != null){
            timeline.stop();
        }
        timeSeconds.set(GAME_TIME);
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(GAME_TIME+1),
                new KeyValue(timeSeconds, 0)));
        timeline.setOnFinished(event -> gameOver());
        timeline.playFromStart();
    }

    /**
     * Generates a BorderLayout GUI, with the left panel being used to hold a player's list of words,
     * the text field for inputting words, buttons to start/end the game, score information, and the timer.
     * The center panel holds the tray for the game.
     * @return The root node for the scene
     */
    private Parent createGUI(){
        Pane root = new Pane();
        BorderPane rootLayout = new BorderPane();

        StackPane centerPanel = new StackPane();
        centerPanel.getChildren().add(tray);

        VBox leftPanel = new VBox(5);

        totalScoreLabel = createTotalScoreLabel();
        scoreThisRoundLabel = createScoreThisRoundLabel();
        startNewGameButton = createStartNewGameButton();
        Label timerLabel = createTimerLabel();
        TextField userInputField = createUserInputField();
        Button submitAllButton = createSubmitAllButton();
        Button addButton = createAddButton(userInputField);
        ListView<Text> playerWordsDisplay = new ListView<>(playerWordList);

        HBox controlButtons = new HBox(5,addButton,submitAllButton,startNewGameButton);
        HBox scoreLabels = new HBox (100,scoreThisRoundLabel,totalScoreLabel);
        leftPanel.getChildren().addAll(scoreLabels,playerWordsDisplay,userInputField,controlButtons, timerLabel);

        rootLayout.setCenter(centerPanel);
        rootLayout.setLeft(leftPanel);
        BorderPane.setMargin(centerPanel,new Insets(20,20,20,20));
        BorderPane.setMargin(leftPanel,new Insets(20,20,20,20));

        root.getChildren().add(rootLayout);
        return root;
    }

    /**
     * @return A button which starts a new game of Boggle. Disabled when a current game is still ongoing.
     */
    private Button createStartNewGameButton(){
        Button startGameButton = new Button();
        startGameButton.setText("Start New Game");
        startGameButton.setOnAction((ActionEvent event) -> {
            startNewGame();
            startGameButton.setDisable(true);
        });
        return startGameButton;
    }

    /**
     * @return A label which displays the number of seconds remaining in red
     */
    private Label createTimerLabel(){
        Label timerLabel = new Label();
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setFont(Font.font(20));
        timerLabel.setTextFill(Color.RED);
        return timerLabel;
    }

    /**
     * @return A text field which submits words to the playerWordList upon pressing ENTER. Will only
     * submit a word if it is 3 or more characters long and not already on the list. Clears the text
     * field if the submission is a success.
     */
    private TextField createUserInputField(){
        TextField textField = new TextField();
        textField.setPromptText("Write a word, then press enter");
        textField.setOnKeyPressed( event -> {
            String word = textField.getText();
            if(event.getCode() == KeyCode.ENTER && word.length() > 2){
                for (Text t : playerWordList) {
                    if (t.getText().equals(word)) {
                        return;
                    }
                }
                playerWordList.add(new Text(word));
                textField.clear();
            }
        });
        return textField;
    }

    /**
     * @return A button which submits the player's word list to be checked (in other words,
     * ends this round of Boggle)
     */
    private Button createSubmitAllButton(){
        Button submitAllButton = new Button("Submit All");
        submitAllButton.setOnAction( (ActionEvent event) -> gameOver());
        return submitAllButton;
    }

    /**
     * @param userInputField The text field for the button to take words from
     * @return A button which adds a word from a particular text field to the
     * player's word list.
     */
    private Button createAddButton(TextField userInputField){
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            String word = tray.getWordAttemptAsString();
            if(word.length() > 2){
                if(!isInList(word)){
                    playerWordList.add(new Text(word));
                    tray.clearWordAttempt();
                }
            } else {
                word = userInputField.getText();
                if (word.length() > 2 && !isInList(word)) {
                    playerWordList.add(new Text(word));
                    userInputField.clear();
                }
            }
        });
        return addButton;
    }

    /**
     * @return A label which displays the score for the current round in blue
     */
    private Label createScoreThisRoundLabel(){
        Label scoreThisRoundLabel = new Label("Score: " + scoreThisRound);
        scoreThisRoundLabel.setFont(Font.font(20));
        scoreThisRoundLabel.setTextFill(Color.BLUE);
        return scoreThisRoundLabel;
    }

    /**
     * @return A label which displays the total score in blue
     */
    private Label createTotalScoreLabel(){
        Label totalScoreLabel = new Label("Total: " + totalScore);
        totalScoreLabel.setFont(Font.font(20));
        totalScoreLabel.setTextFill(Color.BLUE);
        return totalScoreLabel;
    }

    /**
     * @param word A string to check for validity on the tray and in the dictionary
     * @return True if this string is both on the tray and in the dictionary and 3 or more characters
     */
    private boolean isValid(String word){
        return dictionary.contains(word) && tray.contains(word) && word.length()>2;
    }

    /**
     * Checks if the string is in the player's list
     * @param word A string
     * @return True if it's in the player's list of guessed words
     */
    private boolean isInList(String word){
        for (Text t : playerWordList) {
            if (t.getText().equals(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The procedure for ending a round fo Boggle. Stops the timer, re-enables the start game button,
     * and goes through the player's list of words, checking them for validity and adding to the score
     * if they are valid.
     */
    private void gameOver(){
        startNewGameButton.setDisable(false);
        timeline.stop();
        for(Text t : playerWordList){
            String word = t.getText();
            if(isValid(word)){
                t.setFill(Color.BLUE);
                scoreThisRound += word.length()-2;
                totalScore += word.length()-2;
            } else {
                t.setFill(Color.RED);
            }
        }
        scoreThisRoundLabel.setText("Score: " + scoreThisRound);
        totalScoreLabel.setText("Total: " + totalScore);
    }

    /**
     * Launches this javafx application
     */
    public void run(){
        launch();
    }
}
