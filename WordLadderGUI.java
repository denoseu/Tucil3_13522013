import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class WordLadderGUI extends JFrame {
    private BackgroundPanel backgroundPanel; // Panel that draws the background image
    private JTextField startWordField;
    private JTextField endWordField;
    private Image resultImage; // Image for the results area

    public WordLadderGUI() {
        createUI();
    }

    private void createUI() {
        setTitle("Denise's Word Ladder Solver (Valentine's day Ed.)");
        setSize(1414, 2000); // Adjust size as needed
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load the background image
        ImageIcon icon = new ImageIcon("background.png"); // Replace with your image path
        Image backgroundImage = icon.getImage().getScaledInstance(2000, 1414, Image.SCALE_SMOOTH);

        // Load the result background image
        ImageIcon resultIcon = new ImageIcon("resultbg.png"); // Replace with your image path
        resultImage = resultIcon.getImage().getScaledInstance(1200, 300, Image.SCALE_SMOOTH); // Scale as needed

        // Set the custom panel with background
        backgroundPanel = new BackgroundPanel(backgroundImage);
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for flexible positioning

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Adjusted GridLayout for input fields and dropdown
        inputPanel.setOpaque(false); // Transparent panel

        // Start and end word fields
        startWordField = new JTextField(10);
        endWordField = new JTextField(10);
        startWordField.setBackground(Color.WHITE);
        endWordField.setBackground(Color.WHITE);

        // Icons for labels
        ImageIcon startWordIcon = new ImageIcon("start.png");
        ImageIcon endWordIcon = new ImageIcon("end.png");
        JLabel startWordLabel = new JLabel(new ImageIcon(startWordIcon.getImage().getScaledInstance(170, 40, Image.SCALE_SMOOTH)));
        JLabel endWordLabel = new JLabel(new ImageIcon(endWordIcon.getImage().getScaledInstance(170, 40, Image.SCALE_SMOOTH)));

        inputPanel.add(startWordLabel);
        inputPanel.add(startWordField);
        inputPanel.add(endWordLabel);
        inputPanel.add(endWordField);

        // Dropdown for selecting algorithm
        String[] algorithms = {"Uniform Cost Search", "Greedy Best-First Search", "A* Search"};
        JComboBox<String> algorithmChoice = new JComboBox<>(algorithms);
        ImageIcon algorithmIcon = new ImageIcon("algorithm.png");
        JLabel algorithmLabel = new JLabel(new ImageIcon(algorithmIcon.getImage().getScaledInstance(170, 40, Image.SCALE_SMOOTH)));

        inputPanel.add(algorithmLabel);
        inputPanel.add(algorithmChoice);

        // GridBag constraints for input panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;
        gbc.insets = new Insets(120, 0, 20, 0);
        backgroundPanel.add(inputPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        ImageIcon searchButtonIcon = new ImageIcon("search.png");
        JButton searchButton = new JButton(new ImageIcon(searchButtonIcon.getImage().getScaledInstance(107, 38, Image.SCALE_SMOOTH)));
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(createRoundRectBorder(10, 1, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));

        buttonPanel.add(searchButton);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        backgroundPanel.add(buttonPanel, gbc);

        // Result panel
        JPanel resultPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(resultImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        resultPanel.setOpaque(false);
        resultPanel.setPreferredSize(new Dimension(800, 300));
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        backgroundPanel.add(resultPanel, gbc);
    }

    // Rounded rectangle border helper method
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WordLadderGUI().setVisible(true);
            }
        });
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
