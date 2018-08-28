package com.mariangolea.fintracker.banks.pdfparser.parsers;


import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;

/**
 * PDF parser for BCR bank.
 *
 * @author mariangolea@gmail.com
 */
public class BCRParser implements BankTextReportParser {

    @Override
    public PdfPageParseResponse parseTransactions(final String file) {
        throw new RuntimeException("Not supported ATM.");
    }
}
