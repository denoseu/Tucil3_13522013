package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WordLadderGUI extends JFrame {
    private BackgroundPanel backgroundPanel;
    private JTextField startWordField;
    private JTextField endWordField;
    private JComboBox<String> algorithmChoice;
    private JTextPane resultTextPane;
    private Image resultImage;

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
        gbc.insets = new Insets(150, 0, 20, 0);
        backgroundPanel.add(inputPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        ImageIcon searchButtonIcon = new ImageIcon("src/assets/search.png");
        JButton searchButton = new JButton(new ImageIcon(searchButtonIcon.getImage().getScaledInstance(107, 45, Image.SCALE_SMOOTH)));
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder());
        searchButton.setContentAreaFilled(false);
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
        resultPanel.setPreferredSize(new Dimension(800, 330));

        resultTextPane = new JTextPane();
        resultTextPane.setEditable(false);
        resultTextPane.setContentType("text/html");
        resultTextPane.setOpaque(false);
        resultTextPane.setForeground(Color.WHITE);
        resultTextPane.setFont(new Font("SansSerif", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(resultTextPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        backgroundPanel.add(resultPanel, gbc);
    }

    // handle search action
    private void performSearch() {
        String start = startWordField.getText().trim();
        String end = endWordField.getText().trim();
        String algorithm = (String) algorithmChoice.getSelectedItem();
    
        long startTime = System.currentTimeMillis();
        // Using SwingWorker to perform search in the background
        new SwingWorker<List<String>, Void>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                Dictionary dictionary = new Dictionary("src/dictionary.txt");
                if (!dictionary.isWord(start) || !dictionary.isWord(end)) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(WordLadderGUI.this, "Both words must be in the dictionary."));
                    return null;
                }
    
                SearchStrategy strategy = getStrategy(algorithm);
                if (strategy == null) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(WordLadderGUI.this, "Invalid algorithm choice."));
                    return null;
                }
    
                // Set loading message here, after checks
                SwingUtilities.invokeLater(() -> resultTextPane.setText("<html><div style='text-align: center; padding-top: 120px; font-size: 18px; color: #FFFDF4;'>Loading...</div></html>"));
    
                return strategy.findWordLadder(start, end, dictionary);
            }
    
            @Override
            protected void done() {
                try {
                    // get search results
                    List<String> path = get();
                    if (path != null) {
                        // hitung waktu
                        long endTime = System.currentTimeMillis();
                        long timeTaken = endTime - startTime;
                        displayResults(path, timeTaken);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    JOptionPane.showMessageDialog(WordLadderGUI.this, "Error: " + e.getMessage());
                }
            }
        }.execute();
    }
    
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

    // display hasil
    private void displayResults(List<String> path, long timeTaken) {
        StringBuilder sb = new StringBuilder("<html>");
    
        sb.append("<div style='text-align: center; padding-top: 10px; font-size: 18px; color: #FFFDF4;'>Path found:<br></div>");
    
        // keep semua isi yang lain
        sb.append("<div style='text-align: center; padding-top: 20px; font-size: 18px;'>");
        
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
                sb.append(formatWordTransition(path.get(i - 1), path.get(i)));
            } else {
                sb.append(formatInitialWord(path.get(i)));
            }
        }
        
        // div utk total steps dan exe time
        sb.append("<div style='font-size: 14px; color: #FFFDF4;'>")
          .append("Total steps: ").append(path.size() - 1)
          .append("<br>Execution time: ").append(timeTaken).append(" ms<br><br</div>");
        
        // close main dan html tags
        sb.append("</div></html>");
        
        // lihat isi htmlnya di textpane
        resultTextPane.setText(sb.toString());
    }

    private String formatInitialWord(String word) {
        StringBuilder sb = new StringBuilder("<div style='letter-spacing: 10px;'>"); // Adding space between letter boxes
        for (char c : word.toCharArray()) {
            // nbsp buat spasi
            sb.append("<span style='border:2px solid black; background-color: #FFFDF4; padding:15px; margin:5px; font-size: 20px; display: inline-block; width:40px; height:40px; line-height:40px; text-align:center;'>&nbsp;")
              .append(c).append("&nbsp;</span>");
        }
        sb.append("</div><br>");
        return sb.toString();
    }
    
    private String formatWordTransition(String oldWord, String newWord) {
        StringBuilder sb = new StringBuilder("<div style='letter-spacing: 10px;'>"); // space between letters
        for (int i = 0; i < oldWord.length(); i++) {
            char newChar = newWord.charAt(i);
            String bgColor = oldWord.charAt(i) == newChar ? "#FFFDF4" : "#F69DA2"; // ganti warna buat yg berubah
            sb.append("<span style='border:2px solid black; background-color:")
              .append(bgColor).append("; padding:15px; margin:5px; font-size: 20px; display: inline-block; width:40px; height:40px; line-height:40px; text-align:center;'>&nbsp;")
              .append(newChar).append("&nbsp;</span>");
        }
        sb.append("</div><br>");
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordLadderGUI().setVisible(true));
    }

    // custom jpanel utk bg image
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
