import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * Used to handle all Text File related operations
 */
public class TextFile {
    // instance attributes
    // file path of the TextFile object
    private final String filePath;

    /**
     * Used to read from the text file and
     * return the content as a String
     *
     * @return content of the text file as String
     */
    public String readFile() {
        String line = "";
        String content = "";
        StringBuilder buildContent = new StringBuilder();

        try {
            // initiating FileReader and BufferedReader
            FileReader fReader = new FileReader(filePath, StandardCharsets.UTF_8);
            BufferedReader bReader = new BufferedReader(fReader);

            while (line != null) { // "line == null)" -> end of the file
                line = bReader.readLine();
                buildContent.append(line); // adding current line
                buildContent.append("\n"); // adding a new line
            }

            // checks if given textFile is empty
            if (buildContent.length() == 5)
                throw new IllegalArgumentException("Given text file is empty");

            bReader.close();
            // removing null from the end of the file
            // added at the end of the above while loop
            content = buildContent.substring(0, buildContent.length() - 6);

        } catch (FileNotFoundException e) { // handling exceptions
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Can not read from the file");
        } return content; // returns the content of the file
    }

    /**
     * Used to read encodedBytes from textFile
     * @return read list of encodedBytes
     */
    public byte[] readBinaryFile() {
        // plagiarised
        File file = new File(filePath);
        byte[] readBytes = new byte[0];

        try {
            // initiating the readBytes with length of given textFile
            readBytes = new byte[(int) file.length() - 1];
            FileInputStream fStreamer = new FileInputStream(filePath);
            fStreamer.read(readBytes); // reading from the fStreamer
            fStreamer.close();

        } catch (NegativeArraySizeException e) {
            throw new NegativeArraySizeException("File not Found");
        } catch (IOException e) {
            System.err.println("Can not read from the file");
        } return readBytes;
    }

    /**
     * Used to write to the text file
     * @param toAdd content to write into the file
     * @param reWrite if the file should be reWritten
     */
    public void writeFile(String toAdd, boolean reWrite) {
        if (textFileNew(filePath)) // ensures file with given path exists
            textFileCreate(filePath); // if not file with given path is created

        if (!reWrite) { // if not reWrite, current content of the file is kept
            String fileLines = this.readFile(); // reading existing content
            fileLines += toAdd; // merging 2 arrays
            toAdd = fileLines;
        }

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(toAdd); // toAdd is added to the textFile
            writer.close();
        } catch (IOException e) { // handling exceptions
            System.err.println("Can not read from the file");
        }
    }

    /**
     * Used to write given encodedBytes into textFile
     * @param toAdd list of encodedBytes to be added
     */
    public void writeBinaryFile(byte[] toAdd) {
        try {
            FileOutputStream fStreamer = new FileOutputStream(filePath);
            fStreamer.write(toAdd); // writing encodedBytes to the textFile

        } catch (FileNotFoundException e) { // handling exceptions
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Can not read from the file");
        }
    }

    /**
     * Used to write a list to the textFile
     * @param listToAdd list to add to the textFile
     */
    public void writeListFile(ArrayList<Node> listToAdd) {
        int charValue;
        if (textFileNew(filePath)) // ensure textFile with given path exists
            // if not textFile with given path is created
            textFileCreate(filePath);

        try {
            FileWriter writer = new FileWriter(filePath);
            for (Node node : listToAdd) { // looping through each Node
                charValue = node.getValue(); // writing int number of the character
                // this is done to avoid any confusions
                // e.g., adding '\n' would create another line in the textFile
                writer.write(charValue + "\n");
                writer.write(node.getPointer() + "\n"); // writing frequency of the character
            } writer.close();

        } catch (IOException e) { // handling exceptions
            System.err.println("Can not read from the file");
        }
    }

    /**
     * Used to create a text file with given filePath
     * @param filePath path of the textFile to be created
     */
    public static void textFileCreate(String filePath) {
        try {
            File newFile = new File(filePath);

            if (!newFile.createNewFile())
                System.err.println("File with given name already exists");
        } catch (IOException e) { // handling exceptions
            System.err.println("Can not read from the file");
        }
    }

    /**
     * Checks if the textFile with given filePath would be new
     * @param filePath path of the file to check
     * @return true if the file is new, false if it already exists
     */
    public static boolean textFileNew(String filePath) {
        try {
            new FileReader(filePath);
            return false;
        } catch (FileNotFoundException e) {
            return true;
        }
    }

    /**
     * Constructor for textFile object
     * @param filePath path of textFile to be constructed
     */
    public TextFile(String filePath) {
        // textFile is created if a file with given path/name does not exist
        // otherwise, filePath is simply assigned (no need to create a new file)
        if (textFileNew(filePath))
            textFileCreate(filePath);
        this.filePath = filePath;
    }

    // getter method for filePath
    public String getFilePath() { return filePath; }
}
