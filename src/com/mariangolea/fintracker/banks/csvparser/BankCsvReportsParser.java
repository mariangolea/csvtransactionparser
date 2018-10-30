package com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUI;
import javafx.application.Application;

public class BankCsvReportsParser {

    public CsvParserUI initApplication(){
        return new CsvParserUI();
    }
    
    public static void main(String[] args) {
        Application.launch(CsvParserUI.class, args);
    }
}
