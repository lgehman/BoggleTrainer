import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tray extends Pane {

    private final int TRAY_HEIGHT = 5;
    private final int TRAY_WIDTH = 5;
    private final int TILE_SIZE = 50;

    public Tray(){
        setPrefSize(TRAY_WIDTH*TILE_SIZE,TRAY_HEIGHT*TILE_SIZE);
        for(int i=0;i<TRAY_HEIGHT;i++){
            for(int j=0;j<TRAY_WIDTH;j++){
                Tile tile = new Tile();
                tile.setTranslateX(j*TILE_SIZE);
                tile.setTranslateY(i*TILE_SIZE);
                getChildren().add(tile);
            }
        }
    }

    private class Tile extends StackPane {
        public Tile(){
            Rectangle tileEdge = new Rectangle(TILE_SIZE,TILE_SIZE);
            tileEdge.setFill(null);
            tileEdge.setStroke(Color.BLACK);
            getChildren().add(tileEdge);
        }

    }
}
