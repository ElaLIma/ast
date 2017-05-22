/*
 * CopyBytes class for illustrate how ByteStream works
 */
package ast.teoria;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

/**
 *
 * @author andony
 */
public class CopyBytes {

    public static void main(String[] args) throws IOException {

        FileInputStream in = null;
        FileOutputStream out = null;
        String separator = File.separator;

        String inputfile = "assets" + separator + "inputs" + separator + "xanadu.txt";
        String outputfile = "assets" + separator + "outputs" + separator + "outagain.txt";

        try {
            in = new FileInputStream(inputfile);
            out = new FileOutputStream(outputfile);
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
