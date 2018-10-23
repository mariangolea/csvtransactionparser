package com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;
import java.util.Locale;

public enum Bank {
    ING(AbstractBankParser.ROMANIAN_LOCALE,
            "Data,,,Detalii tranzactie,,Debit,Credit",
            ""),
    BT(Locale.ENGLISH,
            "Data tranzactie,Data valuta,Descriere,Referinta tranzactiei,Debit,Credit,Sold contabil",
            "Gasit/e:");

    public final Locale locale;
    /**
     * Each CSV file has a header that the parser needs to skip when looking for
     * transactions.
     * <br>This represents a perfect match of what the header line string looks
     * like in a CSV report file for this bank.
     */
    public final String relevantContentHeaderLine;
    /**
     * Some banks add CSV content to specify the generated transactions number.
     * <br> If a certain bank does not provide one, this field will be a empty
     * string.
     * <br> It represents the label that associated parser needs to look for in
     * order to next parse the transactions number, for validation purposes.
     */
    public final String transactionsNumberLabel;

    private Bank(final Locale locale, final String relevantContentHeaderLine, final String transactionsNumberLabel) {
        this.locale = locale;
        this.relevantContentHeaderLine = relevantContentHeaderLine;
        this.transactionsNumberLabel = transactionsNumberLabel;
    }
}
