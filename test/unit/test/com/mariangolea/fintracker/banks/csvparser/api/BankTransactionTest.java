/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.api;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.BTParser;
import java.math.BigDecimal;

/**
 *
 * @author Marian Golea <marian.golea@microchip.com>
 */
public class BankTransactionTest {

    @Test
    public void testBankTransaction() {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ZERO, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));
        String toString = first.toString();
        assertTrue(toString != null);
        assertTrue(first.isValidatedDuringParse());
        assertTrue(first.transactionTargetIdentified());
        assertTrue(first.getStartDate() == date);
        assertTrue(first.getCompletedDate() == date);
        assertTrue(first.getAmount() == BigDecimal.ZERO);
        assertTrue(first.getDescription().equals("description"));
        assertTrue(first.getType() == BankTransaction.Type.IN);
        assertTrue(first.getTitle().equals(BTParser.OperationID.INCASARE.desc));
        assertTrue(first.getCsvContent().equals(Arrays.asList("one", "two")));
        assertTrue(first.getOriginalCSVContentLinesNumber() == 2);

        BankTransaction second = new BankTransaction(true, true, BTParser.OperationID.INCASARE.desc, date, date, BigDecimal.ZERO, "description",
                BankTransaction.Type.IN, Arrays.asList("one", "two"));

        assertTrue(first.equals(second));
        assertTrue(first.hashCode() == second.hashCode());
        second.getCsvContent().add("hello");
        assertTrue(!first.equals(second));
    }
}
