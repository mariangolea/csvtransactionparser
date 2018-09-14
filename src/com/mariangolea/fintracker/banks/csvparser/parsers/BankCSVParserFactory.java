package com.mariangolea.fintracker.banks.csvparser.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.BTParser;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.INGParser;

/**
   Factory for supported parsers.
 * @author mariangolea@gmail.com
 */
public class BankCSVParserFactory {
    
    /**
    Get the parser instance corresponding to mentioned bank.
    @param bank bank
    @return 
    */
    public static AbstractBankParser getInstance(final Bank bank) {
        switch (bank) {
            case BT:
                return new BTParser();
            case ING:
                return new INGParser();
        }

        return null;
    }
}
