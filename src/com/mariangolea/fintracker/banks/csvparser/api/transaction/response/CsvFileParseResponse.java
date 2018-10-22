package com.mariangolea.fintracker.banks.csvparser.api.transaction.response;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Response after a whole CSV file is parsed.
 * <br> Contains all data needed by upper layer of the application.
 */
public class CsvFileParseResponse {

    /**
    Original csv file.
     */
    public final File csvFile;
    /**
    Whether no unexpected strings were found.
     */
    public final boolean allCsvContentProcessed;
    /**
    If the CSV file contained a special record for stating number of
    contained transactions, and if the {@link com.mariangolea.fintracker.banks.csvparser.api.Bank}
    object is aware of it.
     */
    public final int expectedTransactionsNumber;
    /**
    Number of identified transactions.
     */
    public final int foundTransactionsNumber;
    /**
    List of groups of similar transactions found within the csv file.
     */
    public final List<BankTransaction> parsedTransactions = new ArrayList<>();
    /**
    Not recognized strings, if any.
     */
    public final List<String> unprocessedStrings = new ArrayList<>();
    /**
    Parser used during identification.
     */
    public final AbstractBankParser parserUsed;

    /**
    Construct a instance of this class,
    @param parserUsed used parser
    @param expectedTransactionsNumber if stated in csv file, 0 otherwise.
    @param foundTransactionsNumber number of identified transactions.
    @param csvFile csv file reference
    @param transactions parsed transactions.
    @param unprocessedStrings list of not recognized strings.
     */
    public CsvFileParseResponse(
            AbstractBankParser parserUsed,
            int expectedTransactionsNumber,
            int foundTransactionsNumber,
            final File csvFile,
            final List<BankTransaction> transactions,
            final List<String> unprocessedStrings) {
        Objects.requireNonNull(csvFile);
        Objects.requireNonNull(transactions);
        Objects.requireNonNull(unprocessedStrings);
        Objects.requireNonNull(parserUsed);
        this.parserUsed = parserUsed;
        this.csvFile = csvFile;
        this.unprocessedStrings.addAll(unprocessedStrings);
        parsedTransactions.addAll(transactions);
        this.expectedTransactionsNumber = expectedTransactionsNumber;
        this.foundTransactionsNumber = foundTransactionsNumber;

        allCsvContentProcessed = unprocessedStrings == null || unprocessedStrings.isEmpty();
    }
}
