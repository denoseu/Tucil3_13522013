package src;
import java.util.List;

// interface buat diimplement masing-masing di UCS, GBFS, AStar
public interface SearchStrategy {
    SearchResult findWordLadder(String start, String end, Dictionary dictionary);
}

// simpen search result, berupa path dan jumlah visited nodes
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
