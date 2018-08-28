package com.mariangolea.fintracker.banks.pdfparser;

import java.io.File;
import java.util.List;

import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import com.mariangolea.fintracker.banks.pdfparser.cmdsupport.CmdArgParser;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;
import java.util.ArrayList;

/**
 * Utility to call the bank report PDF parser functionality using command line.
 * <br> Will later be used in black box automated tests.
 * @author mariangolea@gmail.com
 */
public class BankPDFReportsCmdParser {

    public static void main(final String[] args) {
        CmdArgParser cmdParser = new CmdArgParser();
        List<File> inputFiles = cmdParser.getPDFFiles(args);
        BankPDFTransactionParser fac = new BankPDFTransactionParser();
        List<PdfFileParseResponse> res = new ArrayList<>();
        inputFiles.forEach((file) -> {
            res.add(fac.parseTransactions(file));
        });

        System.out.println("Found: " + res.size() + " transactions in all .pdf files.");
    }
}
