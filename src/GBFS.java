package src;
import java.util.*;

public class GBFS implements SearchStrategy {
    @Override
    public SearchResult findWordLadder(String start, String end, Dictionary dictionary) {
        // prio queue untuk simpen semua simpul hidup (yang akan diekspansi) -> urut berdasarkan h(n)
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> heuristic(node.getWord(), end)));
        // Map<String, Integer> visited = new HashMap<>();
        Set<String> visited = new HashSet<>();
        frontier.offer(new Node(start, null, 0));
        int exploredCount = 0;

        // System.out.println("Starting GBFS from: " + start + " to: " + end);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            // System.out.println("Exploring: " + current.getWord() + " with f(n) = heuristic h(n): " + heuristic(current.getWord(), end));
            exploredCount++;

            if (current.getWord().equals(end)) {
                return new SearchResult(constructPath(current), exploredCount);
            }

            // cek dia udah pernah diekspansi atau belum, kalau sudah tidak usah lagi
            // if (!visited.contains(current.getWord())) {
            visited.add(current.getWord());
            frontier.clear();
            for (String neighbor : getNeighbors(current.getWord(), dictionary)) {
                if (!visited.contains(neighbor)) {
                    int fn = heuristic(neighbor, end);
                // System.out.println("Checking neighbor: " + neighbor + " with heuristic h(n): " + neighborHeuristic);
                    // System.out.println("Adding to frontier: " + neighbor + " with heuristic h(n): " + neighborHeuristic);
                    frontier.add(new Node(neighbor, current, fn));
                }
            }
            // }
        }
        return new SearchResult(Collections.emptyList(), exploredCount);
    }

    private int heuristic(String word, String end) {
        int diff = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != end.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

    private List<String> constructPath(Node node) {
        List<String> path = new LinkedList<>();
        while (node != null) {
            path.add(0, node.getWord());
            node = node.getParent();
        }
        return path;
    }

    private List<String> getNeighbors(String word, Dictionary dictionary) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'A'; c <= 'Z'; c++) {
                if (c != originalChar) {
                    chars[i] = c;
                    String newWord = new String(chars);
                    if (dictionary.isWord(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }
            chars[i] = originalChar; // restore original character
        }
        return neighbors;
    }
}
