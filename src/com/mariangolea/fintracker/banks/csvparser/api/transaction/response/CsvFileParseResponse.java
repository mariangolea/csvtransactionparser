package com.mariangolea.fintracker.banks.csvparser.api.transaction.response;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvFileParseResponse {

    public final File csvFile;
    public final boolean allCsvContentProcessed;
    public final int expectedTransactionsNumber;
    public final int foundTransactionsNumber;
    public final List<BankTransaction> parsedTransactions = new ArrayList<>();
    public final List<String> unprocessedStrings = new ArrayList<>();
    public final AbstractBankParser parserUsed;

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
