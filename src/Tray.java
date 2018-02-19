import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.Random;

public class Tray extends Pane {

    private final int TRAY_HEIGHT = 5;
    private final int TRAY_WIDTH = 5;
    private final int TILE_SIZE = 50;
    private final int LETTER_MAX = 4;

    private char[][] board = new char[TRAY_HEIGHT][TRAY_WIDTH];
    private int[] letterCounts;

    public Tray(){
        setPrefSize(TRAY_WIDTH*TILE_SIZE,TRAY_HEIGHT*TILE_SIZE);
        generateBoardState();
        generateTiles();
    }

    private void generateBoardState(){
        letterCounts = new int[26];
        Arrays.fill(letterCounts,0);
        char letter;
        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                if(Character.isLetter(board[i][j])){
                    continue;
                }
                letter = getRandomLetter();
                if(letterCounts[(int)letter - 97] < LETTER_MAX){         //ASCII 'a' = 97, 'b'= 98 ...
                    board[i][j] = letter;
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
            letterCounts[(int)board[i][j-1] - 97]--;
            board[i][j-1] = newLetter;
            letterCounts[(int)newLetter - 97]++;
            if(newLetter == 'q'){
               handleQ(i,j-1);
            }
        } else {
            board[i][j+1] = newLetter;
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

    private void generateTiles(){
        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                Tile tile = new Tile(board[i][j]);
                tile.setTranslateX(j*TILE_SIZE);
                tile.setTranslateY(i*TILE_SIZE);
                getChildren().add(tile);
            }
        }
    }

    private class Tile extends StackPane {

        private Text text = new Text();

        public Tile(char letter){
            text.setText(String.valueOf(letter));
            text.setFont(Font.font(TILE_SIZE/1.25));
            Rectangle tileEdge = new Rectangle(TILE_SIZE,TILE_SIZE);
            tileEdge.setFill(null);
            tileEdge.setStroke(Color.BLACK);
            getChildren().addAll(tileEdge,text);
        }
    }


}
