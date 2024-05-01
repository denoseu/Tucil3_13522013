import java.io.*;
import java.util.*;

public class Dictionary {
    private Set<String> words;

    public Dictionary(String filename) throws IOException {
        words = new LinkedHashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line.trim());
        }
        reader.close();
    }

    public boolean isWord(String word) {
        return words.contains(word);
    }

    public void printDictionary() {
        for (String word : words) {
            System.out.println(word);
        }
    }
}
