/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.api;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroup;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.BTParser;
import java.math.BigDecimal;

/**
 *
 * @author Marian Golea <marian.golea@microchip.com>
 */
public class BankTransactionGroupTest {

    @Test
    public void testGroup() {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ZERO, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        BankTransaction second = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ZERO, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));

        BankTransactionGroup firstGroup = new BankTransactionGroup(BTParser.OperationID.INCASARE.desc,
                BankTransaction.Type.IN);
        firstGroup.addTransaction(first);
        firstGroup.addTransaction(second);

        BankTransaction illegal = new BankTransaction(true, true, "title", date, date, BigDecimal.ONE, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        boolean success = firstGroup.addTransaction(illegal);
        assertTrue(!success);
        List<BankTransaction> badTransactions = firstGroup.addTransactions(Arrays.asList(illegal));
        assertTrue(badTransactions != null && badTransactions.size() == 1 && badTransactions.get(0) == illegal);

        assertTrue(firstGroup.getType() == BankTransaction.Type.IN);
        assertTrue(firstGroup.getGroupIdentifier().equals("title"));
        assertTrue(firstGroup.getTotalAmount().intValue()== 2);
        assertTrue(firstGroup.getTransactions().equals(Arrays.asList(first, second)));

        first = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ZERO, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        second = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ZERO, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        BankTransactionGroup secondGroup = new BankTransactionGroup(BTParser.OperationID.INCASARE.desc,
                BankTransaction.Type.IN);
        secondGroup.addTransaction(first);
        secondGroup.addTransaction(second);

        assertTrue(firstGroup.equals(secondGroup));

        BankTransactionGroup thirdGroup = new BankTransactionGroup(BTParser.OperationID.INCASARE.desc,
                BankTransaction.Type.IN);
        List<BankTransaction> incompatibleTransactions = thirdGroup.addTransactions(Arrays.asList(first, second));
        assertTrue(incompatibleTransactions != null && incompatibleTransactions.isEmpty());
        assertTrue(thirdGroup.equals(secondGroup));
    }
}
