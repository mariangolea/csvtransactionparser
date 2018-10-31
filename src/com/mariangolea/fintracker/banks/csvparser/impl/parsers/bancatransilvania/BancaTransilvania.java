package com.mariangolea.fintracker.banks.csvparser.impl.parsers.bancatransilvania;

import com.mariangolea.fintracker.banks.csvparser.api.parser.Bank;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

class BancaTransilvania extends Bank {

    public BancaTransilvania() {
        super(Locale.ENGLISH,
                "Data tranzactie,Data valuta,Descriere,Referinta tranzactiei,Debit,Credit,Sold contabil",
                "Gasit/e:", 
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("yyyy-MM-dd"),
                NumberFormat.getInstance(Locale.ENGLISH), 
                new int[]{0, 1, 2, 4, 5},
                6);
    }

}
