package com.mariangolea.fintracker.banks.csvparser.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileReader;

public final class BankCSVTransactionParser {

    public CsvFileParseResponse parseTransactions(final File file) {
        CsvFileParseResponse fileResponse = null;
        if (file == null) {
            return fileResponse;
        }
        List<String> lines = loadCSVFile(file);

        Bank bank = recognizeBank(lines);
        if (bank != null) {
            AbstractBankParser parser = BankCSVParserFactory.getInstance(bank);
            // identify parser based on bank swift code being expected in first page header.
            if (parser != null) {
                fileResponse = parser.parseCsvResponse(lines, file);
            } else {
                Logger.getLogger(BankCSVTransactionParser.class.getName()).log(Level.SEVERE, null,
                        new Exception("No parser defined for file: " + file.getAbsolutePath()));
            }
        }

        return fileResponse;
    }

    public List<String> loadCSVFile(final File csvFile) {
        final List<String> response = new ArrayList<>();
        try (BufferedReader csvReader = new BufferedReader(new FileReader(csvFile))) {
            String tempLine;
            while ((tempLine = csvReader.readLine()) != null) {
                response.add(tempLine);
            }
        } catch (IOException ex) {
            Logger.getLogger(BankCSVTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;
    }

    public Bank recognizeBank(final List<String> fileLines) {
        int index;
        for (Bank bank : Bank.values()) {
            index = fileLines.indexOf(bank.relevantContentHeaderLine);
            if (index >= 0) {
                return bank;
            }
        }
        return null;
    }
}
