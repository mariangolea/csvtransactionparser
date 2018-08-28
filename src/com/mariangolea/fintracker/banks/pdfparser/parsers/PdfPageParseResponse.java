package com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTransactionGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple container needed to gather reports from various pages.
 * @author mariangolea@gmail.com
 */
public class PdfPageParseResponse {
    public int pageNumber;
    public final List<BankTransactionGroup> transactionGroups = new ArrayList<>();
    public final List<String> unrecognizedStrings = new ArrayList<>();
}
