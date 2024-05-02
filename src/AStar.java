package src;
import java.util.*;

public class AStar implements SearchStrategy {
    @Override
    public List<String> findWordLadder(String start, String end, Dictionary dictionary) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(Node::getFn));
        Map<String, Integer> costSoFar = new HashMap<>();
        Map<String, Integer> heuristicMap = new HashMap<>(); // To store heuristic values for each node

        int startHeuristic = heuristic(start, end);
        frontier.offer(new Node(start, null, startHeuristic));
        costSoFar.put(start, 0);
        heuristicMap.put(start, startHeuristic);

        System.out.println("Starting A* from: " + start + " to: " + end);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            int currentG = costSoFar.get(current.getWord());
            int currentH = heuristicMap.get(current.getWord());

            System.out.println("Exploring: " + current.getWord() + " with g(n): " + currentG +
                               ", h(n): " + currentH + ", f(n): " + current.getFn());

            if (current.getWord().equals(end)) {
                System.out.println("Goal reached: " + current.getWord());
                return constructPath(current);
            }

            for (String neighbor : getNeighbors(current.getWord(), dictionary)) {
                int newCost = currentG + 1; // Assuming each step has a cost of 1
                int neighborHeuristic = heuristic(neighbor, end);
                int fCost = newCost + neighborHeuristic;

                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    heuristicMap.put(neighbor, neighborHeuristic);
                    frontier.offer(new Node(neighbor, current, fCost));
                    System.out.println("Adding to frontier: " + neighbor + " with g(n): " + newCost +
                                       ", h(n): " + neighborHeuristic + ", f(n): " + fCost);
                }
            }
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
        LinkedList<String> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node.getWord());
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
            chars[i] = originalChar; // Restore original character
        }
        return neighbors;
    }
}