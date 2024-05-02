package src;
public class NodeA {
    private String word;
    private NodeA parent;
    private int gCost; // Cost from the start node to this node
    private int hCost; // Heuristic cost from this node to the goal
    private int fCost; // Total cost, f(n) = gCost + hCost

    public NodeA(String word, NodeA parent, int gCost, int hCost) {
        this.word = word;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost; // Calculate total cost immediately upon node creation
    }

    public String getWord() {
        return word;
    }

    public NodeA getParent() {
        return parent;
    }

    public int getGCost() {
        return gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFn() {
        return fCost;
    }
}
