public class GameCoordinator {
    private Dictionary dictionary;
    private GameDisplay gameDisplay;

    public GameCoordinator(String dictFileName){
        dictionary = new Dictionary(dictFileName);
        gameDisplay = new GameDisplay();
    }
}
