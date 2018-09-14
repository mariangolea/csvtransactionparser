package com.mariangolea.fintracker.banks.csvparser.api.transaction.response;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionAbstractGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionUtils;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionDefaultGroup;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Response after a whole CSV file is parsed.
 * <br> Contains full list of detected transactions, a allOK boolean, and a list
 * of unidentified Strings if any.
 *
 * @author mariangolea@gmail.com
 */
public class CsvFileParseResponse {

    public final File csvFile;
    //true if there are no unprocessed strings.
    public final boolean allCsvContentProcessed;
    public final int expectedTransactionsNumber;
    public final int foundTransactionsNumber;
    public final List<BankTransactionAbstractGroup> parsedTransactionGroups = new ArrayList<>();
    public final List<String> unprocessedStrings = new ArrayList<>();
    public final AbstractBankParser parserUsed;

    private final BankTransactionUtils utils = new BankTransactionUtils();

    public CsvFileParseResponse(
            AbstractBankParser parserUsed, 
            int expectedTransactionsNumber,
            int foundTransactionsNumber,
            final File csvFile,
            final List<BankTransactionDefaultGroup> groups,
            final List<String> unprocessedStrings) {
        Objects.requireNonNull(csvFile);
        Objects.requireNonNull(groups);
        Objects.requireNonNull(unprocessedStrings);
        Objects.requireNonNull(parserUsed);
        this.parserUsed = parserUsed;
        this.csvFile = csvFile;
        this.unprocessedStrings.addAll(unprocessedStrings);
        parsedTransactionGroups.addAll(utils.processTransactions(groups));
        this.expectedTransactionsNumber = expectedTransactionsNumber;
        this.foundTransactionsNumber = foundTransactionsNumber;

        allCsvContentProcessed = unprocessedStrings == null || unprocessedStrings.isEmpty();
    }
}
