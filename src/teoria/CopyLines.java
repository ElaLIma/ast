/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teoria;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

/**
 *
 * @author andony
 */
public class CopyLines {

    public static void main(String[] args) throws IOException {

        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        String separator = File.separator;

        String inputfile = "assets" + separator + "inputs" + separator + "xanadu.txt";
        String outputfile = "assets" + separator + "outputs" + separator + "characteroutput.txt";

        try {
            inputStream = new BufferedReader(new FileReader(inputfile));
            outputStream = new PrintWriter(new FileWriter(outputfile));

            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
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
