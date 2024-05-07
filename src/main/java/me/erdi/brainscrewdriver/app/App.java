package me.erdi.brainscrewdriver.app;

import me.erdi.brainscrewdriver.BS2BFIterator;
import me.erdi.brainscrewdriver.TokenType;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class App implements Runnable {
    private File input;
    private File output;

    public static void main(String[] args) {
        new App().run();
    }

    @Override
    public void run() {
        prompt();
    }

    private void prompt() {
        String message = "Export?";

        if(output == null)
            message = "Select Output File";

        if(input == null)
            message = "Select Input File";

        int action = JOptionPane.showOptionDialog(null,
                message,
                "Choose an action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {
                        "Input File (BrainScrewdriver / BS)",
                        "Output File (BrainFuck)",
                        (input != null && output != null) ? "Export" : "",
                        "Cancel"
                },
                0
        );

        switch(action) {
        case -1:
        case 3:
            return;
        case 0: {
            JFileChooser chooser = new JFileChooser();

            int fileAction = chooser.showOpenDialog(null);
            if(fileAction != JFileChooser.APPROVE_OPTION)
                break;

            input = chooser.getSelectedFile();

            break;
        }
        case 1: {
            JFileChooser chooser = new JFileChooser();

            int fileAction = chooser.showOpenDialog(null);
            if(fileAction != JFileChooser.APPROVE_OPTION)
                break;

            output = chooser.getSelectedFile();

            break;
        }
        case 2:
            if(input != null && output != null)
                export();
        }

        prompt();
    }

    private void export() {
        System.out.println("Exporting..!");

        BS2BFIterator iter;
        try {
            iter = new BS2BFIterator(new FileInputStream(input));
        } catch(FileNotFoundException e) {
            throw new RuntimeException("Exception thrown during export", e);
        }

        try(BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(output.toPath()))) {
            while(iter.hasNext()) {
                TokenType next = iter.next();
                if(next != TokenType.NONE)
                    outputStream.write(next.getPlain());
            }
        } catch(IOException e) {
            throw new RuntimeException("Exception thrown during export", e);
        }

        JOptionPane.showMessageDialog(null, "Exported " + output.length() + "B file to " + output.toPath());
    }
}
