package com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;
import java.util.Locale;

/**
 * Enum of major banks (Romania for now). <br>
 * Each enum contains links to bank's swift code (needed to identify which bank
 * generated the csv report), a dedicated parser object, and the locale used to
 * generate the report (impacts the way dates and currency amounts are parsed).
 *
 * @author mariangolea@gmail.com
 */
public enum Bank {
    ING(AbstractBankParser.ROMANIAN_LOCALE, 
            "Data,,,Detalii tranzactie,,Debit,Credit",
            ""),
    BT(Locale.ENGLISH, 
            "Data tranzactie,Data valuta,Descriere,Referinta tranzactiei,Debit,Credit,Sold contabil", 
            "Gasit/e:");

    public final Locale locale;
    public final String relevantContentHeaderLine;
    public final String transactionsNumberLabel;

    private Bank(final Locale locale, final String relevantContentHeaderLine, final String transactionsNumberLabel) {
        this.locale = locale;
        this.relevantContentHeaderLine = relevantContentHeaderLine;
        this.transactionsNumberLabel = transactionsNumberLabel;
    }
}
