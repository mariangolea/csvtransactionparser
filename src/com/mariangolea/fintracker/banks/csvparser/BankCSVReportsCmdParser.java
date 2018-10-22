package com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.cmdsupport.CmdArgParser;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * CMD Utility to call the bank report CSV parser functionality using command line.
 * <br> Useful in basic validations of expected behavior.
 */
public class BankCSVReportsCmdParser {

    private final CmdArgParser cmdParser = new CmdArgParser();

    /**
     * 
     * @param args
     * @return 
     */
    public List<CsvFileParseResponse> parseInput(final String[] args) {
        List<File> inputFiles = cmdParser.getCSVFiles(args);
        BankCSVTransactionParser fac = new BankCSVTransactionParser();
        List<CsvFileParseResponse> res = new ArrayList<>();
        if (inputFiles == null) {
            return res;
        }
        inputFiles.forEach((file) -> {
            res.add(fac.parseTransactions(file));
        });

        return res;
    }

    public static void main(final String[] args) {
        BankCSVReportsCmdParser argParser = new BankCSVReportsCmdParser();
        List<CsvFileParseResponse> responses = argParser.parseInput(args);
        System.out.println("Parsed " + responses.size() + " input files.");
        for (CsvFileParseResponse response : responses) {
            System.out.println("\t" + response.csvFile.getAbsolutePath() + ":");
            System.out.println("\t\t - unparsedContent: " + (response.allCsvContentProcessed ? "NONE" : response.unprocessedStrings.size() + "Lines of text."));
            System.out.println("\t\t - parser used: " + response.parserUsed.getBank().name());
            System.out.println("\t\t - parser supports finding label for expected transactions: " + !response.parserUsed.getBank().transactionsNumberLabel.isEmpty());
            System.out.println("\t\t - expected transactions number: " + response.expectedTransactionsNumber);
            System.out.println("\t\t - found transactions number: " + response.foundTransactionsNumber);
            System.out.println("\t\t - Unprocessed Strings: ");
            response.unprocessedStrings.forEach((unprocessed) -> {
                System.out.println("\t\t\t - " + unprocessed);
            });
            System.out.println("\t\t - Unprocessed Strings: ");
        }
    }
}
