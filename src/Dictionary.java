import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Author: Luke Gehman
 * Encapsulates a dictionary text file, and can search that file for a string using the contains() method.
 */
public class Dictionary {
    private URL urlToDictionary;

    /**
     * Sets up a dictionary with a URL to the dictionary text file, currently "dictionary.txt"
     */
    public Dictionary(){
        urlToDictionary = this.getClass().getResource("/" + "dictionary.txt");
    }

    /**
     * Searches each line of the dictionary for a particular word
     * @param word A string to search for
     * @return True if one of the lines in the dictionary matches the string, false otherwise
     */
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
            e.printStackTrace();
        }

        return false;
    }
}
