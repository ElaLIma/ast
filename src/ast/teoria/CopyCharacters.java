/*
 * CopyCharacters to illustrate how Characters stream works
 */
package ast.teoria;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author andony
 */
public class CopyCharacters {

    public static void main(String[] args) throws IOException {

        FileReader inputStream = null;
        FileWriter outputStream = null;
        String separator = File.separator;

        String inputfile = "assets" + separator + "inputs" + separator + "xanadu.txt";
        String outputfile = "assets" + separator + "outputs" + separator + "characteroutput.txt";

        try {
            inputStream = new FileReader(inputfile);
            outputStream = new FileWriter(outputfile);

            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
