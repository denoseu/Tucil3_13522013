package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class WordLadderGUI extends JFrame {
    private BackgroundPanel backgroundPanel; // Panel that draws the background image
    private JTextField startWordField;
    private JTextField endWordField;
    private JComboBox<String> algorithmChoice;
    private JTextArea resultTextArea;
    private Image resultImage; // Image for the results area

    public WordLadderGUI() {
        createUI();
    }

    private void createUI() {
        setTitle("Denise's Word Ladder Solver (Valentine's day Ed.)");
        setSize(1414, 2000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("src/assets/background.png");
        Image backgroundImage = icon.getImage().getScaledInstance(2000, 1414, Image.SCALE_SMOOTH);

        ImageIcon resultIcon = new ImageIcon("src/assets/resultbg.png");
        resultImage = resultIcon.getImage().getScaledInstance(1200, 300, Image.SCALE_SMOOTH);

        backgroundPanel = new BackgroundPanel(backgroundImage);
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setOpaque(false);

        startWordField = new JTextField(10);
        endWordField = new JTextField(10);
        startWordField.setBackground(Color.WHITE);
        endWordField.setBackground(Color.WHITE);

        ImageIcon startWordIcon = new ImageIcon("src/assets/start.png");
        ImageIcon endWordIcon = new ImageIcon("src/assets/end.png");
        JLabel startWordLabel = new JLabel(new ImageIcon(startWordIcon.getImage().getScaledInstance(170, 40, Image.SCALE_SMOOTH)));
        JLabel endWordLabel = new JLabel(new ImageIcon(endWordIcon.getImage().getScaledInstance(170, 40, Image.SCALE_SMOOTH)));

        inputPanel.add(startWordLabel);
        inputPanel.add(startWordField);
        inputPanel.add(endWordLabel);
        inputPanel.add(endWordField);

        String[] algorithms = {"Uniform Cost Search", "Greedy Best-First Search", "A* Search"};
        algorithmChoice = new JComboBox<>(algorithms);
        ImageIcon algorithmIcon = new ImageIcon("src/assets/algorithm.png");
        JLabel algorithmLabel = new JLabel(new ImageIcon(algorithmIcon.getImage().getScaledInstance(170, 40, Image.SCALE_SMOOTH)));

        inputPanel.add(algorithmLabel);
        inputPanel.add(algorithmChoice);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;
        gbc.insets = new Insets(120, 0, 20, 0);
        backgroundPanel.add(inputPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        ImageIcon searchButtonIcon = new ImageIcon("src/assets/search.png");
        JButton searchButton = new JButton(new ImageIcon(searchButtonIcon.getImage().getScaledInstance(107, 38, Image.SCALE_SMOOTH)));
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(createRoundRectBorder(10, 1, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        buttonPanel.add(searchButton);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        backgroundPanel.add(buttonPanel, gbc);

        JPanel resultPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(resultImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        resultPanel.setOpaque(false);
        resultPanel.setPreferredSize(new Dimension(800, 300));

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setOpaque(false);
        resultTextArea.setForeground(Color.WHITE);
        resultTextArea.setFont(new Font("SansSerif", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        backgroundPanel.add(resultPanel, gbc);
    }

    private static Border createRoundRectBorder(int radius, int thickness, Color insideColor, Color borderColor) {
        return new Border() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(borderColor);
                g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
                ((Graphics2D) g).setStroke(new BasicStroke(thickness));
                g.setColor(insideColor);
                g.fillRoundRect(x + thickness, y + thickness, width - 2 * thickness, height - 2 * thickness, radius, radius);
            }

            public Insets getBorderInsets(Component c) {
                return new Insets(thickness, thickness, thickness, thickness);
            }

            public boolean isBorderOpaque() {
                return false;
            }
        };
    }

    // Method to handle the search action
    private void performSearch() {
        String start = startWordField.getText();
        String end = endWordField.getText();
        String algorithm = (String) algorithmChoice.getSelectedItem();
        try {
            // Assuming Dictionary and SearchStrategy classes are correctly implemented
            Dictionary dictionary = new Dictionary("src/dictionary.txt");
            if (!dictionary.isWord(start) || !dictionary.isWord(end)) {
                JOptionPane.showMessageDialog(this, "Both words must be in the dictionary.");
                return;
            }

            SearchStrategy strategy = getStrategy(algorithm);
            if (strategy == null) {
                JOptionPane.showMessageDialog(this, "Invalid algorithm choice.");
                return;
            }

            long startTime = System.currentTimeMillis();
            List<String> path = strategy.findWordLadder(start, end, dictionary);
            long endTime = System.currentTimeMillis();

            displayResults(path, endTime - startTime);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Helper method to select the appropriate search strategy
    private SearchStrategy getStrategy(String algorithm) {
        switch (algorithm) {
            case "Uniform Cost Search":
                return new UCS();
            case "Greedy Best-First Search":
                return new GBFS();
            case "A* Search":
                return new AStar();
            default:
                return null;
        }
    }

    // Helper method to display results in the GUI
    private void displayResults(List<String> path, long timeTaken) {
        if (path != null && !path.isEmpty()) {
            resultTextArea.setText("Path found:\n" + String.join("\n", path) +
                                   "\nTotal steps: " + (path.size() - 1) +
                                   "\nExecution time: " + timeTaken + " ms");
        } else {
            resultTextArea.setText("No path found between the given words.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordLadderGUI().setVisible(true));
    }

    // Custom JPanel for background image
    private class BackgroundPanel extends JPanel {
        private Image image;

        public BackgroundPanel(Image image) {
            this.image = image;
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
