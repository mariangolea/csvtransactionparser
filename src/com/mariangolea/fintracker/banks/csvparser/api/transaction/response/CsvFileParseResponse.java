package com.mariangolea.fintracker.banks.csvparser.api.transaction.response;

import com.mariangolea.fintracker.banks.csvparser.api.BankTransactionMergeUtils;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroup;

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
    public final boolean allOK;
    public final List<BankTransactionGroup> parsedTransactionGroups = new ArrayList<>();
    public final List<String> unprocessedStrings = new ArrayList<>();

    private final BankTransactionMergeUtils utils = new BankTransactionMergeUtils();

    public CsvFileParseResponse(final File csvFile, final List<BankTransactionGroup> groups, final List<String> unprocessedStrings) {
        Objects.requireNonNull(csvFile);
        Objects.requireNonNull(groups);
        Objects.requireNonNull(unprocessedStrings);
        this.csvFile = csvFile;
        this.unprocessedStrings.addAll(unprocessedStrings);
        allOK = unprocessedStrings == null || unprocessedStrings.isEmpty();
        parsedTransactionGroups.addAll(utils.mergeGroups(groups));
    }
}
