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
 *
 * @author mariangolea@gmail.com
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
        List<String> lines = loadCSVFile(file);
        CsvFileParseResponse fileResponse = null;
        if (lines == null) {
            return fileResponse;
        }

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
        List<String> response = null;
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(csvFile));
            response = new ArrayList<>();
            String tempLine;
            while ((tempLine = csvReader.readLine()) != null) {
                response.add(tempLine);
            }
        } catch (IOException ex) {
            Logger.getLogger(BankCSVTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally{
            try {
                if (csvReader != null) csvReader.close();
            } catch (IOException ex) {
                Logger.getLogger(BankCSVTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return response;
    }

    public Bank recognizeBank(List<String> fileLines) {
        int index = 0;
        for (Bank bank : Bank.values()) {
            index = fileLines.indexOf(bank.relevantContentHeaderLine);
            if (index >= 0) {
                return bank;
            }
        }
        return null;
    }
}
