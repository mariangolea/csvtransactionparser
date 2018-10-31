package com.mariangolea.fintracker.banks.csvparser.api.parser;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public abstract class AbstractBankParser {

    public static final Locale ROMANIAN_LOCALE = new Locale.Builder().setLanguage("ro").setRegion("RO")
            .setLanguageTag("ro-RO").build();

    public static final String UNRECOGNIZED_TRANSACTION = "Unrecognized Transaction";

    public final Bank bank;

    private final Map<String, Date> parsedCompletedDates = new HashMap<>();
    private final Map<String, Date> parsedStartedDates = new HashMap<>();
    private final Map<String, BigDecimal> parsedAmounts = new HashMap<>();

    public AbstractBankParser(final Bank bank) {
        this.bank = Objects.requireNonNull(bank);
    }

    public abstract BankTransaction parseTransaction(List<String> toConsume);

    public abstract int findNextTransactionLineIndex(List<String> toConsume);

    public Bank getBank() {
        return bank;
    }

    public CsvFileParseResponse parseCsvResponse(List<String> split, File file) {
        List<BankTransaction> result = new ArrayList<>();

        List<String> toConsume = new ArrayList<>(split);
        int transactionsIndex = getTransactionsIndex(toConsume);
        if (transactionsIndex < 0) {
            return null;
        }

        //find out transactions number if possible.
        final BigDecimal expectedTransactions = searchTransactionsNumber(transactionsIndex, toConsume);

        //remove header lines up to first transaction
        toConsume = toConsume.subList(transactionsIndex + 1, toConsume.size());

        List<String> unrecognizedStrings = new ArrayList<>();
        BankTransaction transaction;
        int consumedLines;
        int foundTransactionsNumber = 0;
        while (!toConsume.isEmpty()) {
            int nextTransactionIndex = findNextTransactionLineIndex(toConsume);
            List<String> transactionLines = toConsume;
            if (nextTransactionIndex > 0) {
                transactionLines = toConsume.subList(0, nextTransactionIndex);
            }
            transaction = parseTransaction(transactionLines);
            if (transaction == null) {
                for (String transactionString : transactionLines) {
                    if (transactionString != null && !transactionString.isEmpty()) {
                        unrecognizedStrings.addAll(transactionLines);
                    }
                }
                consumedLines = transactionLines.size();
            } else {
                foundTransactionsNumber++;
                result.add(transaction);
                consumedLines = transaction.getCSVContentLines();
            }
            toConsume = toConsume.subList(consumedLines, toConsume.size());
        }

        return new CsvFileParseResponse(this, expectedTransactions.intValue(), foundTransactionsNumber, file, result, unrecognizedStrings);
    }

    public Date parseCompletedDate(final String dateString) {
        return parseDate(dateString, parsedCompletedDates, bank.completedDateFormat);
    }

    public Date parseStartDate(final String dateString) {
        return parseDate(dateString, parsedStartedDates, bank.startDateFormat);
    }

    public BigDecimal parseAmount(final String amountString) {
        if (amountString == null || amountString.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        final String preparedString = amountString.trim();
        BigDecimal amount = parsedAmounts.get(preparedString);
        if (amount != null) {
            return amount;
        }

        try {
            Number attempt = bank.numberFormat.parse(preparedString);
            if (null != attempt) {
                amount = new BigDecimal(attempt.toString());
            }
        } catch (ParseException ex) {
            Logger.getLogger(AbstractBankParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        parsedAmounts.put(preparedString, amount);
        return amount;
    }

    public final CSVRecord parseSingleLine(final String singleLine) {
        Reader in = new StringReader(singleLine);
        CSVRecord record = null;
        try {
            CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);
            List<CSVRecord> list = parser.getRecords();
            record = list.get(0);
            if (!mandatoryChecks(record)) {
                return null;
            }
        } catch (IOException ex) {
            Logger.getLogger(AbstractBankParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return record;
    }

    public BankTransaction createTransaction(final Date startDate, final Date completedDate,
            BigDecimal creditAmount, BigDecimal debitAmount, final String description, final List<String> csvContent) {
        return new BankTransaction(startDate, completedDate, creditAmount, debitAmount, description,
                csvContent);
    }

    private boolean mandatoryChecks(final CSVRecord record) {
        if (record == null || bank.maxRecordSize > record.size()) {
            return false;
        }

        for (int index : bank.mandatoryRecordIndexes) {
            if (record.get(index) == null) {
                return false;
            }
        }
        
        return true;
    }

    private BigDecimal searchTransactionsNumber(int transactionsIndex, List<String> toConsume) {
        BigDecimal expectedTransactions = BigDecimal.ZERO;
        if (!bank.transactionsNumberLabel.isEmpty()) {
            for (int i = 0; i < transactionsIndex; i++) {
                if (toConsume.get(i).contains(bank.transactionsNumberLabel)) {
                    String[] toParse = toConsume.get(i).split(",");
                    if (toParse != null && toParse.length == 2) {
                        String transactionsString = toParse[1];
                        if (transactionsString.contains(" ")) {
                            transactionsString = transactionsString.substring(0, transactionsString.indexOf(" "));
                        }
                        expectedTransactions = parseAmount(transactionsString);
                        break;
                    }
                }
            }
        }

        return expectedTransactions;
    }

    private int getTransactionsIndex(final List<String> search) {
        for (int i = 0; i < search.size(); i++) {
            if (search.get(i).contains(bank.relevantContentHeaderLine)) {
                return i;
            }
        }

        return -1;
    }

    private Date parseDate(final String dateString, final Map<String, Date> cache, final DateFormat dateFormat) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        final String prepared = dateString.trim();
        Date startedDate = cache.get(prepared);
        if (startedDate != null) {
            return startedDate;
        }
        try {
            startedDate = dateFormat.parse(prepared);
        } catch (ParseException ex) {
            Logger.getLogger(AbstractBankParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (startedDate != null) {
            cache.put(prepared, startedDate);
        }
        return startedDate;
    }

}
