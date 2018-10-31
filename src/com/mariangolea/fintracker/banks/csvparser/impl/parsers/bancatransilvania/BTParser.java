package com.mariangolea.fintracker.banks.csvparser.impl.parsers.bancatransilvania;

import com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParser;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.csv.CSVRecord;

public class BTParser extends AbstractBankParser {

    public BTParser() {
        super(new BancaTransilvania());
    }

    @Override
    public int findNextTransactionLineIndex(List<String> toConsume) {
        return 1;
    }

    @Override
    public BankTransaction parseTransaction(List<String> toConsume) {
        Objects.requireNonNull(toConsume);
        if (toConsume.size() != 1 || toConsume.get(0).isEmpty()) {
            return null;
        }
        
        CSVRecord record = parseSingleLine(toConsume.get(0));
        if (record == null) {
            return null;
        }

        Date startedDate = parseStartDate(record.get(0));
        Date completedDate = parseCompletedDate(record.get(1));
        String desc = record.get(2).trim();
        BigDecimal credit = parseAmount(record.get(4));
        BigDecimal debit = parseAmount(record.get(5));
        
        if (credit == BigDecimal.ZERO && debit == BigDecimal.ZERO) {
            return null;
        }

        return createTransaction(startedDate, completedDate, credit.abs(), debit.abs(), desc, toConsume);
    }
}
