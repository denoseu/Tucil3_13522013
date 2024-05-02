package src;
import java.util.*;

public class GBFS implements SearchStrategy {
    @Override
    public List<String> findWordLadder(String start, String end, Dictionary dictionary) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> heuristic(node.getWord(), end)));
        Map<String, Integer> visited = new HashMap<>();
        frontier.offer(new Node(start, null, 0));

        System.out.println("Starting GBFS from: " + start + " to: " + end);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            System.out.println("Exploring: " + current.getWord() + " with f(n) = heuristic h(n): " + heuristic(current.getWord(), end));

            if (current.getWord().equals(end)) {
                System.out.println("Goal reached: " + current.getWord());
                return constructPath(current);
            }

            if (!visited.containsKey(current.getWord()) || visited.get(current.getWord()) > heuristic(current.getWord(), end)) {
                visited.put(current.getWord(), heuristic(current.getWord(), end));
                for (String neighbor : getNeighbors(current.getWord(), dictionary)) {
                    int neighborHeuristic = heuristic(neighbor, end);
                    System.out.println("Checking neighbor: " + neighbor + " with heuristic h(n): " + neighborHeuristic);
                    if (!visited.containsKey(neighbor) || visited.get(neighbor) > neighborHeuristic) {
                        System.out.println("Adding to frontier: " + neighbor + " with heuristic h(n): " + neighborHeuristic);
                        frontier.offer(new Node(neighbor, current, 0));
                    }
                }
            }
            printPriorityQueue(frontier, end);
        }
        System.out.println("No path found");
        return Collections.emptyList(); // No path found
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

    private void printPriorityQueue(PriorityQueue<Node> queue, String end) {
        List<Node> sortedNodes = new ArrayList<>(queue);
        sortedNodes.sort(Comparator.comparingInt(node -> heuristic(node.getWord(), end)));

        System.out.println("Current Priority Queue:");
        for (Node node : sortedNodes) {
            System.out.println(node.getWord() + " - h(n): " + heuristic(node.getWord(), end));
        }
    }
}
