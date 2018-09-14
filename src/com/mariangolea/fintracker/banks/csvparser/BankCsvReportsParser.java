package com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUICategorizer;

import javax.swing.*;

/**
 * Starts the UI component for parsing bank csv transaction reports parser.
 *
 * @author mariangolea@gmail.com
 */
public class BankCsvReportsParser {
    private final CsvParserUICategorizer panel = new CsvParserUICategorizer();
    
    public JFrame initFrame(){
        JFrame frame = new JFrame();
        frame.setTitle("Bank Transactions Merger");
        frame.setJMenuBar(panel.createMenu());
        frame.setContentPane(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        return frame;
    }
    
    public static void main(String[] args) {
        BankCsvReportsParser parser = new BankCsvReportsParser();
        JFrame frame = parser.initFrame();
        frame.setVisible(true);
    }
}
