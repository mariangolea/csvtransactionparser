package com.mariangolea.fintracker.banks.pdfparser.api;

import com.mariangolea.fintracker.banks.pdfparser.parsers.PdfPageParseResponse;

/**
 * Interface for dedicated bank generated transactions reports which have been
 * converted from a PDF format to simple text in a String.
 *
 * @author mariangolea@gmail.com
 */
public interface BankTextReportParser {

    /**
     * Parse contained bank transactions in associated string file object.
     *
     * @param pdfPageContent string content parsed from a single pdf page.
     * @return list of all recognized {@linkplain  BankTransactionGroup} objects.
     */
    public PdfPageParseResponse parseTransactions(final String pdfPageContent);
}
