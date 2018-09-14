package com.mariangolea.fintracker.banks.csvparser.parsers.impl;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * CSV parser for ING bank.
 *
 * @author mariangolea@gmail.com
 */
public class INGParser extends AbstractBankParser {

    /**
    Enum of all known transaction identifier strings.
    */
    public enum OperationID {
        CASH_WITHDRAWAL("Retragere numerar"),
        RATE_CREDIT("Rata Credit"),
        ASIGURARI_GENERALE("Prima asigurari generale"),
        INCASARE("Incasare"),
        COMISION("Comision plata anticipata"),
        RECALCULARI_CREDITE("Recalculari aferente creditului"),
        TRANSFER_HOMEBANK("Transfer Home'Bank"),
        PRIMA_ASIGURARE_ING("Prima asigurare ING Credit Protect"),
        PRIMA_ASIGURARE_VIATA("Prima asigurare de viata (credite)"),
        CUMPARARE_POS("Cumparare POS"),
        RAMBURSARE_RATE_CREDIT("Rambursare rata card credit"),
        REALIMENTARE("Realimentare (debitare directa)"),
        CUMPARARE_POS_CORECTIE("Cumparare POS corectie");

        public final String desc;

        private OperationID(String desc) {
            this.desc = desc;
        }

        private static OperationID getOperationID(final String operationLine) {
            for (OperationID id : OperationID.values()) {
                if (operationLine.contains(id.desc)) {
                    return id;
                }
            }

            return null;
        }
    }

    private final List<String> operationDescriptionsList = new ArrayList<>();

    public INGParser() {
        super(Bank.ING, new SimpleDateFormat("dd-MM-yyyy"), NumberFormat.getInstance(ROMANIAN_LOCALE));
    }

    @Override
    public List<String> getListOfSupportedTransactionIDs() {
        if (operationDescriptionsList.isEmpty()) {
            for (OperationID operation : OperationID.values()) {
                operationDescriptionsList.add(operation.desc);
            }
        }
        return new ArrayList<>(operationDescriptionsList);
    }

    @Override
    public int findNextTransactionLineIndex(List<String> toConsume) {
        Reader in;
        CSVRecord record;
        String test;
        if (toConsume == null || toConsume.isEmpty()){
            return -1;
        }
        
        for (int i = 1; i < toConsume.size(); i++) {
            test = toConsume.get(i);
            if (test == null || test.isEmpty()) {
                continue;
            }
            in = new StringReader(test);
            try {
                CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);
                List<CSVRecord> list = parser.getRecords();
                record = list.get(0);
                if (record == null || record.get(0) == null) {
                    continue;
                }
                Date date = parseCompletedDate(record.get(0));
                if (date != null) {
                    return i;
                }
            } catch (IOException ex) {
                Logger.getLogger(BTParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    @Override
    public BankTransaction parseTransaction(List<String> toConsume) {
        Objects.requireNonNull(toConsume);
        /**
           Data,,,Detalii tranzactie,,Debit,Credit
         * 07 septembrie 2018,,,Transfer Home'Bank,,"4.400,00",
         * ,,,Beneficiar: Marian Golea,,,
         * ,,,In contul: RO52BTRLRONVGLD120898001,,,
           ,,,Banca: BTRA CENTRALA,,,
         * ,,,Detalii: acoperire card credit BT,,,
         * ,,,Referinta: 233640525,,,
         */
        CSVRecord record = parseSingleLine(toConsume.get(0));

        if (record == null || record.size() < 7) {
            return null;
        }

        String temp = record.get(0);
        Date completedDate = parseCompletedDate(temp.trim());
        if (completedDate == null){
            return null;
        }
        Date startedDate = completedDate;
        String desc = record.get(3);
        OperationID operation = OperationID.getOperationID(desc);
        if (operation == null){
            return null;
        }
        
        BankTransaction.Type type = BankTransaction.Type.OUT;
        int amountIndex = 5;
        if (record.get(amountIndex) == null || record.get(amountIndex).trim().isEmpty()){
            amountIndex = 6;
            type = BankTransaction.Type.IN;
        }
        BigDecimal amount =  parseAmount(record.get(amountIndex));
        if (amount == BigDecimal.ZERO) {
            return null;
        }

        String title = operation.desc;
        int detailsLinesNumber = toConsume.size() - 1;
        String details = "";
        if (detailsLinesNumber > 0) {
            for (int i = 0; i < detailsLinesNumber; i++) {
                record = parseSingleLine(toConsume.get(i + 1));
                if (record != null && record.size() > 3) {
                    temp = record.get(3);
                    if (temp != null && !temp.isEmpty()) {
                        details += temp + ",";
                    }
                }
            }
        }
        return createGeneralTransaction(title, startedDate, completedDate, amount.abs(), details, type, toConsume);
    }

}
