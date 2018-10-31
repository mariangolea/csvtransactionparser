package com.mariangolea.fintracker.banks.csvparser.api.parser;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CsvFileParseResponse {

    public final File csvFile;
    public final boolean allCsvContentProcessed;
    public final int expectedTransactionsNumber;
    public final int foundTransactionsNumber;
    public final Collection<BankTransaction> parsedTransactions;
    public final Collection<String> unprocessedStrings;
    public final AbstractBankParser parserUsed;

    public CsvFileParseResponse(
            final AbstractBankParser parserUsed,
            int expectedTransactionsNumber,
            int foundTransactionsNumber,
            final File csvFile,
            final List<BankTransaction> transactions,
            final List<String> unprocessedStrings) {
        this.parserUsed = Objects.requireNonNull(parserUsed);
        this.csvFile = Objects.requireNonNull(csvFile);
        this.unprocessedStrings = Collections.unmodifiableCollection(Objects.requireNonNull(unprocessedStrings));
        parsedTransactions = Collections.unmodifiableCollection(Objects.requireNonNull(transactions));
        this.expectedTransactionsNumber = expectedTransactionsNumber;
        this.foundTransactionsNumber = foundTransactionsNumber;

        allCsvContentProcessed = unprocessedStrings == null || unprocessedStrings.isEmpty();
    }
}
