package src;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Welcome to the CLI Word Ladder Solver!");

            // load dictionary
            Dictionary dictionary = new Dictionary("src/dictionary.txt");

            System.out.print("Enter start word: ");
            String start = scanner.nextLine();
            System.out.print("Enter end word: ");
            String end = scanner.nextLine();

            // cek apakah katanya ada di dictionary
            if (!dictionary.isWord(start.toUpperCase()) || !dictionary.isWord(end.toUpperCase())) {
                System.out.println("Both words must be in the dictionary.");
                return;
            }

            System.out.println("Select the algorithm:");
            System.out.println("1. Uniform Cost Search (UCS)");
            System.out.println("2. Greedy Best-First Search");
            System.out.println("3. A* Search");
            System.out.print("Enter choice (1, 2, or 3): ");
            int algorithmChoice = scanner.nextInt();
            scanner.nextLine();

            SearchStrategy strategy;
            switch (algorithmChoice) {
                case 1:
                    strategy = new UCS();
                    break;
                case 2:
                    strategy = new GBFS();
                    break;
                case 3:
                    strategy = new AStar();
                    break;
                default:
                    System.out.println("Invalid algorithm choice.");
                    return;
            }

            long startTime = System.currentTimeMillis();
            SearchResult searchResult = strategy.findWordLadder(start.toUpperCase(), end.toUpperCase(), dictionary);
            long endTime = System.currentTimeMillis();
            List<String> path = searchResult.getPath();

            // results
            if (path != null && !path.isEmpty()) {
                System.out.println("Path found:");
                path.forEach(System.out::println);
                System.out.println("Total steps: " + (path.size() - 1));
            } else {
                System.out.println("No path found between the given words.");
            }
            System.out.println("Nodes explored: " + searchResult.getNodesExplored());
            System.out.println("Execution time: " + (endTime - startTime) + " ms");

        } catch (IOException e) {
            System.out.println("Failed to load the dictionary.");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
