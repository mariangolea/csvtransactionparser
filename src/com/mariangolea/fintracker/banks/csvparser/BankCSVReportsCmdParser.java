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
    private final CmdArgParser cmdParser = new CmdArgParser();;
    
    public List<CsvFileParseResponse> parseInput(String[] args){
        List<File> inputFiles = cmdParser.getCSVFiles(args);
        BankCSVTransactionParser fac = new BankCSVTransactionParser();
        List<CsvFileParseResponse> res = new ArrayList<>();
        if (inputFiles == null){
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
        for (CsvFileParseResponse response : responses){
            System.out.println("\t" + response.csvFile.getAbsolutePath() + ":");
            System.out.println("\t\t - allOK: " + response.allOK);
            System.out.println("\t\t - transaction groups: " + response.parsedTransactionGroups.size());
            System.out.println("\t\t - Unprocessed Strings: " );
            for (String unprocessed : response.unprocessedStrings){
                System.out.println("\t\t\t - " + unprocessed);
            }
            System.out.println("\t\t - Unprocessed Strings: " );
        }
    }
}
