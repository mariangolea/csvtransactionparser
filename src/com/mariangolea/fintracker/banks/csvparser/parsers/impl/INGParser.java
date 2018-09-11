package com.mariangolea.fintracker.banks.csvparser.parsers.impl;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction.Type;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CSV parser for ING bank.
 *
 * @author mariangolea@gmail.com
 */
public class INGParser extends AbstractBankParser {

    @Override
    public int findNextTransactionLineIndex(List<String> toConsume) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public enum OperationID {
        CASH_WITHDRAWAL("Retragere numerar"), RATE_CREDIT("Rata Credit"), ASIGURARI_GENERALE(
                "Prima asigurari generale"), INCASARE("Incasare"), COMISION(
                "Comision plata anticipata"), RECALCULARI_CREDITE(
                "Recalculari aferente creditului"), TRANSFER_HOMEBANK(
                "Transfer Home'Bank"), PRIMA_ASIGURARE_ING(
                "Prima asigurare ING Credit Protect"), PRIMA_ASIGURARE_VIATA(
                "Prima asigurare de viata (credite)");

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
    public void resetValidationCounters() {
        //not needed in this parser.
    }

    @Override
    public BankTransaction parseTransaction(List<String> toConsume) {
        OperationID operation = OperationID.getOperationID(toConsume.get(0));
        BankTransaction transaction = null;
        int linesConsumed = 0;
        if (operation != null) {
            String[] operationDetails = toConsume.get(0).split(operation.desc);
            if (operation == OperationID.PRIMA_ASIGURARE_VIATA) {
                operationDetails = toConsume.get(0).split("Prima asigurare de viata \\(credite\\)");
            }
            Float amount = OperationID.INCASARE == operation ? null : parseAmount(operationDetails[0]);
            Date completedDate = OperationID.INCASARE == operation ? null : parseCompletedDate(operationDetails[1]);
            Date startDate = completedDate;
            String desc = operation.desc;
            Type operationType = Type.OUT;
            switch (operation) {
                case RATE_CREDIT:
                    linesConsumed = 2;
                    break;
                case CASH_WITHDRAWAL:
                    String thirdLine = toConsume.get(3);
                    startDate = parseStartDate(thirdLine.split(" ")[1]);
                    desc = toConsume.get(2);
                    linesConsumed = 4;
                    break;
                case ASIGURARI_GENERALE:
                    desc = toConsume.get(2) + toConsume.get(3);
                    linesConsumed = 5;
                    break;
                case INCASARE:
                    String amountString = operationDetails[1].substring(operationDetails[1].lastIndexOf(" ") + 1);
                    amount = parseAmount(amountString);
                    String completedDateString = operationDetails[1].substring(0,
                            operationDetails[1].indexOf(amountString));
                    completedDate = parseCompletedDate(completedDateString);
                    startDate = completedDate;
                    desc = toConsume.get(3);
                    linesConsumed = 5;
                    operationType = Type.IN;
                    break;
                case COMISION:
                    linesConsumed = 2;
                    break;
                case PRIMA_ASIGURARE_ING:
                    linesConsumed = 2;
                    break;
                case PRIMA_ASIGURARE_VIATA:
                    linesConsumed = 1;
                    break;
                case RECALCULARI_CREDITE:
                    desc = toConsume.get(2);
                    linesConsumed = 4;
                    break;
                case TRANSFER_HOMEBANK:
                    desc = toConsume.get(4);
                    linesConsumed = 6;
                    break;
                default:
                    throw new RuntimeException("Unhandled Operation ID found for ING bank. Please update code!");
            }

            transaction = createGeneralTransaction(operation.desc, startDate, completedDate, amount, desc, operationType,
                    toConsume.subList(0, linesConsumed));
        }

        return transaction;
    }
}
