import java.io.File;
import java.util.Scanner;

public class Dictionary {
    private File dictionarySource;

    public Dictionary(){
        dictionarySource = new File("animals.txt");
    }

    public boolean contains(String word){
        try{
            Scanner scanner = new Scanner(dictionarySource);
            while(scanner.hasNextLine()){
                if(word.equals(scanner.nextLine())){
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Source file not found");
        }
        return false;
    }
}
