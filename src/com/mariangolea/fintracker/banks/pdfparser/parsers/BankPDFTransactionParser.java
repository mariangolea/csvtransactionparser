package com.mariangolea.fintracker.banks.pdfparser.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransactionMergeUtils;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uses apache's PDFBox to read the text from a pdf file containing transactions
 * report from any supported banks.
 *
 * @author mariangolea@gmail.com
 */
public final class BankPDFTransactionParser{
    private final BankTransactionMergeUtils utils = new BankTransactionMergeUtils();
    
    /**
     * Parse contained bank transactions in associated pdf file object.
     *
     * @param file has to represent the path to a pdf file containing bank
     * generated transactions report.
     * @return list of all recognized {@linkplain  BankTransaction} objects.
     */
    public PdfFileParseResponse parseTransactions(final File file) {
        List<PdfPageParseResponse> result = new ArrayList<>();
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(1);
            String firstPage = pdfStripper.getText(document);
            BankTextReportParser parser = null;
            for (Bank bank : Bank.values()) {
                if (firstPage.contains(bank.swiftCode)) {
                    parser = BankPDFParserFactory.getInstance(bank);
                    break;
                }
            }

            if (parser != null) {
                PdfPageParseResponse response = parser.parseTransactions(firstPage);
                response.setPageNumber(1);
                result.add(response);
                for (int i = 2; i <= document.getNumberOfPages(); i++) {
                    pdfStripper.setStartPage(i);
                    pdfStripper.setEndPage(i);
                    firstPage = pdfStripper.getText(document);
                    response = parser.parseTransactions(firstPage);
                    response.setPageNumber(i);
                    result.add(response);
                }
            } else{
                Logger.getLogger(BankPDFTransactionParser.class.getName()).log(Level.SEVERE, null, new Exception("No parser defined for file: " + file.getAbsolutePath()));
            }

        } catch (IOException ex) {
            Logger.getLogger(BankPDFTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException ex) {
                    Logger.getLogger(BankPDFTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return new PdfFileParseResponse(file, result);
    }
}
