package com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.cmdsupport.CmdArgParser;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to call the bank report CSV parser functionality using command line.
 * <br> Will later be used in black box automated tests.
 *
 * @author mariangolea@gmail.com
 */
public class BankCSVReportsCmdParser {

    public static void main(final String[] args) {
        CmdArgParser cmdParser = new CmdArgParser();
        List<File> inputFiles = cmdParser.getCSVFiles(args);
        BankCSVTransactionParser fac = new BankCSVTransactionParser();
        List<CsvFileParseResponse> res = new ArrayList<>();
        inputFiles.forEach((file) -> {
            res.add(fac.parseTransactions(file));
        });

        System.out.println("Found: " + res.size() + " transactions in all .csv files.");
    }
}
