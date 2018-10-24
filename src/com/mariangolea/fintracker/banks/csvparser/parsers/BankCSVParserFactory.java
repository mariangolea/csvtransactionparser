package com.mariangolea.fintracker.banks.csvparser.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.BTParser;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.INGParser;

public class BankCSVParserFactory {
    
    public static AbstractBankParser getInstance(final Bank bank) {
        if (bank == null){
            return null;
        }
        switch (bank) {
            case BT:
                return new BTParser();
            case ING:
                return new INGParser();
            default:
                return null;
        }
    }
}
