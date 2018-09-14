package com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUICategorizer;

import javax.swing.*;

/**
 * UI component for parsing bank csv transaction reports parser.
 *
 * @author mariangolea@gmail.com
 */
public class BankCsvReportsParser {
    private final CsvParserUICategorizer panel = new CsvParserUICategorizer();
    
    /**
     * Creates the JFrame of this application.
     * @return {@link JFrame}
     */
    public JFrame initFrame(){
        JFrame frame = new JFrame();
        frame.setTitle("Bank Transactions Merger");
        frame.setJMenuBar(panel.createMenu());
        frame.setContentPane(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        return frame;
    }
    
    /**
     * Entry point of the UI application.
     * @param args irrelevant
     */
    public static void main(String[] args) {
        BankCsvReportsParser parser = new BankCsvReportsParser();
        JFrame frame = parser.initFrame();
        frame.setVisible(true);
    }
}
