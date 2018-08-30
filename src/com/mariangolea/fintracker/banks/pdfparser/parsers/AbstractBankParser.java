/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common behavior of all bank pdf parsers.
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public abstract class AbstractBankParser implements BankTextReportParser{

    protected static final Locale ROMANIAN_LOCALE = new Locale.Builder().setLanguage("ro").setRegion("RO")
            .setLanguageTag("ro-RO").build();
    private final DateFormat romanianDateFormat = DateFormat.getDateInstance(DateFormat.LONG, ROMANIAN_LOCALE);

    private final Bank bank;
    private final NumberFormat numberFormat;
    private final DateFormat startDateFormat;

    AbstractBankParser(final Bank bank, final DateFormat startDateFormat, final NumberFormat numberFormat) {
        Objects.requireNonNull(bank);
        Objects.requireNonNull(startDateFormat);
        Objects.requireNonNull(numberFormat);

        this.bank = bank;
        this.startDateFormat = startDateFormat;
        this.numberFormat = numberFormat;
    }

    public abstract PdfPageParseResponse parsePageResponse(List<String> relevantLines);
    
    @Override
    public PdfPageParseResponse parseTransactions(String pdfPage) {
        if (pdfPage == null) {
            return null;
        }
        int relevantLineIndex = pdfPage.indexOf(bank.relevantContentHeaderLine);
        if (relevantLineIndex < 0) {
            return null;
        }

        String relevant = pdfPage.substring(relevantLineIndex + bank.relevantContentHeaderLine.length());
        String[] split = relevant.split(bank.lineSeparator);
        Map<String, List<BankTransaction>> result = new HashMap<>();
        
        PdfPageParseResponse response = parsePageResponse(Arrays.asList(split));
        return response;
    }

    public Date parseCompletedDate(final String dateString) {
        Date completedDate = null;
        try {
            completedDate = romanianDateFormat.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return completedDate;
    }

    public Date parseStartDate(final String dateString) {
        Date completedDate = null;
        try {
            completedDate = startDateFormat.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return completedDate;
    }

    public Float parseAmount(final String amountString) {
        Float amount = null;
        try {
            Number attempt = numberFormat.parse(amountString);
            if (null != attempt) {
                amount = attempt.floatValue();
            }
        } catch (ParseException ex) {
            Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return amount;
    }

}
