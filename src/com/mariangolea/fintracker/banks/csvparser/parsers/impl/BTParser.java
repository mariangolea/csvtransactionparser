package com.mariangolea.fintracker.banks.csvparser.parsers.impl;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;
import java.io.File;
import java.math.BigDecimal;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.csv.CSVRecord;

public class BTParser extends AbstractBankParser {

    public BTParser() {
        super(Bank.BT, new SimpleDateFormat("yyyy-mm-dd"), new SimpleDateFormat("yyyy-mm-dd"),
                NumberFormat.getInstance(Bank.BT.locale));
    }

    @Override
    public int findNextTransactionLineIndex(List<String> toConsume) {
        return 1;
    }

    @Override
    public CsvFileParseResponse parseCsvResponse(List<String> split, File file) {
        return super.parseCsvResponse(split, file); 
    }

    @Override
    public BankTransaction parseTransaction(List<String> toConsume) {
        Objects.requireNonNull(toConsume);
        if (toConsume.size() != 1 || toConsume.get(0).isEmpty()) {
            return null;
        }
        // 2018-05-12,2018-05-12
        //,"Plata la POS non-BT cu card VISA;EPOS 10/05/2018 71003100        TID:71003101 ENEL ENERGIE MUNTENIA  BUCURESTI RO 41196811 valoare tranzactie: 77.06 RON RRN:813009844291   comision tranzactie 0.00 RON;"
        //,746NVPO1813200BQ,"-77.06",,"-396.66"
        CSVRecord record = parseSingleLine(toConsume.get(0));
        if (record == null || record.size() < 6) {
            return null;
        }

        String temp = record.get(0);
        if (temp == null) {
            return null;
        }
        Date startedDate = parseStartDate(temp.trim());

        temp = record.get(1);
        if (temp == null) {
            return null;
        }
        Date completedDate = parseCompletedDate(temp.trim());

        temp = record.get(2);
        if (temp == null) {
            return null;
        }
        String desc = temp.trim();

        temp = record.get(4);
        BigDecimal credit = BigDecimal.ZERO;
        if (temp != null && !temp.isEmpty()) {
            credit = parseAmount(temp.trim());
        }
        
        temp = record.get(5);
        BigDecimal debit = BigDecimal.ZERO;
        if (temp != null && !temp.isEmpty()) {
            debit = parseAmount(temp.trim());
        }
        if (credit == BigDecimal.ZERO && debit == BigDecimal.ZERO) {
            return null;
        }

        temp = record.get(6);
        if (temp == null) {
            return null;
        }

        return createTransaction(startedDate, completedDate, credit.abs(), debit.abs(), desc, toConsume);
    }
}
