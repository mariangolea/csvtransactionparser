package com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;

/**
 *
 * @author mariangolea@gmail.com
 */
public class BankPDFParserFactory {

    public static BankTextReportParser getInstance(final Bank bank) {
        switch (bank) {
            case BCR:
                return new BCRParser();
            case BRD:
                return new BRDParser();
            case BT:
                return new BTParser();
            case ING:
                return new INGParser();
        }
        
        return null;
    }
}
