package com.mariangolea.fintracker.banks.csvparser.impl.parsers.ing;

import com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParser;
import static com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParser.ROMANIAN_LOCALE;
import com.mariangolea.fintracker.banks.csvparser.api.parser.Bank;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

class ING extends Bank {

    public ING() {
        super(AbstractBankParser.ROMANIAN_LOCALE,
                "Data,,,Detalii tranzactie,,Debit,Credit",
                "", 
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("yyyy-MM-dd"),
                NumberFormat.getInstance(ROMANIAN_LOCALE),
                new int[]{0, 3, 5, 6},
                7);
    }

}
