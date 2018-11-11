package com.mariangolea.fintracker.banks.csvparser.api.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractBankParserFactory {
    private final Map<Bank, AbstractBankParser> parsers = new HashMap<>();

    public AbstractBankParserFactory(final Collection<AbstractBankParser> knownParsers) {
        Objects.requireNonNull(knownParsers).forEach(parser ->{
            parsers.put(parser.bank, parser);
        });
    }
    
    public AbstractBankParser getParser(final List<String> fileLines){
        for (Bank bank : parsers.keySet()){
            if (matchesBank(bank, fileLines)){
                return parsers.get(bank);
            }
        }
        return null;
    }
    
    private boolean matchesBank(final Bank bank, final List<String> fileLines){
        return Objects.requireNonNull(fileLines).stream().anyMatch((line) -> (Objects.requireNonNull(line).contains(bank.relevantContentHeaderLine)));
    }
}
