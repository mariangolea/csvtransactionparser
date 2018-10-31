package com.mariangolea.fintracker.banks.csvparser.impl;

import com.mariangolea.fintracker.banks.csvparser.impl.ui.CsvParserUI;
import javafx.application.Application;

public class BankCsvReportsParser {

    public CsvParserUI initApplication(){
        return new CsvParserUI();
    }
    
    public static void main(String[] args) {
        Application.launch(CsvParserUI.class, args);
    }
}
