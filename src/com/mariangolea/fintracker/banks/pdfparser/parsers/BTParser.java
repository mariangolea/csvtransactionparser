package com.mariangolea.fintracker.banks.pdfparser.parsers;


import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;

/**
 * PDF parser for Banca Transilvania bank.
 *
 * @author mariangolea@gmail.com
 */
public class BTParser implements BankTextReportParser {

    @Override
    public PdfPageParseResponse parseTransactions(final String file) {
        throw new RuntimeException("Not supported ATM.");
    }
}
