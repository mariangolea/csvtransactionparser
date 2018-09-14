package com.mariangolea.fintracker.banks.csvparser.parsers.impl;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;
import java.io.File;
import java.math.BigDecimal;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.csv.CSVRecord;

/**
 * CSV parser for Banca Transilvania bank.
 *
 * @author mariangolea@gmail.com
 */
public class BTParser extends AbstractBankParser {
    
    /**
    Enum of all known transaction identifier strings.
    */
    public enum OperationID {
        PLATA_POS("Plata la POS"), INCASARE("Incasare"), TRANSFER_RECUPERARE_RESTANTE(
                "Transfer pentru recuperare restante");

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
    private BigDecimal amountValidationCounter = BigDecimal.ZERO;

    public BTParser() {
        super(Bank.BT, new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd-MM-yyyy"),
                NumberFormat.getInstance(Bank.BT.locale));
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
        return 1;
    }

    @Override
    public CsvFileParseResponse parseCsvResponse(List<String> split, File file) {
        amountValidationCounter = BigDecimal.ZERO;
        return super.parseCsvResponse(split, file); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BankTransaction parseTransaction(List<String> toConsume) {
        Objects.requireNonNull(toConsume);
        if (toConsume.size() != 1 || toConsume.get(0).isEmpty()) {
            return null;
        }
        // 2018-05-12,2018-05-12
        //,"Plata la POS non-BT cu card VISA;EPOS 10/05/2018 71003100        TID:71003101 ENEL ENERGIE MUNTENIA  BUCURESTI RO 41196811 valoare tranzactie: 77.06 RON RRN:813009844291   comision tranzactie 0.00 RON;"
        //,746NVPO1813200BQ,"-77.06",,"-396.66"
        CSVRecord record = parseSingleLine(toConsume.get(0));
        if (record == null || record.size() < 6) {
            return null;
        }

        String temp = record.get(0);
        if (temp == null) {
            return null;
        }
        Date startedDate = parseStartDate(temp.trim());

        temp = record.get(1);
        if (temp == null) {
            return null;
        }
        Date completedDate = parseCompletedDate(temp.trim());

        temp = record.get(2);
        if (temp == null) {
            return null;
        }
        OperationID operation = OperationID.getOperationID(temp.trim());
        String title = operation == null ? temp.trim() : operation.desc;

        BankTransaction.Type type = BankTransaction.Type.OUT;
        temp = record.get(4);
        if (temp == null || temp.isEmpty()) {
            temp = record.get(5);
            type = BankTransaction.Type.IN;
        }
        BigDecimal amount = parseAmount(temp.trim());
        if (amount == BigDecimal.ZERO){
            return null;
        }
        boolean companyDescFound = false;
        boolean amountValdationSuccess = false;

        temp = record.get(6);
        if (temp == null) {
            return null;
        }
        String validationAmountString = temp.trim();
        if (validationAmountString != null && !validationAmountString.isEmpty()) {
            BigDecimal validationAmount = parseAmount(validationAmountString);
            amountValdationSuccess = validateAmount(amount, validationAmount);
        }
        String companyDesc = record.get(2);
        int index = companyDesc.indexOf("TID:");
        if (index >= 0) {
            companyDesc = companyDesc.substring(index + "TID:".length());
            companyDesc = companyDesc.substring(companyDesc.indexOf(" "), companyDesc.length());
            index = companyDesc.indexOf("valoare tranzactie");
            if (index >= 0) {
                companyDesc = companyDesc.substring(0, index).trim();
                index = companyDesc.lastIndexOf(" ");
                if (index >= 0) {
                    companyDesc = companyDesc.substring(0, index).trim();
                    companyDescFound = true;
                }
            }
        }

        if (companyDescFound) {
            if (amountValdationSuccess) {
                return createStrictDescriptionTransaction(title, startedDate, completedDate, amount.abs(), companyDesc, type, toConsume);
            } else {
                return createCompanyIdDescriptionTransaction(title, startedDate, completedDate, amount.abs(), companyDesc, type, toConsume);
            }
        } else {
            return createGeneralTransaction(title, startedDate, completedDate, amount.abs(), companyDesc, type, toConsume);
        }
    }

    private boolean validateAmount(BigDecimal amount, BigDecimal expectedTotal) {
        if (amountValidationCounter == BigDecimal.ZERO) {
            //first transaction parsed since reinitialization of validation
            //first transaction in a document.
            amountValidationCounter = expectedTotal;
            return true;
        } else {
            if (amountValidationCounter.add(amount) == expectedTotal) {
                amountValidationCounter = expectedTotal;
                return true;
            }

            return false;
        }
    }
}
