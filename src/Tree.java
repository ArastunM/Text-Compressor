import java.util.ArrayList;


/**
 * Tree object is used when constructing a Tree (Huffman Compression).
 * Tree object contains a getMinNode method and a
 * nodeList storing all of the TreeNodes
 */
public class Tree {
    // instance attribute
    // nodeList storing all required TreeNodes
    public ArrayList<TreeNode> nodeList = new ArrayList<>();

    /**
     * Used to return the TreeNode with lowest frequency from the treeList
     * and remove that TreeNode in the process
     *
     * @return Node with the lowest frequency
     */
    public TreeNode getMinNode() {
        TreeNode minNode = nodeList.get(0);
        // looping through all TreeNodes in nodeList
        for (TreeNode node : nodeList) {
            if (node.getFrequency() < minNode.getFrequency()) {
                minNode = node; // minNode so far
            }
        }
        nodeList.remove(minNode); // removing the TreeNode
        return minNode;
    }

    /**
     * Empty constructor for Tree object
     */
    public Tree() {}
}
