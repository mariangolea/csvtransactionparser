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

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionDefaultGroup;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.BTParser;
import java.math.BigDecimal;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class BankTransactionDefaultGroupTest {

    @Test
    public void testGroup() {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ONE, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        BankTransaction second = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ONE, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));

        BankTransactionDefaultGroup firstGroup = new BankTransactionDefaultGroup(BTParser.OperationID.INCASARE.desc,
                BankTransaction.Type.IN);
        firstGroup.addTransaction(first);
        firstGroup.addTransaction(second);

        //different title
        BankTransaction illegalOne = new BankTransaction(true, true, "title", date, date, BigDecimal.ONE, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        boolean success = firstGroup.addTransaction(illegalOne);
        assertTrue(!success);
        //different type
        BankTransaction illegalTwo = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ONE, "description",
                BankTransaction.Type.OUT, Arrays.asList("one", "two"));
        success = firstGroup.addTransaction(illegalTwo);
        assertTrue(!success);

        List<BankTransaction> badTransactions = firstGroup.addTransactions(Arrays.asList(illegalOne, illegalTwo));
        assertTrue(badTransactions != null && badTransactions.size() == 2 && badTransactions.get(0) == illegalOne && badTransactions.get(1) == illegalTwo);

        assertTrue(firstGroup.getType() == BankTransaction.Type.IN);
        assertTrue(firstGroup.getGroupIdentifier().equals(BTParser.OperationID.INCASARE.desc));
        assertTrue(firstGroup.getTotalAmount().intValue() == 2);
        assertTrue(firstGroup.getCompanyDescriptions().equals(Arrays.asList("description")));
        assertTrue(firstGroup.getTransactionsForCompanyDesc("description").equals(Arrays.asList(first, second)));
        assertTrue(firstGroup.toString() != null && !firstGroup.toString().isEmpty());

        first = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ONE, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        second = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ONE, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        BankTransactionDefaultGroup secondGroup = new BankTransactionDefaultGroup(BTParser.OperationID.INCASARE.desc,
                BankTransaction.Type.IN);
        secondGroup.addTransaction(first);
        secondGroup.addTransaction(second);

        assertTrue(firstGroup.equals(secondGroup));
        assertTrue(firstGroup.hashCode() == secondGroup.hashCode());
        BankTransactionDefaultGroup thirdGroup = new BankTransactionDefaultGroup(BTParser.OperationID.INCASARE.desc,
                BankTransaction.Type.IN);
        List<BankTransaction> incompatibleTransactions = thirdGroup.addTransactions(Arrays.asList(first, second));
        assertTrue(incompatibleTransactions != null && incompatibleTransactions.isEmpty());
        assertTrue(thirdGroup.equals(secondGroup));
        
        second.getCsvContent().add("hello");
        assertTrue(!secondGroup.equals(firstGroup));
        assertTrue(secondGroup.hashCode() != firstGroup.hashCode());
    }
}
