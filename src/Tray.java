import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Random;

public class Tray extends Pane {

    private final int TRAY_HEIGHT = 5;
    private final int TRAY_WIDTH = 5;
    private final int TILE_SIZE = 50;

    private char[][] board = new char[TRAY_HEIGHT][TRAY_WIDTH];

    public Tray(){
        setPrefSize(TRAY_WIDTH*TILE_SIZE,TRAY_HEIGHT*TILE_SIZE);
        generateBoardState();

        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                Tile tile = new Tile(board[i][j]);
                tile.setTranslateX(j*TILE_SIZE);
                tile.setTranslateY(i*TILE_SIZE);
                getChildren().add(tile);
            }
        }
    }

    private void generateBoardState(){
        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                board[i][j] = getRandomLetter();
            }
        }
    }

    private char getRandomLetter(){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random();
        return alphabet.charAt(rand.nextInt(alphabet.length()));
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
