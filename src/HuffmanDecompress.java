import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;


/**
 * Responsible for handling all operations related to Huffman Decompression
 */
public class HuffmanDecompress {
    // static attribute
    // encodedString retrieved from file to decompress
    private static String encodedString;

    /**
     * Used to decompress Text Files
     * @param initFile textFile to decompress
     */
    public static void decompressTextFile(TextFile initFile) {
        // decompressedFile -> where decompressed file would be stored
        TextFile decompressedFile = new TextFile
                (HuffmanCompress.getCustomPath(initFile, "_decoded"));
        System.out.println("\nDECOMPRESSING...\n");

        // encodedBytes retrieved from the initFile
        byte[] encodedBytes = initFile.readBinaryFile();
        // encodedBytes converted to encodedStrings
        encodedString = convertToString(encodedBytes);

        // getting binaryValueList from initFile + _list path
        ArrayList<Node> binaryValueList = getBinaryValueList(initFile); // getting binaryValueList
        // finding the maximum frequency in binaryValueList
        int maxFrequency = getMaxFrequency(binaryValueList); // getting maxFrequency

        // sorting binaryValueList in ascending frequency order
        ArrayList<ArrayList<Node>> sortedValueList = sortBinaryValueList(binaryValueList);

        // decoding encodedString into readable text (decompressedText)
        String decompressedText = decompressBinaryToText(encodedString, sortedValueList, maxFrequency);
        // writing decompressed text to given textFile
        decompressedFile.writeFile(decompressedText, true);

        // Additional info
        System.out.println("Compressed file stored in " + decompressedFile.getFilePath());
    }

    /**
     * Given encodedBytes are converted into encodedString
     * @param encodedBytes given encodedBytes
     * @return encodedString
     */
    public static String convertToString(byte[] encodedBytes) {
        // Based on given bitStrings example (java version)
        StringBuilder encodedString = new StringBuilder(encodedBytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * encodedBytes.length; i++)
            encodedString.append((encodedBytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return encodedString.toString();
    }

    /**
     * Used to get the binaryValueList of given compressed file
     * @param compressedFile compressed file to refer to
     * @return binaryValueList of given file
     */
    public static ArrayList<Node> getBinaryValueList(TextFile compressedFile) {
        ArrayList<Node> binaryValueList = new ArrayList<>();
        // initiating file to retrieve binaryValueList from
        TextFile listFile = new TextFile(HuffmanCompress.getCustomPath(compressedFile, "_list"));

        String listString = listFile.readFile(); // retrieving content of listFile
        // splitting listString (each line as one array element)
        String[] list = listString.split("\n");

        // assigning the removeCount (last element of listFile)
        int removeCount = Integer.parseInt(list[list.length - 1]);
        // removing the extra0s from encodedString (added during compression when decoding into binary)
        encodedString = encodedString.substring(0, encodedString.length() - removeCount);

        char charValue;
        for (int i = 1; i < list.length - 1; i += 2) { // looping through listString
            // binaryValueList constructed as listString is looped through
            charValue = (char)(Integer.parseInt(list[i - 1]));
            binaryValueList.add(new Node(charValue, list[i]));
        } return binaryValueList;
    }

    /**
     * Used to build up the text content based on binaryValueList
     *
     * @param textContent binary values to decompress
     * @param sortedValueList sortedValueList to refer to when decompressing
     *
     * @param maxFrequency maximum possible frequency in sortedValueList
     * @return decompressed text content (on given interval)
     */
    public static String decompressBinaryToText(String textContent,
                                                ArrayList<ArrayList<Node>> sortedValueList,
                                                int maxFrequency) {
        int start = 0; // setting starting position
        StringBuilder buildContent = new StringBuilder(); // initialising the StringBuilder

        /*
         * Method loops through textContent, considering all possible frequency lengths and
         * decodes the part of the encoded binary string into a text when there is a match
         */

        // looping through until reaching the end of textContent
        while (textContent.length() >= start + maxFrequency) {
            // looping through each binary representation type
            // a -> 1010 is binary representation type 4 (length of 1010)
            outerLoop:
            for (int i = maxFrequency; i > 0; i--) {
                // only considering cases where binary representation of given type exists
                if (!(sortedValueList.get(i) == null)) {
                    // checks all possible binary values in given type (type -> i)
                    for (Node node : sortedValueList.get(i)) {
                        // checks if binary pointer matches part of the textContent
                        if (node.getPointer().equals(textContent.substring(start, start + i))) {
                            // appends the corresponding character to buildContent
                            buildContent.append(node.getValue());
                            start += i; // incrementing start when new element is appended

                            // ensuring textContent hasn't reached the end
                            if (textContent.length() < start + i)
                                break outerLoop;
                            break;
                        }
                    }
                }
            }
            /*
            if textContent.length is for example 100 and start = 97, maxFrequency = 6
            above while loop condition would be false, however there would still be 100-97 = 3
            possible bits to encode. Therefore, in this and similar situations, maxFrequency is adjusted
            to textContent.length - start (here 100-97 = 3) so that all bits are read
             */
            if ((textContent.length() <= start + maxFrequency) && (textContent.length() > start))
                maxFrequency = textContent.length() - start; // not to miss any characters
        }
        return buildContent.toString();
    }

    /**
     * Used to find the maximum frequency used in binaryValueList
     * @param binaryValueList binaryValueList to refer to
     * @return maximum frequency in binaryValueList
     */
    public static int getMaxFrequency(ArrayList<Node> binaryValueList) {
        int maxFrequency = binaryValueList.get(0).getPointer().length();

        for (Node node : binaryValueList) { // searching through all Nodes
            if (node.getPointer().length() > maxFrequency) {
                maxFrequency = node.getPointer().length();
            }
        } return maxFrequency; // returning obtained maxFrequency
    }

    /**
     * Used to sort Nodes of binaryValueList so that returned arrayList
     * would contain the following:
     * - in index 0 of arrayList: binary values with length 0 (null).
     * - index 1: binary values with length 1.
     * - index 2: binary values with length 2 and etc.
     *
     * This sorting is important as it significantly speeds up the
     * decompressTextInRange method (you don't have to loop
     * through binaryValueList each time)
     *
     * @param binaryValueList binaryValueList to sort
     * @return sorted binaryValueList
     */
    public static ArrayList<ArrayList<Node>> sortBinaryValueList(ArrayList<Node> binaryValueList) {
        ArrayList<ArrayList<Node>> sortedList = new ArrayList<>();
        // getting maximum frequency in binaryValueList
        int maxFrequency = getMaxFrequency(binaryValueList);
        int nodeIndex;

        for (int i = 0; i < maxFrequency + 1; i++) {
            sortedList.add(null); // adding maxFrequency number of slots
        }

        // assigning all Nodes in sortValueList
        for (Node node : binaryValueList) {
            nodeIndex = node.getPointer().length();
            ArrayList<Node> tempList = new ArrayList<>();

            if (!(sortedList.get(nodeIndex) == null))
                // getting all Nodes already inside sortedList
                // with current frequency length
                tempList = sortedList.get(nodeIndex);
            // adding current Node to that list
            tempList.add(node);

            // adding stored Nodes (in a list) to sortedList
            // index of sortedList refers to length of frequency in that slot
            sortedList.set(nodeIndex, tempList);
        } return sortedList;
    }

    /**
     * Used to compare the given 2 Strings
     *
     * @param text1 first String
     * @param text2 second String
     * @return similarity index (out of 1) between given strings
     */
    public static double compare(String text1, String text2) {
        String[] text1List = text1.split("\n");
        String[] text2List = text2.split("\n");
        String[] text1LL, text1W;
        String[] text2LL, text2W;

        double identicalLines = 0.0;
        int totalLines = Math.min(text1List.length, text2List.length);
        int totalLL, totalW;

        // checking individual lines
        for (int i = 0; i < totalLines; i++) {
            if (text1List[i].equals(text2List[i]))
                identicalLines++;
            else {
                // checking individual words
                text1LL = text1List[i].split(" ");
                text2LL = text2List[i].split(" ");
                totalLL = Math.min(text1LL.length, text2LL.length);

                for (int j = 0; j < totalLL; j++) {
                    if (text1LL[j].equals(text2LL[j]))
                        identicalLines += 1.0 / totalLL;
                    // checking individual letters
                    else {
                        text1W = text1LL[j].split("");
                        text2W = text2LL[j].split("");
                        totalW = Math.min(text1W.length, text2W.length);

                        for (int m = 0; m < totalW; m++) {
                            if (text1W[m].equals(text2W[m]))
                                identicalLines += (1.0 / totalW) / totalLL;
                        }
                    }
                }
            }
        } return identicalLines / totalLines;
    }

    /**
     * Used as a UI when decompressing
     * @param args command line user input
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("WELCOME TO THE HUFFMAN DECOMPRESSOR!");
        System.out.println("Enter path of the txt file to be decompressed: ");
        String filePath = scanner.nextLine(); // requesting input

        // checks if filePath ends with ".txt"
        if (filePath.endsWith(".txt")) {
            TextFile fileToDecompress = new TextFile(filePath);
            // decompressText
            decompressTextFile(fileToDecompress);
            UILikeness(fileToDecompress);
        } else
            System.err.println("Given path should be a txt file");
    }

    /**
     * Used as UI to check the degree of likeness of
     * the initial file and decompressed file
     * @param fileToDecompress textFile that was decompressed
     */
    public static void UILikeness(TextFile fileToDecompress) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to add the path of initial text file " +
                "(that was compressed) to check the accuracy of decompression? Y/N");
        String input = scanner.nextLine(); // requesting input

        if (input.toLowerCase(Locale.ROOT).equals("y")) {
            System.out.println("Enter path of the initial text file: ");
            String filePath = scanner.nextLine();
            // assigning values
            TextFile initialFile = new TextFile(filePath);
            TextFile decompressedFile = new TextFile
                    (HuffmanCompress.getCustomPath(fileToDecompress, "_decoded"));

            // checking if decompression was successful
            String initialContent = initialFile.readFile();
            String decompressedContent = decompressedFile.readFile();

            double likeness = compare(initialContent, decompressedContent);
            System.out.println(likeness + " of the decompressed file is identical with given file");
        }
    }
}
