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

/**
 * Uses apache's CSVBox to read the text from a csv file containing transactions
 * report from any supported banks.
 */
public final class BankCSVTransactionParser {

    /**
     * Parse contained bank transactions in associated csv file object.
     *
     * @param file has to represent the path to a csv file containing bank
     * generated transactions report.
     * @return parse response, may be null.
     */
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

    /**
     * Reads all file content, each line of text as a string within the response
     * list.
     *
     * @param csvFile csv file
     * @return may be null;
     */
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

    /**
    Look ahead in all CSV file content and match strings against
    all Bank instances relevant content header line fields.
    @param fileLines
    @return may return null if bank not supported yet
     */
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
