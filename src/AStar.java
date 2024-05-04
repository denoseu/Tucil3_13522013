package src;

import java.util.*;

public class AStar implements SearchStrategy {
    @Override
    public SearchResult findWordLadder(String start, String end, Dictionary dictionary) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> node.getFn() + heuristic(node.getWord(), end)));
        Set<String> visited = new HashSet<>();
        frontier.add(new Node(start, null, 0));
        int exploredCount = 0;

        System.out.println("Starting A* from: " + start + " to: " + end);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            exploredCount++;
            System.out.println("Exploring: " + current.getWord() + " with f(n) = g(n) + h(n): " + (current.getFn() + heuristic(current.getWord(), end)));

            if (current.getWord().equals(end)) {
                System.out.println("Goal reached: " + current.getWord());
                return new SearchResult(constructPath(current), exploredCount);
            }

            if (!visited.contains(current.getWord())) {
                visited.add(current.getWord());
                for (String neighbor : getNeighbors(current.getWord(), dictionary)) {
                    if (!visited.contains(neighbor)) {
                        int gn = current.getFn() + 1;
                        int hn = heuristic(neighbor, end);
                        int fn = gn + hn;
                        frontier.add(new Node(neighbor, current, fn));
                    }
                }
            }
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
