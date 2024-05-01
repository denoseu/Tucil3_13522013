import java.util.*;

public class UCS implements SearchStrategy {
    @Override
    public List<String> findWordLadder(String start, String end, Dictionary dictionary) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(Node::getFn));
        Set<String> visited = new HashSet<>();
        frontier.add(new Node(start, null, 0));

        System.out.println("Starting UCS from: " + start + " to: " + end);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            System.out.println("Exploring: " + current.getWord() + " with f(n) = g(n): " + current.getFn());

            if (current.getWord().equals(end)) {
                System.out.println("Goal reached: " + current.getWord());
                return constructPath(current);
            }

            if (!visited.contains(current.getWord())) {
                visited.add(current.getWord());
                for (String neighbor : getNeighbors(current.getWord(), dictionary)) {
                    if (!visited.contains(neighbor)) {
                        System.out.println("Adding to frontier: " + neighbor);
                        frontier.add(new Node(neighbor, current, current.getFn() + 1));
                    }
                }
            }
        }
        System.out.println("No path found");
        return Collections.emptyList();
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
