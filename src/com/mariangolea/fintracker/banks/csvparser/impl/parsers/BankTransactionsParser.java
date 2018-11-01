package com.mariangolea.fintracker.banks.csvparser.impl.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.AbstractBankTransactionParser;

public class BankTransactionsParser extends AbstractBankTransactionParser{
    
    public BankTransactionsParser() {
        super(new BankParserFactory());
    }
    
}
