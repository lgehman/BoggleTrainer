import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Author: Luke Gehman
 * Encapsulates a dictionary text file, and can search that file for a string with the contains() method.
 */
public class Dictionary {
    private URL urlToDictionary;

    public Dictionary(){
        urlToDictionary = this.getClass().getResource("/" + "dictionary.txt");
    }

    public boolean contains(String word) {
        try{
            InputStream stream = urlToDictionary.openStream();
            Scanner scanner = new Scanner(stream);
            while(scanner.hasNextLine()){
                if(word.equals(scanner.nextLine())){
                    return true;
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }

        return false;
    }
}
