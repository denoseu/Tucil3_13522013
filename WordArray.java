import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class WordArray {

    public static void main(String[] args) {
        String filePath = "dictionary.txt"; // Path to the text file containing words
        String[] wordsArray = readWordsFromFile(filePath);
        
        // Print the entire array of words
        System.out.println(Arrays.toString(wordsArray));
    }

    private static String[] readWordsFromFile(String filePath) {
        List<String> words = new ArrayList<>();
        File file = new File(filePath);
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine().trim()); // Add each word to the list, trimming whitespace
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: The file was not found.");
            return new String[0]; // Return an empty array in case of error
        }
        
        // Convert the List to an array and return
        return words.toArray(new String[0]);
    }
}
