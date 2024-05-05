package src;
import java.util.*;

public class UCS implements SearchStrategy {
    @Override
    public SearchResult findWordLadder(String start, String end, Dictionary dictionary) {
        // priority queue untuk menyimpan node yang akan diekspansi (dicari semua tetangga-tetangga dari node ini) -> semua simpul hidupnya
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(Node::getFn));
        // set untuk menyimpan node yang sudah diekspansi
        Set<String> visited = new HashSet<>();
        frontier.add(new Node(start, null, 0));
        int exploredCount = 0;

        System.out.println("Starting UCS from: " + start + " to: " + end);

        while (!frontier.isEmpty()) {
            // ambil node dengan f(n) terkecil -> udah prioqueue jadi udah otomatis terurut
            Node current = frontier.poll();
            exploredCount++;
            System.out.println("Exploring: " + current.getWord() + " with f(n) = g(n): " + current.getFn());

            // cek apakah node ini adalah goal, jika iya maka langsung return
            if (current.getWord().equals(end)) {
                System.out.println("Goal reached: " + current.getWord());
                return new SearchResult(constructPath(current), exploredCount);
            }

            // if (!visited.contains(current.getWord())) {
            visited.add(current.getWord());
            for (String neighbor : getNeighbors(current.getWord(), dictionary)) {
                // cek apakah tetangga ini sudah pernah diekspansi sebelumnya
                if (!visited.contains(neighbor)) {
                    // kalau belum, dia ditambahin ke frontier
                    System.out.println("Adding to frontier: " + neighbor);
                    frontier.add(new Node(neighbor, current, current.getFn() + 1));
                }
            }
            // }
        }
        return new SearchResult(Collections.emptyList(), exploredCount);
    }

    private List<String> constructPath(Node node) {
        List<String> path = new LinkedList<>();
        while (node != null) {
            path.add(0, node.getWord());
            node = node.getParent();
        }
        return path;
    }

    // fungsi untuk mendapatkan semua tetangga dari suatu kata (beda 1 huruf)
    private List<String> getNeighbors(String word, Dictionary dictionary) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'A'; c <= 'Z'; c++) {
                if (c != originalChar) {
                    chars[i] = c;
                    String newWord = new String(chars);
                    // System.out.println("Checking potential neighbor: " + newWord);
                    if (dictionary.isWord(newWord)) {
                        neighbors.add(newWord);
                        // System.out.println("Valid neighbor added: " + newWord);
                    }
                }
            }
            chars[i] = originalChar; // restore original character
        }
        return neighbors;
    }
    
}
