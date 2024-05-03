package src;
import java.util.List;

public interface SearchStrategy {
    SearchResult findWordLadder(String start, String end, Dictionary dictionary);
}

class SearchResult {
    List<String> path;
    int nodesExplored;

    public SearchResult(List<String> path, int nodesExplored) {
        this.path = path;
        this.nodesExplored = nodesExplored;
    }

    public List<String> getPath() {
        return path;
    }

    public int getNodesExplored() {
        return nodesExplored;
    }
}
