package com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction.Type;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransactionGroup;

/**
 * PDF parser for ING bank.
 *
 * @author mariangolea@gmail.com
 */
public class INGParser extends AbstractBankParser {

    public enum OperationID {
        CASH_WITHDRAWAL("Retragere numerar"),
        RATE_CREDIT("Rata Credit"),
        ASIGURARI_GENERALE("Prima asigurari generale"),
        INCASARE("Incasare"),
        COMISION("Comision plata anticipata"),
        RECALCULARI_CREDITE("Recalculari aferente creditului"),
        TRANSFER_HOMEBANK("Transfer Home'Bank"),
        PRIMA_ASIGURARE_ING("Prima asigurare ING Credit Protect"),
        PRIMA_ASIGURARE_VIATA("Prima asigurare de viata (credite)");

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

    public INGParser() {
        super(Bank.ING, new SimpleDateFormat("dd-MM-yyyy"), NumberFormat.getInstance(ROMANIAN_LOCALE));
    }

    @Override
    public PdfPageParseResponse parsePageResponse(List<String> split) {
        PdfPageParseResponse response = new PdfPageParseResponse();
        Map<String, List<BankTransaction>> result = new HashMap<>();
        for (OperationID operation : OperationID.values()) {
            result.put(operation.desc, new ArrayList<>());
        }
        List<String> toConsume = new ArrayList<>(split);
        List<String> unrecognizedStrings = new ArrayList<>();
        BankTransaction transaction;
        int consumedLines;
        while (!toConsume.isEmpty()) {
            transaction = parseTransaction(toConsume);
            if (transaction == null) {
                if (toConsume.get(0) != null && !toConsume.get(0).isEmpty()) {
                    unrecognizedStrings.add(toConsume.get(0));
                }
                consumedLines = 1;
            } else {
                result.get(transaction.title).add(transaction);
                consumedLines = transaction.getOriginalPDFContentLinesNumber();
            }
            toConsume = toConsume.subList(consumedLines, toConsume.size());
        }

        List<BankTransactionGroup> groups = new ArrayList<>();
        result.keySet().forEach((operationID) -> {
            List<BankTransaction> transactions = result.get(operationID);
            if (transactions != null && !transactions.isEmpty()) {
                groups.add(new BankTransactionGroup(operationID, result.get(operationID)));
            }
        });

        response.transactionGroups.addAll(groups);
        response.unrecognizedStrings.addAll(unrecognizedStrings);
        return response;
    }

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
                    completedDate = parseCompletedDate(
                            operationDetails[1].substring(0, operationDetails[1].indexOf(amountString)));
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

            transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, operationType, toConsume.subList(0, linesConsumed));
        }

        return transaction;
    }
}
