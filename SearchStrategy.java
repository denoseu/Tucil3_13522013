import java.util.List;

public interface SearchStrategy {
    List<String> findWordLadder(String start, String end, Dictionary dictionary);
}

