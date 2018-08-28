package com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;

/**
 * PDF parser for BRD bank.
 *
 * @author mariangolea@gmail.com
 */
public class BRDParser implements BankTextReportParser {

    @Override
    public PdfPageParseResponse parseTransactions(final String file) {
        throw new RuntimeException("Not supported ATM.");
    }
}
