import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Tray extends Pane {

    private final int TRAY_HEIGHT = 5;
    private final int TRAY_WIDTH = 5;
    private final int TILE_SIZE = 50;
    private final int LETTER_MAX = 4;

    private Tile[][] board = new Tile[TRAY_HEIGHT][TRAY_WIDTH];
    private int[] letterCounts;

    public Tray(){
        setPrefSize(TRAY_WIDTH*TILE_SIZE,TRAY_HEIGHT*TILE_SIZE);
        generateTiles();
        generateBoardState();
    }

    public boolean contains(String word){

        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                if(board[i][j].getLetter()==word.charAt(0)){
                    LinkedList<Tile> tilesUsed = new LinkedList();
                    tilesUsed.add(board[i][j]);
                    if(checkForWord(word.substring(1),i,j,tilesUsed)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

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
                if((i==0 && j==0) || tilesUsed.contains(board[i][j])){
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

    private char getRandomLetter(){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random();
        return alphabet.charAt(rand.nextInt(alphabet.length()));
    }

    private char getRandomLetterUFavored(){
        Random rand = new Random();
        if(rand.nextInt(10) > 1){
            return 'u';
        } else {
            return getRandomLetter();
        }
    }



    private class Tile extends StackPane {

        private Text text = new Text();
        private int row;
        private int column;

        public Tile(char letter, int row, int column){
            this.row = row;
            this.column = column;
            text.setText(String.valueOf(letter));
            text.setFont(Font.font(TILE_SIZE/1.25));
            Rectangle tileEdge = new Rectangle(TILE_SIZE,TILE_SIZE);
            tileEdge.setFill(null);
            tileEdge.setStroke(Color.BLACK);
            getChildren().addAll(tileEdge,text);
        }

        public char getLetter(){
            return text.getText().charAt(0);
        }

        public void setLetter(char letter){
            text.setText(Character.toString(letter));
        }

        public int getRow(){
            return row;
        }

        public int getColumn(){
            return column;
        }

    }


}
