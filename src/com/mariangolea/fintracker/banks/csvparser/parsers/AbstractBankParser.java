/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.csvparser.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction.Type;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionDefaultGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.INGParser;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Common behavior of all bank csv parsers.
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public abstract class AbstractBankParser {

    public static final Locale ROMANIAN_LOCALE = new Locale.Builder().setLanguage("ro").setRegion("RO")
            .setLanguageTag("ro-RO").build();

    public static final String UNRECOGNIZED_TRANSACTION = "Unrecognized Transaction";

    private final NumberFormat numberFormat;
    private final DateFormat startDateFormat;
    private final DateFormat completedDateFormat;
    private final Bank bank;

    private final Map<String, Date> parsedCompletedDates = new HashMap<>();
    private final Map<String, Date> parsedStartedDates = new HashMap<>();
    private final Map<String, BigDecimal> parsedAmounts = new HashMap<>();

    public AbstractBankParser(final Bank bank, final DateFormat startDateFormat, final NumberFormat numberFormat) {
        this(bank, startDateFormat, DateFormat.getDateInstance(DateFormat.LONG, ROMANIAN_LOCALE), numberFormat);
    }

    public AbstractBankParser(final Bank bank, final DateFormat startDateFormat, final DateFormat completedDateFormat,
            final NumberFormat numberFormat) {
        Objects.requireNonNull(startDateFormat);
        Objects.requireNonNull(numberFormat);
        Objects.requireNonNull(bank);

        this.bank = bank;
        this.startDateFormat = startDateFormat;
        this.numberFormat = numberFormat;
        this.completedDateFormat = completedDateFormat;
    }

    public abstract List<String> getListOfSupportedTransactionIDs();

    public abstract BankTransaction parseTransaction(List<String> toConsume);

    public abstract int findNextTransactionLineIndex(List<String> toConsume);
    
    public Bank getBank(){
        return bank;
    }
    
    public CsvFileParseResponse parseCsvResponse(List<String> split, File file) {
        Map<String, List<BankTransaction>> result = new HashMap<>();
        for (String operation : getListOfSupportedTransactionIDs()) {
            result.put(operation, new ArrayList<>());
        }
        result.put(UNRECOGNIZED_TRANSACTION, new ArrayList<>());

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
                List<BankTransaction> recognized = result.get(transaction.getTitle());
                if (recognized != null) {
                    recognized.add(transaction);
                } else {
                    result.get(UNRECOGNIZED_TRANSACTION).add(transaction);
                }

                consumedLines = transaction.getOriginalCSVContentLinesNumber();
            }
            toConsume = toConsume.subList(consumedLines, toConsume.size());
        }

        List<BankTransactionDefaultGroup> groups = new ArrayList<>();
        result.keySet().forEach((operationID) -> {
            List<BankTransaction> transactions = result.get(operationID);
            if (transactions != null && !transactions.isEmpty()) {
                BankTransactionDefaultGroup group = new BankTransactionDefaultGroup(operationID,
                        transactions.get(0).getType());
                group.addTransactions(transactions);
                groups.add(group);
            }
        });
        
        return new CsvFileParseResponse(this, expectedTransactions.intValue(), foundTransactionsNumber, file, groups, unrecognizedStrings);
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

    public Date parseCompletedDate(final String dateString) {
        Date completedDate = dateString == null ? null : parsedCompletedDates.get(dateString);
        if (completedDate != null) {
            return completedDate;
        }
        try {
            completedDate = completedDateFormat.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (completedDate != null) {
            parsedCompletedDates.put(dateString, completedDate);
        }
        return completedDate;
    }

    public Date parseStartDate(final String dateString) {
        Date startedDate = dateString == null ? null : parsedStartedDates.get(dateString);
        if (startedDate != null) {
            return startedDate;
        }
        try {
            startedDate = startDateFormat.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (startedDate != null) {
            parsedStartedDates.put(dateString, startedDate);
        }
        return startedDate;
    }

    public BigDecimal parseAmount(final String amountString) {
        BigDecimal amount = amountString == null ? null : parsedAmounts.get(amountString);
        if (amount != null) {
            return amount;
        }

        try {
            Number attempt = numberFormat.parse(amountString);
            if (null != attempt) {
                amount = new BigDecimal(attempt.toString());
            }
        } catch (ParseException ex) {
            Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (amount == null){
            amount = BigDecimal.ZERO;
        }
        parsedAmounts.put(amountString, amount);
        return amount;
    }

    public final CSVRecord parseSingleLine(String singleLine) {
        Reader in = new StringReader(singleLine);
        CSVRecord record = null;
        try {
            CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);
            List<CSVRecord> list = parser.getRecords();
            record = list.get(0);
        } catch (IOException ex) {
            Logger.getLogger(AbstractBankParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return record;
    }

    public BankTransaction createCompanyIdDescriptionTransaction(final String title, final Date startDate, final Date completedDate,
            BigDecimal amount, final String description, final Type type, final List<String> csvContent) {
        return new BankTransaction(false, true, title, startDate, completedDate, amount, description, type,
                csvContent);
    }

    public BankTransaction createStrictDescriptionTransaction(final String title, final Date startDate, final Date completedDate,
            BigDecimal amount, final String description, final Type type, final List<String> csvContent) {
        return new BankTransaction(true, true, title, startDate, completedDate, amount, description, type,
                csvContent);
    }

    public BankTransaction createGeneralTransaction(final String title, final Date startDate, final Date completedDate,
            BigDecimal amount, final String description, final Type type, final List<String> csvContent) {
        return new BankTransaction(false, false, title, startDate, completedDate, amount, description, type,
                csvContent);
    }
}
