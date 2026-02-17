// Mehmet Arda Kutlu

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes the string output to the output.txt
 */
public class Writer {
    private FileWriter fileWriter;
    // Use BufferedWriter to write content.
    private BufferedWriter outputWriter;

    /**
     * Creates a writer object that writes the output to the output file
     * (output.txt for the standard part, bonus.txt for the bonus part).
     * @throws IOException if the file cannot be created or accessed
     */
    public Writer(String fileName) throws IOException {
        fileWriter = new FileWriter(fileName);
        outputWriter = new BufferedWriter(fileWriter);
    }

    /**
     * Closes the BufferedWriter to avoid unexpected behavior.
     * @throws IOException if the closing fails
     */
    public void closeBuffer() throws IOException {
        outputWriter.close();
    }

    /**
     * Writes the given string to the output.txt.
     * Does not add a newline unless included in the string.
     * @param toWrite the string content to write to the file
     * @throws IOException if writing fails
     */
    public void write(String toWrite) throws IOException {
        outputWriter.write(toWrite);
    }
}
