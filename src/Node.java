/**
 * Node objet has a value and a pointer
 */
public class Node {
    // instance attributes
    // value -> stored character
    private char value;
    // pointer -> frequency of stored character
    private String pointer;

    /**
     * @return String representation of the Node
     */
    @Override
    public String toString() {
        return value + " -> " + pointer;
    }

    /**
     * Constructor for Node object
     * @param value character of the Node
     * @param pointer pointer of the Node
     */
    public Node(char value, String pointer) {
        setValue(value);
        setPointer(pointer);
    }

    // getter methods
    public char getValue() { return value; }
    public String getPointer() { return pointer; }
    // setter methods
    public void setValue(char value) { this.value = value; }
    public void setPointer(String pointer) { this.pointer = pointer; }

}

/**
 * TreeNode object, extends Node has two child TreeNodes
 */
class TreeNode extends Node {
    // instance attributes
    // frequency of the character
    private int frequency;
    // left child of the current TreeNode
    private TreeNode child1;
    // right child of the current TreeNode
    private TreeNode child2;

    /**
     * @return String representation of the node
     */
    @Override
    public String toString() {
        return getValue() + " -> " + frequency;
    }

    /**
     * @return true if TreeNode is a Leaf (has 0 child Nodes), false otherwise
     */
    public boolean isLeaf() {
        return child1 == null && child2 == null;
    }

    /**
     * Constructor for TreeNode object
     * @param value character of the TreeNode
     * @param frequency frequency of the assigned character
     * @param child1 right child of the TreeNode
     * @param child2 left child of the TreeNode
     */
    public TreeNode(char value, int frequency, TreeNode child1, TreeNode child2) {
        super(value, null);
        setFrequency(frequency);
        setChild1(child1);
        setChild2(child2);
    }

    // setter methods
    public int getFrequency() { return frequency; }
    public TreeNode getChild1() { return child1; }
    public TreeNode getChild2() { return child2; }
    // getter methods
    public void setFrequency(int frequency) { this.frequency = frequency; }
    public void setChild1(TreeNode child1) { this.child1 = child1; }
    public void setChild2(TreeNode child2) { this.child2 = child2; }
}