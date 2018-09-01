package com.mariangolea.fintracker.banks.pdfparser.api;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransactionGroup;
import com.mariangolea.fintracker.banks.pdfparser.parsers.PdfPageParseResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Response after a whole PDF file is parsed.
 * <br> Contains full list of detected transactions, a allOK boolean, and a list
 * of unidentified Strings if any.
 *
 * @author mariangolea@gmail.com
 */
public class PdfFileParseResponse {

    public final File pdfFile;
    public final boolean allOK;
    public final List<PdfPageParseResponse> pageResponses;
    public final List<BankTransactionGroup> parsedTransactionGroups;
    public final List<String> unprocessedStrings = new ArrayList<>();

    private final BankTransactionMergeUtils utils = new BankTransactionMergeUtils();

    public PdfFileParseResponse(final File pdfFile, final List<PdfPageParseResponse> pageResponses) {
        this.pdfFile = pdfFile;
        boolean success = true;
        this.pageResponses = pageResponses;
        List<BankTransactionGroup> temp = new ArrayList<>();
        for (PdfPageParseResponse pageResponse : pageResponses) {
            temp.addAll(pageResponse.transactionGroups);
            unprocessedStrings.addAll(pageResponse.unrecognizedStrings);
            success = success && (unprocessedStrings == null || unprocessedStrings.isEmpty());
        }
        allOK = success;
        parsedTransactionGroups = utils.mergeGroups(temp);
    }
}
