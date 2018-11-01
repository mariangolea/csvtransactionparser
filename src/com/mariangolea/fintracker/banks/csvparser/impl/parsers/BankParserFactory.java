package com.mariangolea.fintracker.banks.csvparser.impl.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParserFactory;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.bancatransilvania.BTParser;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.ing.INGParser;
import java.util.Arrays;

public class BankParserFactory extends AbstractBankParserFactory{
    
    public BankParserFactory() {
        super(Arrays.asList(new INGParser(), new BTParser()));
    }
    
}
