import java.util.*;


/**
 * Responsible for handling all operations related to Huffman Compression
 */
public class HuffmanCompress {
    // static attribute
    // total number of ASCII characters
    private static final int CHARACTER_COUNT = 256;
    // sorted binary value list
    private static ArrayList<Node> binaryValueList = new ArrayList<>();
    // number of 0s added to encoded binary string
    private static String removeCount;

    /**
     * Used to compress Text Files
     * @param initFile textFile to compress
     */
    public static void compressTextFile(TextFile initFile) {
        System.out.println("\nCOMPRESSING...\n");
        // compressedFile -> where compressed file would be stored
        TextFile compressedFile = new TextFile(getCustomPath(initFile, "_compressed"));

        String textContent = initFile.readFile(); // 1. getting the content of initFile
        int[] frequencyList = setFrequency(textContent); // 2. setting the frequency of content
        TreeNode rootNode = buildTree(frequencyList); // 3. building a tree based on set frequency

        // 4. generating a binaryValueList based on the built Tree
        binaryValueList = setBinaryValueList(rootNode);
        // 5. generating encoded data String based on lookupTable
        // encodedString (example value) = 001001011011
        String encodedString = genEncodedString(textContent, binaryValueList);

        // 6. writing the encoded String to required file in bits
        byte[] bytes = convertToBinary(encodedString); // decoding encodedString into bytes
        compressedFile.writeBinaryFile(bytes); // writing compressed bytes
        // 7. storing BinaryValueList (which would be referred to when decompressing)
        storeBinaryValueList(compressedFile);

        // Comparison: Before / After / Reduction %
        System.out.println("Before compression: " + textContent.length());
        System.out.println("After compression: " + bytes.length);
        System.out.println("Reduction in size: " + ((textContent.length() - bytes.length)
                / (double)textContent.length()) + "%");

        // Additional info
        System.out.println("compressed file stored in " + compressedFile.getFilePath());
    }

    /**
     * Decodes the given encodedString into encodedBytes
     * @param encodedString encodedString to refer to
     * @return the encodedBytes
     */
    public static byte[] convertToBinary(String encodedString) {
        // Based on given bitStrings example (java version)
        StringBuilder encodedStringBuilder = new StringBuilder(encodedString);
        // extra 0s added to the end of encodedString to be divisible by 8
        int extra0s = 0;
        while (encodedStringBuilder.length() % 8 != 0) {
            // 0 added if encodedString is not divisible by 8
            encodedStringBuilder.append('0');
            extra0s++;
        }
        encodedString = encodedStringBuilder.toString();

        // declaring encodedBytes list
        byte[] encodedBytes = new byte[(encodedString.length() / 8) + 1];

        for (int i = 0; i < encodedString.length(); i++) {
            char c = encodedString.charAt(i);
            // if binary value is 1
            if (c == '1') {
                encodedBytes[i >> 3] |= 0x80 >> (i & 0x7);
            } else if (c != '0') { // if binary value is neither 1 nor 0 (wrong format)
                throw new IllegalArgumentException("non-binary string in given file");
            }
        }
        // extra0s assigned to removeCount
        removeCount = String.valueOf(extra0s);
        return encodedBytes;
    }


    /**
     * Gets the frequency of characters of given String
     * @param textContent given String content
     * @return int[] containing character index and their frequency
     */
    public static int[] setFrequency(String textContent) {
        // frequency list initiated with size 256 (number of ASCII characters)
        int[] frequencyList = new int[CHARACTER_COUNT];
        for (char character : textContent.toCharArray()) {
            try { // current character frequency
                frequencyList[character]++;
            } catch (IndexOutOfBoundsException e) {
                // character is outside ASCII bound
                frequencyList[63]++; // adding special character as '?'
            }
        } return frequencyList;
    }

    /**
     * Used to construct the Tree for the Huffman Encoding
     * @param frequencyList frequencyList to use when constructing the Tree
     * @return returns the Constructed Tree
     */
    private static TreeNode buildTree(int[] frequencyList) {
        // initialising the Tree object
        Tree tree = new Tree();

        for (char i = 0; i < CHARACTER_COUNT; i++) {
            if(frequencyList[i] > 0) { // adding an empty TreeNode if given character has frequency > 0
                tree.nodeList.add(new TreeNode(i, frequencyList[i], null, null)); // new leaf node
            }
        }
        // extreme case with nodeList.size() == 1
        if (tree.nodeList.size() == 1) {
            TreeNode child1 = tree.getMinNode();
            TreeNode child2 = new TreeNode('\0', 1, null, null);
            return new TreeNode
                    ('\0', child1.getFrequency() + child2.getFrequency(), child1, child2);
        }

        // loop used to construct the tree
        while (tree.nodeList.size() > 1) {
            // getting two nodes from the Tree (nodes with lowest frequency)
            TreeNode child1 = tree.getMinNode();
            TreeNode child2 = tree.getMinNode();

            // merging the child Nodes into 1 parent Node
            // parent Node has a reference of both child Nodes
            assert child2 != null;
            TreeNode parentNode = new TreeNode
                    ('\0', child1.getFrequency() + child2.getFrequency(), child1, child2);
            tree.nodeList.add(parentNode); // adding constructed parent Node into the nodeList

        } return tree.getMinNode(); // returning the root value
    }

    /**
     * Used to initiate the assignment of characters with their binary value
     * @param rootNode root TreeNode of the Tree
     * @return array containing characters and their binary value
     */
    public static ArrayList<Node> setBinaryValueList(TreeNode rootNode) {
        ArrayList<Node> binaryValueList = new ArrayList<>();
        // assigning values using recursion
        genBinaryValueList(rootNode, "", binaryValueList);
        return binaryValueList;
    }

    /**
     * Assigns binary values of each character using recursion.
     * Child Nodes of rootNode are called until reaching a Leaf Node (binary values 0/1
     * also being added in the process), where character and final binary value
     * is added to binaryValueList
     *
     * @param rootNode current rootNode
     * @param code accumulated binary code
     * @param binaryValueList binaryValueList to store in
     */
    private static void genBinaryValueList(TreeNode rootNode,
                                           String code,
                                           ArrayList<Node> binaryValueList) {
        if (rootNode.isLeaf()) // if Lead Node is reached
            // final binary value and character are added
            binaryValueList.add(new Node(rootNode.getValue(), code));
        else {
            // both sides of the rootNode are called (process follows until reaching a Leaf Node)
            // respective binary codes are also accumulated
            genBinaryValueList(rootNode.getChild1(), code + '0', binaryValueList);
            genBinaryValueList(rootNode.getChild2(), code + '1', binaryValueList);
        }
    }

    /**
     * Combines binary values of characters in the order of initial text Content
     *
     * @param textContent content of the file to refer to
     * @param binaryValueList binaryValueList to refer to
     * @return generated encodedString
     */
    public static String genEncodedString(String textContent,
                                          ArrayList<Node> binaryValueList) {
        StringBuilder builder = new StringBuilder();
        // for each character in textContent
        for (char character : textContent.toCharArray()) {
            for (Node node : binaryValueList) {
                if (node.getValue() == character) { // if character matches to Node
                    // adds encoded value of current character
                    builder.append(node.getPointer());
                    break;
                }
            }
        } return builder.toString(); // returns built encodedString
    }

    /**
     * Used to store the BinaryValueList in compressedFile + _list.txt path.
     * BinaryValueList is stored to be available for decompressing
     *
     * @param compressedFile compressed file to refer to
     */
    public static void storeBinaryValueList(TextFile compressedFile) {
        TextFile listFile = new TextFile(getCustomPath(compressedFile, "_list"));

        listFile.writeListFile(binaryValueList);
        // removeCount is also added to the end of the file
        // removeCount helps determine how many 0s where added to encodedString
        // when decoding encodedString into encodedBytes
        listFile.writeFile("\n" + removeCount, false);
    }

    /**
     * Used to get a custom path for given TextFile
     * @param compressedFile TextFile to get the custom path of
     * @return built custom path
     */
    public static String getCustomPath(TextFile compressedFile, String customString) {
        String filePath = compressedFile.getFilePath(); // getting initial path
        // adding customString the end of the text file
        String newPath = filePath.substring(0, filePath.length() - 4);
        newPath += customString + ".txt";
        return newPath;
    }

    /**
     * Used as a UI when compressing
     * @param args command line user input
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("WELCOME TO THE HUFFMAN COMPRESSOR!");
        System.out.println("Enter path of the txt file to be compressed: ");
        String filePath = scanner.nextLine();

        // checks if filePath ends with ".txt"
        if (filePath.endsWith(".txt")) {
            TextFile file1 = new TextFile(filePath);
            // compressTextFile
            compressTextFile(file1);
        } else
            System.err.println("Given path should be a txt file");
    }
}
