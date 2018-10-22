package com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUI;
import javafx.application.Application;

/**
 * UI component for parsing bank csv transaction reports parser.
 */
public class BankCsvReportsParser {

    public CsvParserUI initApplication(){
        CsvParserUI application = new CsvParserUI();
        return application;
    }
    
    /**
     * Entry point of the UI application.
     * @param args irrelevant
     */
    public static void main(String[] args) {
        Application.launch(CsvParserUI.class, args);
    }
}
