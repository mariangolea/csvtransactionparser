package com.mariangolea.fintracker.banks.csvparser.impl.parsers.ing;

import com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParser;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class INGParser extends AbstractBankParser {

    public INGParser() {
        super(new ING());
    }

    @Override
    public int findNextTransactionLineIndex(List<String> toConsume) {
        Reader in;
        CSVRecord record;
        String test;
        if (toConsume == null || toConsume.isEmpty()) {
            return -1;
        }

        for (int i = 1; i < toConsume.size(); i++) {
            test = toConsume.get(i);
            if (test == null || test.isEmpty()) {
                continue;
            }
            in = new StringReader(test);
            try {
                CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);
                List<CSVRecord> list = parser.getRecords();
                record = list.get(0);
                if (record == null || record.get(0) == null) {
                    continue;
                }
                Date date = parseCompletedDate(record.get(0));
                if (date != null) {
                    return i;
                }
            } catch (IOException ex) {
                Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    @Override
    public BankTransaction parseTransaction(List<String> toConsume) {
        Objects.requireNonNull(toConsume);
        CSVRecord record = parseSingleLine(toConsume.get(0));
        if (record == null) {
            return null;
        }

        Date completedDate = parseCompletedDate(record.get(0));
        String desc = record.get(3);
        BigDecimal debitAmount = parseAmount(record.get(5));
        BigDecimal creditAmount = parseAmount(record.get(6));
        if (creditAmount == BigDecimal.ZERO && debitAmount == BigDecimal.ZERO) {
            return null;
        }

        int detailsLinesNumber = toConsume.size() - 1;
        StringBuilder details = new StringBuilder();
        if (detailsLinesNumber > 0) {
            for (int i = 0; i < detailsLinesNumber; i++) {
                record = parseSingleLine(toConsume.get(i + 1));
                if (record != null && record.size() > 3) {
                    details.append(record.get(3)).append(",");
                }
            }
        }
        return createTransaction(completedDate, completedDate, creditAmount.abs(), debitAmount.abs(), desc + details.toString(), toConsume);
    }
}
