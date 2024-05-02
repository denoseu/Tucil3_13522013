public class Node {
    private String word;
    private int fn;
    private Node parent;

    public Node(String word, Node parent, int fn) {
        this.word = word;
        this.fn = fn;
        this.parent = parent;
    }

    public String getWord() {
        return word;
    }

    public int getFn() {
        return fn;
    }

    public Node getParent() {
        return parent;
    }
}
