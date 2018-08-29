package com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTransactionGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple container needed to gather reports from various pages.
 * @author mariangolea@gmail.com
 */
public class PdfPageParseResponse {
    private int pageNumber;
    public final List<BankTransactionGroup> transactionGroups = new ArrayList<>();
    public final List<String> unrecognizedStrings = new ArrayList<>();

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    
}
