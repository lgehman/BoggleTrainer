import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Dictionary {
    private URL urlToDictionary;

    public Dictionary(){
        urlToDictionary = this.getClass().getResource("/" + "dictionary.txt");
    }

    public boolean contains(String word){
        try{
            InputStream stream = urlToDictionary.openStream();
            Scanner scanner = new Scanner(stream);
            while(scanner.hasNextLine()){
                if(word.equals(scanner.nextLine())){
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
