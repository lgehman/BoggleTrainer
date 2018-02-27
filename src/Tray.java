import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.util.*;

/**
 * Author: Luke Gehman
 * Holds a 2D array of Tiles, each displaying a letter. Can check to see if a particular string is on the
 * tray with the contains() method. Tiles can be 'selected' on user left click, or unselected on user right
 * click. Selecting a tile adds to a word attempt, which the tray keeps track of.
 */
public class Tray extends Pane {

    private final int TRAY_HEIGHT = 5;
    private final int TRAY_WIDTH = 5;
    private final int TILE_SIZE = 105;
    private final int LETTER_MAX = 4;
    private int[] letterCounts;
    private Tile[][] board;
    private LinkedList<Tile> wordAttempt;

    /**
     * Sets up a tray with tiles all initially set to display '.'. Calling set() later will
     * assign letters to these tiles.
     */
    public Tray(){
        board = new Tile[TRAY_HEIGHT][TRAY_WIDTH];
        wordAttempt = new LinkedList<>();
        setPrefSize(TRAY_WIDTH*TILE_SIZE,TRAY_HEIGHT*TILE_SIZE);
        generateTiles();
    }

    /**
     * Clears the board of previous letters and generates a new board state (assignment of a letter
     * to each tile)
     */
    public void set(){
        for(Tile[] ts : board){
            for(Tile t : ts){
                t.setLetter('.');
            }
        }
        generateBoardState();
    }

    /**
     * Checks if a particular word is on the tray.
     * @param word A string to check
     * @return True if the string is on the tray
     */
    public boolean contains(String word){

        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                if(board[i][j].getLetter()==word.charAt(0)){
                    LinkedList<Tile> tilesUsed = new LinkedList<>();
                    tilesUsed.add(board[i][j]);
                    if(checkForWord(word.substring(1),i,j,tilesUsed)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Recursively checks for a substring of the word passed at contiguous row,column locations on
     * the board. Keeps track of which tiles have been used to avoid using the same tile twice.
     * @param word The string to check
     * @param row The starting column
     * @param column The starting row
     * @param tilesUsed A list of tiles to keep track of which tiles have been used so far in constructing
     *                  the word.
     * @return True if the word can be constructed from that location on the board
     */
    private boolean checkForWord(String word, int row, int column, LinkedList<Tile> tilesUsed){
        if(word.length()==0){
            return true;
        }
        int rowOffset,columnOffset;
        for(rowOffset=-1;rowOffset<=1;rowOffset++){
            for(columnOffset=-1;columnOffset<=1;columnOffset++){
                int i = row+rowOffset;
                int j = column+columnOffset;
                if(i >= TRAY_HEIGHT || i<0 || j >= TRAY_WIDTH || j<0){
                    continue;
                }
                if((columnOffset==0 && rowOffset==0) || tilesUsed.contains(board[i][j])){
                    continue;
                }
                if(board[i][j].getLetter() == word.charAt(0)){
                    LinkedList<Tile> tilesUsedBranch = new LinkedList<>(tilesUsed);
                    tilesUsedBranch.add(board[i][j]);
                    if(checkForWord(word.substring(1),i,j,tilesUsedBranch)){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * Returns a string which is the concatenation of the letters on each of the tiles in the
     * player's wordAttempt
     * @return
     */
    public String getWordAttemptAsString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Tile t : wordAttempt){
            stringBuilder.append(t.getLetter());
        }
        return stringBuilder.toString();
    }

    /**
     * Clears the word attempt, unselecting all the tiles in it.
     */
    public void clearWordAttempt(){
        for(int i = wordAttempt.size()-1; i>=0; i--){
            wordAttempt.get(i).setUnselected();
        }
    }

    /**
     * Fills the board with a new set of tiles displaying '.'
     */
    private void generateTiles(){
        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                Tile tile = new Tile('.',i,j);
                tile.setTranslateX(j*TILE_SIZE);
                tile.setTranslateY(i*TILE_SIZE);
                board[i][j] = tile;
                getChildren().add(tile);
            }
        }
    }

    /**
     * Sets up each tile with a letter to display. No more than 4 of a given letter may appear on the
     * board. There is increased likelihood of a 'u' appearing next to a 'q' tile.
     */
    private void generateBoardState(){
        letterCounts = new int[26];
        Arrays.fill(letterCounts,0);
        char letter;
        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                if(Character.isLetter(board[i][j].getLetter())){
                    continue;
                }
                letter = getRandomLetter();
                if(letterCounts[(int)letter - 97] < LETTER_MAX){         //ASCII 'a' = 97, 'b'= 98 ...
                    board[i][j].setLetter(letter);
                    letterCounts[(int)letter - 97]++;
                    if(letter=='q'){
                        handleQ(i,j);
                    }
                } else {
                    j--;
                }
            }
        }
    }

    /**
     * Sets the tile either immediately preceding or following the index given with a new
     * random letter which is heavily favored to be a 'u'
     * @param i The row of the q
     * @param j The column of the q
     */
    private void handleQ(int i, int j){
        char newLetter;
        do{
            newLetter = getRandomLetterUFavored();
        } while(letterCounts[(int)newLetter - 97] >= LETTER_MAX);
        if(j == TRAY_WIDTH-1){
            letterCounts[(int)board[i][j-1].getLetter() - 97]--;
            board[i][j-1].setLetter(newLetter);
            letterCounts[(int)newLetter - 97]++;
            if(newLetter == 'q'){
               handleQ(i,j-1);
            }
        } else {
            board[i][j+1].setLetter(newLetter);
            letterCounts[(int)newLetter - 97]++;
            if(newLetter == 'q'){
                handleQ(i,j+1);
            }
        }
    }

    /**
     * @return A random lowercase letter
     */
    private char getRandomLetter(){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random();
        return alphabet.charAt(rand.nextInt(alphabet.length()));
    }

    /**
     * @return A random character which has ~80% of being a 'u'
     */
    private char getRandomLetterUFavored(){
        Random rand = new Random();
        if(rand.nextInt(10) > 1){
            return 'u';
        } else {
            return getRandomLetter();
        }
    }

    /**
     * The Tile objects hold information about their own location (row and column) as well as the
     * letter they display (a Text object).
     */
    private class Tile extends StackPane {

        private Text text = new Text();
        private int row;
        private int column;
        private boolean selected;
        private Rectangle tile;

        /**
         * Constructs a new Tile with the given character at the given location.
         * @param letter Any character
         * @param row The row index
         * @param column The column index
         */
        public Tile(char letter, int row, int column){
            this.row = row;
            this.column = column;
            text.setText(String.valueOf(letter));
            selected = false;
            tileDisplaySetup();

            setOnMouseClicked(event -> {
                if(event.getButton()== MouseButton.PRIMARY){
                    setSelected();

                } else if (event.getButton()== MouseButton.SECONDARY){
                    setUnselected();
                }
            });

        }

        /**
         * Sets up the initial GUI formatting for the tiles.
         */
        private void tileDisplaySetup(){
            text.setFont(Font.font(TILE_SIZE/1.4));
            tile = new Rectangle(TILE_SIZE,TILE_SIZE);
            tile.setFill(null);
            tile.setStroke(Color.BLACK);
            getChildren().addAll(tile,text);
        }

        /**
         * @return True if this tile is selected
         */
        public boolean getSelected(){
            return selected;
        }

        /**
         * Sets this tile as selected (selected = true), changes it's background color to PURPLE,
         * and adds this tile's letter to the wordAttempt if it wasn't already selected.
         */
        public void setSelected(){
            if(!selected){
                wordAttempt.add(this);
            }
            selected = true;
            tile.setFill(Color.PURPLE);
        }

        /**
         * Sets this tile as unselected (selected = false), changes it's background color to null (clear),
         * and removes it from the wordAttempt.
         */
        public void setUnselected(){
            if(wordAttempt.getLast() == this){
                wordAttempt.removeLast();
                selected = false;
                tile.setFill(null);
            }
        }

        /**
         * @return The character this Tile contains
         */
        public char getLetter(){
            return text.getText().charAt(0);
        }

        /**
         * @param letter A character to be displayed
         */
        public void setLetter(char letter){
            text.setText(Character.toString(letter));
        }

        /**
         * @return The row index
         */
        public int getRow(){
            return row;
        }

        /**
         * @return The column index
         */
        public int getColumn(){
            return column;
        }

    }


}
