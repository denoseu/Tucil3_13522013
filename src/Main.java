package src;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Welcome to the Word Ladder Solver!");

            // Load the dictionary
            Dictionary dictionary = new Dictionary("src/dictionary.txt");

            // Get user input for start and end words
            System.out.print("Enter start word: ");
            String start = scanner.nextLine();
            System.out.print("Enter end word: ");
            String end = scanner.nextLine();

            // Ensure both words are in the dictionary
            if (!dictionary.isWord(start) || !dictionary.isWord(end)) {
                System.out.println("Both words must be in the dictionary.");
                return;
            }

            // Algorithm selection
            System.out.println("Select the algorithm:");
            System.out.println("1. Uniform Cost Search (UCS)");
            System.out.println("2. Greedy Best-First Search");
            System.out.println("3. A* Search");
            System.out.print("Enter choice (1, 2, or 3): ");
            int algorithmChoice = scanner.nextInt();
            scanner.nextLine(); // consume the remaining newline

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

            // Execute the selected algorithm
            long startTime = System.currentTimeMillis();
            SearchResult searchResult = strategy.findWordLadder(start, end, dictionary);
            long endTime = System.currentTimeMillis();
            List<String> path = searchResult.getPath();

            // Output results
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
            scanner.close(); // Ensure the scanner is closed after use
        }
    }
}
