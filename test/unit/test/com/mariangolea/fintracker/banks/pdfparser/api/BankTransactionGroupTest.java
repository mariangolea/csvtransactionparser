/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.api;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransactionGroup;
import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Marian Golea <marian.golea@microchip.com>
 */
public class BankTransactionGroupTest {

    @Test
    public void testGroup() {
        INGParser ing = new INGParser();
        Date date = ing.parseCompletedDate("19 august 2018");
        BankTransaction first = new BankTransaction("title", date, date, 1, "description", BankTransaction.Type.IN, Arrays.asList("one", "two"));
        BankTransaction second = new BankTransaction("title", date, date, 1, "description", BankTransaction.Type.IN, Arrays.asList("one", "two"));

        BankTransactionGroup firstGroup = new BankTransactionGroup("title");
        firstGroup.addTransaction(first);
        firstGroup.addTransaction(second);

        assertTrue(firstGroup.getType() == BankTransaction.Type.IN);
        assertTrue(firstGroup.getTitle().equals("title"));
        assertTrue(firstGroup.getTotalAmount() == 2);

        first = new BankTransaction("title", date, date, 1, "description", BankTransaction.Type.IN, Arrays.asList("one", "two"));
        second = new BankTransaction("title", date, date, 1, "description", BankTransaction.Type.IN, Arrays.asList("one", "two"));
        BankTransactionGroup secondGroup = new BankTransactionGroup("title");
        secondGroup.addTransaction(first);
        secondGroup.addTransaction(second);
        
        assertTrue(firstGroup.equals(secondGroup));
        
        BankTransactionGroup thirdGroup = new BankTransactionGroup("title");
        List<BankTransaction> incompatibleTransactions = thirdGroup.addTransactions(Arrays.asList(first, second));
        assertTrue(incompatibleTransactions != null && incompatibleTransactions.isEmpty());
        assertTrue(thirdGroup.equals(secondGroup));
    }
}
